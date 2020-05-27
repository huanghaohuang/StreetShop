package com.street.shop.service.product;

import com.street.shop.dao.product.CategoryDao;
import com.street.shop.entity.product.Category;
import com.street.shop.pojo.ConstDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * 类别服务
 */

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategorySpecService categorySpecService;

    /**
     * 添加类别
     * @param name              类别名称
     * @param parentId          父节点(目前支持两层类别层次)
     * @param img               类别图片
     * @param orderFlag         显示排列序号
     * @return
     */
    public String addCategory(String name, int parentId, String img, int orderFlag) {
        String result = "";
        if (name == null || name.length() <= 0) {
            result = "分类名称为空!";
            return result;
        }
        if (parentId > 0) {
            Category parentCategory = getCategoryById(parentId);
            if (parentCategory == null) {
                result = "父类信息为空!";
                return result;
            }
        }
        if (img == null) {
            img = "";
        }
        try {
            Category category = new Category();
            category.setName(name);
            category.setParentId(parentId);
            category.setImg(img);
            category.setOrderFlag(orderFlag);
            category.setCreatedTime(new Date());
            category.setDeleted(0);
            categoryDao.save(category);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    //获取分类
    public Category getCategoryById(int categoryId) {
        Category category = null;
        try {
            Optional<Category> categoryOptional = categoryDao.findById(categoryId);
            if (categoryOptional != null &&
                    categoryOptional.isPresent()) {
                category = categoryOptional.get();
                //获取类别规格列表
                List<String> specs = categorySpecService.getCategorySpecList(category.getId());
                category.setSpecs(specs);
            }
        } catch (Exception e) {
        }
        return category;
    }


    //修改类别
    public String updateCategory(Map<String, Object> updateInfo) {
        String result = "";
        if (updateInfo == null || updateInfo.size() <= 0) {
            result = "修改信息为空!";
            return result;
        }
        try {
            int categoryId = 0;
            if (updateInfo.get("categoryId") != null) {
                categoryId = Integer.parseInt(updateInfo.get("categoryId").toString());
            }
            if (categoryId <= 0) {
                result = "类别id为空!";
                return result;
            }
            Category category = getCategoryById(categoryId);
            if (category == null) {
                result = "类别信息不存在!";
                return result;
            }
            if (updateInfo.get("name") != null) {
                category.setName(updateInfo.get("name").toString());
            }
            if (updateInfo.get("img") != null) {
                category.setImg(updateInfo.get("img").toString());
            }
            if (updateInfo.get("parentId") != null) {
                try{
                    int parentId = Integer.parseInt(updateInfo.get("parentId").toString());
                    category.setParentId(parentId);
                }
                catch(Exception e){
                }
            }
            if (updateInfo.get("orderFlag") != null) {
                try{
                    int orderFlag = Integer.parseInt(updateInfo.get("orderFlag").toString());
                    category.setOrderFlag(orderFlag);
                }
                catch(Exception e){
                }
            }
            categoryDao.save(category);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    //删除类别
    public String delCategory(int categoryId) {
        String result = "";
        try {
            //删除子节点
            List<Category> categoryList = getCategoryList(categoryId);
            if (categoryList != null) {
                for (Category category : categoryList) {
                    categoryDao.deleteById(category.getId());
                    //删除节点规格
                    categorySpecService.delCategorySpecList(category.getId());
                }
            }
            //删除本节点
            categoryDao.deleteById(categoryId);
            //删除本节点的规格信息
            categorySpecService.delCategorySpecList(categoryId);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //获取子类列表
    public List<Category> getCategoryList(int parentId) {
        List<Category> categoryList = new ArrayList<>();
        try {
            Specification<Category> spec = (Specification<Category>) (root, crite, cb) -> {
                List<Predicate> pr = new ArrayList<>();
                pr.add(cb.equal(root.get("parentId").as(Integer.class), parentId));
                return cb.and(pr.toArray(new Predicate[pr.size()]));
            };
            categoryList = categoryDao.findAll(spec);
            if (categoryList != null) {
                for (Category category : categoryList) {
                    //获取规格
                    List<String> specs = categorySpecService.getCategorySpecList(category.getId());
                    if (specs == null) {
                        specs = new ArrayList<>();
                    }
                    category.setSpecs(specs);
                }
            }
        } catch (Exception e) {
        }
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        return categoryList;
    }


}

