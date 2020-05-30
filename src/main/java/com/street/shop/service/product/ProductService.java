package com.street.shop.service.product;

import com.google.gson.Gson;
import com.street.shop.dao.product.ProductDao;
import com.street.shop.entity.product.Category;
import com.street.shop.entity.product.Product;
import com.street.shop.entity.product.ProductUnit;
import com.street.shop.pojo.ConstDefine;
import com.street.shop.pojo.ProductStatus;
import com.street.shop.pojo.Unit;
import com.street.shop.pojo.UnitSpec;
import com.street.shop.util.ExcelUtil;
import com.street.shop.util.ProductUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.*;

/**
 * 商品服务
 */

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductUnitService productUnitService;

    /**
     * 添加产品
     */
    public String addProduct(
            String name, int shopId, String uniqueCode, int categoryId, int bandId, String logo,
            List<Unit> unitList, List<String> keyWords) {
        String result = "";
        if (name == null || name.length() <= 0) {
            result = "商品名称为空!";
            return result;
        }
        if (shopId <= 0) {
            result = "商户shopId为空!";
            return result;
        }
        if (categoryId <= 0) {
            result = "分类categoryId为空!";
            return result;
        }
        if (uniqueCode == null || uniqueCode.length() <= 0) {
            //生成一个唯一编号
            uniqueCode = UUID.randomUUID().toString().replace("-", "");
        } else {
            //查询是否存在同一个编号的产品
            Product product = getProductByUniqueCode(shopId, uniqueCode);
            if (product != null) {
                result = "已经存在同编码的商品!";
                return result;
            }
        }
        if (unitList == null || unitList.size() <= 0) {
            result = "商品规格信息为空!";
            return result;
        }
        if (logo == null) {
            logo = "";
        }
        Product product = new Product();
        product.setName(name);
        product.setStatus(ProductStatus.OFFLINE);
        product.setShopId(shopId);
        product.setCategoryId(categoryId);
        product.setUniqueCode(uniqueCode);
        product.setBandId(bandId);

        int minOfflinePrice = 0;
        int maxOfflinePrice = 0;
        int minPrice = 0;
        int maxPrice = 0;

        Set<String> keySet = new TreeSet<>();
        if (keyWords != null && keyWords.size() > 0) {
            keySet.addAll(keyWords);
        }
        List<String> categoryNameList = categoryService.getParentCategoryNameList(categoryId);
        if (categoryNameList != null) {
            keySet.addAll(categoryNameList);
        }
        product.setKeyWords(String.join(",", keySet));

        Date createTime = new Date();
        //根据规格列表中的规格价格获取最低价格最高价
        List<ProductUnit> productUnitList = new ArrayList<>();
        for (Unit unit : unitList) {
            int price = unit.getPrice();
            if (minPrice == 0) {
                minPrice = price;
            }
            if (maxPrice == 0) {
                maxPrice = price;
            }
            if (price > maxPrice) {
                maxPrice = price;
            }
            if (price < minPrice) {
                minPrice = price;
            }
            int offlinePrice = unit.getOfflinePrice();
            if (offlinePrice > maxOfflinePrice) {
                maxOfflinePrice = offlinePrice;
            }
            if (offlinePrice < minOfflinePrice) {
                minOfflinePrice = offlinePrice;
            }
            //唯一编号
            String unitUniqueCode = unit.getUniqueCode();
            if (unitUniqueCode != null && unitUniqueCode.length() > 0) {
                ProductUnit productUnit = productUnitService.getProductUnitByUniqueCode(shopId, unitUniqueCode);
                if (productUnit != null) {
                    result = "商品规格编号重复, 编号:" + unitUniqueCode;
                    return result;
                }
            } else {
                //系统分配一个唯一编号
                unitUniqueCode = UUID.randomUUID().toString().replace("-", "");
            }

            ProductUnit productUnit = new ProductUnit();
            productUnit.setShopId(shopId);
            productUnit.setUniqueCode(unitUniqueCode);
            productUnit.setPrice(price);
            productUnit.setOfflinePrice(offlinePrice);
            productUnit.setUnites(ProductUtil.makeSpecStr(unit.getUnitSpecList()));
            productUnit.setCreateAt(createTime);
            productUnitList.add(productUnit);
        }
        product.setMaxPrice(maxPrice);
        product.setMinPrice(minPrice);
        product.setMinOfflinePrice(minOfflinePrice);
        product.setMaxOfflinePrice(maxOfflinePrice);
        product.setLogo(logo);
        product.setCreateAt(new Date());
        try {
            productDao.save(product);
            //产品id
            int productId = product.getProductId();
            for (ProductUnit productUnit : productUnitList) {
                productUnit.setProductId(productId);
                productUnitService.addProductUnit(productUnit);
            }
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    public String addProduct(Product product) {
        String result = "";
        if (product == null) {
            result = "商品信息为空!";
            return result;
        }
        try {
            productDao.save(product);
            //这里没有保存规格明细
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    //根据产品唯一编号获取产品信息
    public Product getProductByUniqueCode(int shopId, String uniqueCode) {
        Product product = null;
        if (uniqueCode == null || uniqueCode.length() <= 0) {
            return product;
        }
        try {
            Specification<Product> spec = (Specification<Product>) (root, crite, cb) -> {
                List<Predicate> pr = new ArrayList<>();
                pr.add(cb.equal(root.get("shopId").as(Integer.class), shopId));
                pr.add(cb.equal(root.get("uniqueCode").as(String.class), uniqueCode));
                return cb.and(pr.toArray(new Predicate[pr.size()]));
            };
            Optional<Product> productOptional = productDao.findOne(spec);
            if (productOptional != null && productOptional.isPresent()) {
                product = productOptional.get();
            }
        } catch (Exception e) {
        }
        return product;
    }

    /**
     * 根据id获取商品信息
     */
    public Product getProductById(int productId) {
        Product product = null;
        if (productId <= 0) {
            return product;
        }
        try {
            Optional<Product> productOptional = productDao.findById(productId);
            if (productOptional != null && productOptional.isPresent()) {
                product = productOptional.get();
                //获取规格详情
            }
        } catch (Exception e) {
        }
        return product;
    }

    /**
     * 从excel文件导入商品信息
     *
     * @param inputStream 文件流
     * @param shopId      商户id
     * @param categoryId  类别id
     * @return
     */
    public String importProductFromExcelFile(String fileName, InputStream inputStream, int shopId, int categoryId) {
        String result = "";
        try {
            if (inputStream == null) {
                result = "文件流为空!";
                return result;
            }
            if (fileName == null || fileName.length() <= 0) {
                result = "文件名为空!";
                return result;
            }
            int index = fileName.indexOf("-");
            if (index == -1) {
                result = "文件名格式不正确!";
                return result;
            }
            String categoryName = fileName.substring(0, index);
            if (categoryService.getCategoryIdByName(categoryName) <= 0) {
                result = "错误的文件名, 请不要修改模板的文件名!";
                return result;
            }
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                result = "未知的分类categoryId!";
                return result;
            }
            Workbook wb = null;
            if (fileName.endsWith("xls")) {
                try {
                    wb = new HSSFWorkbook(inputStream);
                } catch (Exception e) {
                }
            } else {
                if (fileName.endsWith("xlsx")) {
                    try {
                        wb = new XSSFWorkbook(inputStream);
                    } catch (Exception e) {
                    }
                }
            }
            if (wb == null) {
                result = "文件格式不正确!";
                return result;
            }
            Sheet sheet = wb.getSheetAt(0);
            Row firstRow = sheet.getRow(0);
            Date createTime = new Date();
            Map<String, Product> productMap = new HashMap<>();
            int cellNum = sheet.getRow(0).getLastCellNum(); //单元数量
            int lastRowNum = sheet.getLastRowNum() + 1;
            for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                Cell cell = row.getCell(ConstDefine.productNameCellIndex);
                if (cell != null) {
                    String productName = ExcelUtil.getCellValue(cell);
                    if (productName != null && productName.length() > 0) {
                        productName = productName.trim();
                        if (productMap.containsKey(productName)) {
                            Product product = productMap.get(productName);
                            if (product != null) {
                                List<ProductUnit> productUnitList = product.getProductUnitList();
                                if (productUnitList != null) {
                                    ProductUnit productUnit = new ProductUnit();
                                    cell = row.getCell(ConstDefine.unitSpecUniqueCodeCellIndex);
                                    String unitSpecUniqueCode = ExcelUtil.getCellValue(cell);
                                    if (unitSpecUniqueCode == null || unitSpecUniqueCode.length() <= 0) {
                                        unitSpecUniqueCode = UUID.randomUUID().toString().replace("-", "");
                                    }
                                    productUnit.setUniqueCode(unitSpecUniqueCode);
                                    cell = row.getCell(ConstDefine.priceCellIndex);
                                    String priceStr = ExcelUtil.getCellValue(cell);
                                    if (priceStr != null && priceStr.length() > 0) {
                                        try {
                                            productUnit.setPrice(Integer.parseInt(priceStr));
                                        } catch (Exception e) {
                                        }
                                    }
                                    cell = row.getCell(ConstDefine.offlinePriceCellIndex);
                                    String offlinePriceStr = ExcelUtil.getCellValue(cell);
                                    if (offlinePriceStr != null && offlinePriceStr.length() > 0) {
                                        try {
                                            productUnit.setOfflinePrice(Integer.parseInt(offlinePriceStr));
                                        } catch (Exception e) {
                                        }
                                    }
                                    //规格信息
                                    List<UnitSpec> unitSpecList = new ArrayList<>();
                                    for (int cellIndex = ConstDefine.offlinePriceCellIndex + 1; cellIndex < cellNum; cellIndex++) {
                                        String key = ExcelUtil.getCellValue(firstRow.getCell(cellIndex));
                                        String value = ExcelUtil.getCellValue(row.getCell(cellIndex));
                                        if (key != null && key.length() > 0 &&
                                                value != null && value.length() > 0) {
                                            UnitSpec unitSpec = new UnitSpec();
                                            unitSpec.setKey(key);
                                            unitSpec.setValue(value);
                                            unitSpecList.add(unitSpec);
                                        }
                                    }
                                    String unites = ProductUtil.makeSpecStr(unitSpecList);
                                    productUnit.setUnites(unites);
                                    productUnit.setShopId(shopId);
                                    productUnit.setCreateAt(createTime);
                                    productUnitList.add(productUnit);
                                }
                            }
                        } else {
                            Product product = new Product();
                            product.setName(productName);
                            cell = row.getCell(ConstDefine.productUniqueCodeCellIndex);
                            String productUniquecode = ExcelUtil.getCellValue(cell);
                            if (productUniquecode == null || productUniquecode.length() <= 0) {
                                productUniquecode = UUID.randomUUID().toString().replace("-", "");
                            }
                            product.setUniqueCode(productUniquecode);
                            product.setLogo("");
                            product.setCategoryId(categoryId);
                            product.setShopId(shopId);
                            product.setCreateAt(createTime);

                            List<ProductUnit> productUnitList = product.getProductUnitList();
                            ProductUnit productUnit = new ProductUnit();
                            cell = row.getCell(ConstDefine.unitSpecUniqueCodeCellIndex);
                            String unitSpecUniqueCode = ExcelUtil.getCellValue(cell);
                            if (unitSpecUniqueCode == null || unitSpecUniqueCode.length() <= 0) {
                                unitSpecUniqueCode = UUID.randomUUID().toString().replace("-", "");
                            }
                            productUnit.setUniqueCode(unitSpecUniqueCode);
                            cell = row.getCell(ConstDefine.priceCellIndex);
                            String priceStr = ExcelUtil.getCellValue(cell);
                            if (priceStr != null && priceStr.length() > 0) {
                                try {
                                    productUnit.setPrice(Integer.parseInt(priceStr));
                                } catch (Exception e) {
                                }
                            }
                            cell = row.getCell(ConstDefine.offlinePriceCellIndex);
                            String offlinePriceStr = ExcelUtil.getCellValue(cell);
                            if (offlinePriceStr != null && offlinePriceStr.length() > 0) {
                                try {
                                    productUnit.setOfflinePrice(Integer.parseInt(offlinePriceStr));
                                } catch (Exception e) {
                                }
                            }
                            //规格信息
                            List<UnitSpec> unitSpecList = new ArrayList<>();
                            for (int cellIndex = ConstDefine.offlinePriceCellIndex + 1; cellIndex < cellNum; cellIndex++) {
                                String key = ExcelUtil.getCellValue(firstRow.getCell(cellIndex));
                                String value = ExcelUtil.getCellValue(row.getCell(cellIndex));
                                if (key != null && key.length() > 0 &&
                                        value != null && value.length() > 0) {
                                    UnitSpec unitSpec = new UnitSpec();
                                    unitSpec.setKey(key);
                                    unitSpec.setValue(value);
                                    unitSpecList.add(unitSpec);
                                }
                            }
                            String unites = ProductUtil.makeSpecStr(unitSpecList);
                            productUnit.setUnites(unites);
                            productUnit.setShopId(shopId);
                            productUnit.setCreateAt(createTime);
                            productUnitList.add(productUnit);

                            productMap.put(productName, product);
                        }
                    }
                }
            }
            if (productMap != null && productMap.size() > 0) {
                //遍历
                for (Map.Entry<String, Product> entry : productMap.entrySet()) {
                    Product product = entry.getValue();
                    //计算最大和最小
                    int minPrice = 0;
                    int maxPrice = 0;
                    int minOfflinePrice = 0;
                    int maxOfflinePrice = 0;
                    List<ProductUnit> productUnitList = product.getProductUnitList();
                    if (productUnitList != null && productUnitList.size() > 0) {
                        for (ProductUnit productUnit : productUnitList) {
                            int price = productUnit.getPrice();
                            int offlinePrice = productUnit.getOfflinePrice();
                            if (minPrice == 0) {
                                minPrice = price;
                            }
                            if (minOfflinePrice == 0) {
                                minOfflinePrice = offlinePrice;
                            }
                            if (price < minPrice) {
                                minPrice = price;
                            }
                            if (price > maxPrice) {
                                maxPrice = price;
                            }
                            if (offlinePrice < minOfflinePrice) {
                                minOfflinePrice = offlinePrice;
                            }
                            if (offlinePrice > maxOfflinePrice) {
                                maxOfflinePrice = offlinePrice;
                            }
                        }
                    }
                    product.setMinPrice(minPrice);
                    product.setMaxPrice(maxPrice);
                    product.setMinOfflinePrice(minOfflinePrice);
                    product.setMaxOfflinePrice(maxOfflinePrice);
                    //添加
                    productDao.save(product);
                    int productId = product.getProductId();
                    for (ProductUnit productUnit : productUnitList) {
                        productUnit.setProductId(productId);
                        productUnitService.addProductUnit(productUnit);
                    }
                }
            }
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
            }
        }
        return result;
    }


    //修改基本信息
    public String updateProduct(int operatorUserId, Map<String, Object> updateInfo) {
        String result = "";
        if (updateInfo == null || updateInfo.size() <= 0) {
            result = "修改的信息为空!";
            return result;
        }
        try {
            //判断操作人员是否有权限修改


            //产品id
            int productId = 0;
            if (updateInfo.get("productId") != null) {
                try {
                    productId = Integer.parseInt(updateInfo.get("productId").toString());
                } catch (Exception e) {
                }
            }
            if (productId <= 0) {
                result = "产品id为空!";
                return result;
            }
            //商户id
            int shopId = 0;
            if (updateInfo.get("shopId") != null) {
                try {
                    shopId = Integer.parseInt(updateInfo.get("shopId").toString());
                } catch (Exception e) {
                }
            }
            if (shopId <= 0) {
                result = "商户id为空!";
                return result;
            }
            Product product = getProductById(productId);
            if (product == null) {
                result = "产品信息不存在!";
                return result;
            }
            //唯一编号
            if (updateInfo.get("uniqueCode") != null) {
                String uniqueCode = updateInfo.get("uniqueCode").toString();
                if (uniqueCode != null && uniqueCode.length() > 0) {
                    if (product.getUniqueCode() != null &&
                            !product.getUniqueCode().equals(uniqueCode)) {
                        //修改唯一编号
                        if (getProductByUniqueCode(shopId, uniqueCode) != null) {
                            result = "已经存在同编码的商品, 修改编码再提交!";
                            return result;
                        }
                        product.setUniqueCode(uniqueCode);
                    }
                }
            }
            //产品名称
            if (updateInfo.get("name") != null) {
                String name = updateInfo.get("name").toString();
                if (name != null && name.length() > 0) {
                    product.setName(name);
                }
            }
            //分类
            if (updateInfo.get("categoryId") != null) {
                int categoryId = 0;
                try {
                    categoryId = Integer.parseInt(updateInfo.get("categoryId").toString());
                    if (categoryId > 0) {
                        product.setCategoryId(categoryId);
                    }
                } catch (Exception e) {
                }
            }
            if (updateInfo.get("bandId") != null) {
                int bandId = 0;
                try {
                    bandId = Integer.parseInt(updateInfo.get("bandId").toString());
                    product.setBandId(bandId);
                } catch (Exception e) {
                }
            }
            if (updateInfo.get("logo") != null) {
                String logo = updateInfo.get("logo").toString();
                if (logo != null && logo.length() > 0) {
                    product.setLogo(logo);
                }
            }
            //规格信息
            boolean productUnitListChange = false;
            if (updateInfo.get("productUnitList") != null) {
                String productUnitListJson = updateInfo.get("productUnitList").toString();
                if (productUnitListJson != null && productUnitListJson.length() > 0) {
                    List<ProductUnit> productUnitList = new ArrayList<>();
                    productUnitList = new Gson().fromJson(productUnitListJson, productUnitList.getClass());
                    if (productUnitList != null && productUnitList.size() > 0) {
                        int minPrice = 0;
                        int maxPrice = 0;
                        int minOfflinePrice = 0;
                        int maxOfflinePrice = 0;
                        for (ProductUnit productUnit : productUnitList) {
                            int productUnitId = productUnit.getProductUnitId();
                            if (productUnitId <= 0) {
                                result = "产品规格id为空!";
                                return result;
                            }
                            if (productUnit.getProductId() != productId) {
                                result = "产品规格信息中的产品id不等于产品id!";
                                return result;
                            }
                            int price = productUnit.getPrice();
                            int offlinePrice = productUnit.getOfflinePrice();
                            if (minPrice == 0) {
                                minPrice = price;
                            }
                            if (minOfflinePrice == 0) {
                                minOfflinePrice = offlinePrice;
                            }
                            if (price < minPrice) {
                                minPrice = price;
                            }
                            if (price > maxPrice) {
                                maxPrice = price;
                            }
                            if (offlinePrice < minOfflinePrice) {
                                minOfflinePrice = offlinePrice;
                            }
                            if (offlinePrice > maxOfflinePrice) {
                                maxOfflinePrice = offlinePrice;
                            }
                        }
                        product.setMinPrice(minPrice);
                        product.setMaxPrice(maxPrice);
                        product.setMinOfflinePrice(minOfflinePrice);
                        product.setMaxOfflinePrice(maxOfflinePrice);

                        product.setProductUnitList(productUnitList);
                        productUnitListChange = true;
                    }
                }
            }
            //关键词
            if (updateInfo.get("keyWords") != null) {
                String keyWordsJson = updateInfo.get("keyWords").toString();
                if (keyWordsJson != null && keyWordsJson.length() > 0) {
                    List<String> keyWordList = new ArrayList<>();
                    keyWordList = new Gson().fromJson(keyWordsJson, keyWordList.getClass());
                    if (keyWordList != null && keyWordList.size() > 0) {
                        Set<String> keySet = new TreeSet<>();
                        keySet.addAll(keyWordList);
                        List<String> categoryNameList = categoryService.getParentCategoryNameList(product.getCategoryId());
                        if (categoryNameList != null) {
                            keySet.addAll(categoryNameList);
                        }
                        product.setKeyWords(String.join(",", keySet));
                    }
                }
            }

            productDao.save(product);
            if (productUnitListChange) {
                //修改规格
                List<ProductUnit> productUnitList = product.getProductUnitList();
                if (productUnitList != null) {
                    for (ProductUnit productUnit : productUnitList) {
                        productUnit.setProductId(productId);
                        productUnitService.addProductUnit(productUnit);
                    }
                }
            }
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //修改状态
    //上线
    public String setProductOnline(int operatorUserId, int productId) {
        String result = "";
        if (productId <= 0) {
            result = "产品id为空!";
            return result;
        }
        Product product = getProductById(productId);
        if (product == null) {
            result = "产品信息不存在!";
            return result;
        }
        //判断操作人员的权限
        if (product.getStatus() == ProductStatus.FORBIDDEN) {
            result = "此商品已被管理员禁用, 请联系客服!";
            return result;
        }
        if (product.getLogo() == null ||
                product.getLogo().length() <= 0) {
            result = "商品没有缩略图暂不能上架!";
            return result;
        }
        try {
            product.setStatus(ProductStatus.ONLINE);
            productDao.save(product);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    //下线
    public String setProductOffline(int operatorUserId, int productId) {
        String result = "";
        if (productId <= 0) {
            result = "产品id为空!";
            return result;
        }
        Product product = getProductById(productId);
        if (product == null) {
            result = "产品信息不存在!";
            return result;
        }
        //判断操作人员的权限
        if (product.getStatus() == ProductStatus.FORBIDDEN) {
            result = "此商品已被管理员禁用, 请联系客服!";
            return result;
        }
        try {
            product.setStatus(ProductStatus.OFFLINE);
            productDao.save(product);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    /**
     * 给商品批量添加规格信息
     */
    public String addProductUnitBatch(int productId, List<Unit> unitList) {
        String result = "";
        if (productId <= 0) {
            result = "商品id为空!";
            return result;
        }
        if (unitList == null || unitList.size() <= 0) {
            result = "商品规格信息为空!";
            return result;
        }
        try {
            Product product = getProductById(productId);
            if (product == null) {
                result = "商品信息不存在!";
                return result;
            }
            List<ProductUnit> productUnitList = new ArrayList<>();
            int shopId = product.getShopId();
            Date createTime = new Date();
            for (Unit unit : unitList) {
                String unitSpecUniqueCode = unit.getUniqueCode();
                if (unitSpecUniqueCode == null || unitSpecUniqueCode.length() <= 0) {
                    unitSpecUniqueCode = UUID.randomUUID().toString().replace("-", "");
                } else {
                    //判断是否已经存在同一个编号的规格信息
                    ProductUnit productUnit = productUnitService.getProductUnitByUniqueCode(shopId, unitSpecUniqueCode);
                    if (productUnit != null) {
                        result = "存在同样的商品规格编号!";
                        return result;
                    }
                }
                ProductUnit productUnit = new ProductUnit();
                productUnit.setUniqueCode(unitSpecUniqueCode);
                productUnit.setPrice(unit.getPrice());
                productUnit.setOfflinePrice(unit.getOfflinePrice());
                //规格信息
                List<UnitSpec> unitSpecList = unit.getUnitSpecList();
                String unites = ProductUtil.makeSpecStr(unitSpecList);
                productUnit.setUnites(unites);
                productUnit.setShopId(shopId);
                productUnit.setCreateAt(createTime);
                productUnitList.add(productUnit);
            }
            if (productUnitList != null) {
                for (ProductUnit productUnit : productUnitList) {
                    productUnit.setProductId(productId);
                    productUnitService.addProductUnit(productUnit);
                }
            }
            //修改最后的价格
            product = getProductById(productId);
            if (product != null) {
                productUnitList = product.getProductUnitList();
                int minPrice = 0;   //最小线上价格
                int maxPrice = 0;   //最大线上价格
                int minOfflinePrice = 0;    //最小线下价格
                int maxOfflinePrice = 0; //最大线下价格
                if (productUnitList != null && productUnitList.size() > 0) {
                    for (ProductUnit productUnit : productUnitList) {
                        int price = productUnit.getPrice();
                        int offlinePrice = productUnit.getOfflinePrice();
                        if (minPrice == 0) {
                            minPrice = price;
                        }
                        if (minOfflinePrice == 0) {
                            minOfflinePrice = offlinePrice;
                        }
                        if (price < minPrice) {
                            minPrice = price;
                        }
                        if (price > maxPrice) {
                            maxPrice = price;
                        }
                        if (offlinePrice < minOfflinePrice) {
                            minOfflinePrice = offlinePrice;
                        }
                        if (offlinePrice > maxOfflinePrice) {
                            maxOfflinePrice = offlinePrice;
                        }
                    }
                }
                product.setMinPrice(minPrice);
                product.setMaxPrice(maxPrice);
                product.setMinOfflinePrice(minOfflinePrice);
                product.setMaxOfflinePrice(maxOfflinePrice);
                //修改价格
                productDao.save(product);
            }
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


}
