package com.street.shop.service.product;

import com.street.shop.dao.product.ProductUnitDao;
import com.street.shop.entity.product.ProductUnit;
import com.street.shop.pojo.ConstDefine;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductUnitService {

    @Autowired
    private ProductUnitDao productUnitDao;

    /**
     * 添加商品规格
     *
     * @param productUnit
     * @return
     */
    public String addProductUnit(ProductUnit productUnit) {
        String result = "";
        if (productUnit == null) {
            return result;
        }
        try {
            productUnitDao.save(productUnit);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    public ProductUnit getProductUnitByUniqueCode(int shopId, String uniqueCode) {
        ProductUnit productUnit = null;
        if (uniqueCode == null || uniqueCode.length() <= 0) {
            return productUnit;
        }
        try {
            Specification<ProductUnit> spec = (Specification<ProductUnit>) (root, crite, cb) -> {
                List<Predicate> pr = new ArrayList<>();
                pr.add(cb.equal(root.get("shopId").as(Integer.class), shopId));
                pr.add(cb.equal(root.get("uniqueCode").as(String.class), uniqueCode));
                return cb.and(pr.toArray(new Predicate[pr.size()]));
            };
            Optional<ProductUnit> productUnitOptional = productUnitDao.findOne(spec);
            if (productUnitOptional != null && productUnitOptional.isPresent()) {
                productUnit = productUnitOptional.get();
            }
        } catch (Exception e) {
        }
        return productUnit;
    }



    public List<ProductUnit> getProductUnitList(int productId) {
        List<ProductUnit> productUnitList = new ArrayList<>();
        try {
            Specification<ProductUnit> spec = (Specification<ProductUnit>) (root, crite, cb) -> {
                List<Predicate> pr = new ArrayList<>();
                pr.add(cb.equal(root.get("productId").as(Integer.class), productId));
                return cb.and(pr.toArray(new Predicate[pr.size()]));
            };
            productUnitList = productUnitDao.findAll(spec);
        } catch (Exception e) {
        }
        return productUnitList;
    }

    //根据id获取商品规格信息
    public ProductUnit getProductUnitById(int productUnitId) {
        ProductUnit productUnit = null;
        try {
            Optional<ProductUnit> productUnitOptional = productUnitDao.findById(productUnitId);
            if (productUnitOptional != null && productUnitOptional.isPresent()) {
                productUnit = productUnitOptional.get();
            }
        } catch (Exception e) {
        }
        return productUnit;
    }


    /**
     * 修改商品的规格价格和状态
     */
    public String updateProductUnit(int productUnitId, int price, int offlinePrice, int status) {
        String result = "";
        if (productUnitId <= 0) {
            result = "商品规格id为空!";
            return result;
        }
        if (status != 0 && status != 1) {
            result = "该规格商品状态不正确!";
            return result;
        }
        try {
            ProductUnit productUnit = getProductUnitById(productUnitId);
            if (productUnit == null) {
                result = "该商品规格信息不存在!";
                return result;
            }
            productUnit.setPrice(price);
            productUnit.setOfflinePrice(offlinePrice);
            productUnit.setStatus(status);
            productUnitDao.save(productUnit);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    /**
     * 批量添加商品规格
     */


}
