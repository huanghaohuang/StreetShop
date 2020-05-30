package com.street.shop.service.product;

import com.street.shop.dao.product.CategorySpecDao;
import com.street.shop.entity.product.CategorySpec;
import com.street.shop.pojo.ConstDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
public class CategorySpecService {

    @Autowired
    private CategorySpecDao categorySpecDao;

    /**
     * 添加类别规格
     * @param spec
     * @param categoryId
     * @return
     */
    public String addCategorySpec(String spec, int categoryId) {
        String result = "";
        if (spec == null || spec.length() <= 0) {
            result = "规格名称为空!";
            return result;
        }
        if (categoryId <= 0) {
            result = "分类id为空!";
            return result;
        }
        try {
            CategorySpec categorySpec = new CategorySpec();
            categorySpec.setCategoryId(categoryId);
            categorySpec.setSpec(spec);
            categorySpec.setCreatedAt(new Date());
            categorySpecDao.save(categorySpec);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //删除
    public String delCategorySpecById(int categorySpecId) {
        String result = "";
        if (categorySpecId <= 0) {
            result = "规格id为空!";
            return result;
        }
        try {
            categorySpecDao.deleteById(categorySpecId);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //删除某个类别的所有规格
    public String delCategorySpecList(int categoryId) {
        String result = "";
        try {
            Specification<CategorySpec> spec = (Specification<CategorySpec>) (root, crite, cb) -> {
                List<Predicate> pr = new ArrayList<>();
                pr.add(cb.equal(root.get("categoryId").as(Integer.class), categoryId));
                return cb.and(pr.toArray(new Predicate[pr.size()]));
            };
            List<CategorySpec> categorySpecList = categorySpecDao.findAll(spec);
            if (categorySpecList != null) {
                for (CategorySpec categorySpec : categorySpecList) {
                    categorySpecDao.delete(categorySpec);
                }
            }
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }




    public CategorySpec getCategorySpecById(int categorySpecId) {
        CategorySpec categorySpec = null;
        try {
            Optional<CategorySpec> categorySpecOptional = categorySpecDao.findById(categorySpecId);
            if (categorySpecOptional != null &&
                    categorySpecOptional.isPresent()) {
                categorySpec = categorySpecOptional.get();
            }
        } catch (Exception e) {
        }
        return categorySpec;
    }


    //修改
    public String updateCategorySpec(int categorySpecId, String spec, int categoryId) {
        String result = "";
        if (spec == null || spec.length() <= 0) {
            result = "规格名称为空!";
            return result;
        }
        if (categoryId <= 0) {
            result = "分类id为空!";
            return result;
        }
        try {
            CategorySpec categorySpec = getCategorySpecById(categorySpecId);
            if (categorySpec == null) {
                result = "规格信息不存在!";
                return result;
            }
            categorySpec.setSpec(spec);
            categorySpec.setCategoryId(categoryId);
            categorySpecDao.save(categorySpec);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //获取列表
    public List<String> getCategorySpecList(int categoryId) {
        List<String> specList = new ArrayList<>();
        try {
            Specification<CategorySpec> spec = (Specification<CategorySpec>) (root, crite, cb) -> {
                List<Predicate> pr = new ArrayList<>();
                pr.add(cb.equal(root.get("categoryId").as(Integer.class), categoryId));
                return cb.and(pr.toArray(new Predicate[pr.size()]));
            };
            List<CategorySpec> categorySpecList = categorySpecDao.findAll(spec);
            if (categorySpecList != null) {
                for (CategorySpec categorySpec : categorySpecList) {
                    specList.add(categorySpec.getSpec());
                }
            }
        } catch (Exception e) {
        }
        return specList;
    }


}
