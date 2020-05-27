package com.street.shop.dao.product;

import com.street.shop.entity.product.Category;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface CategoryDao extends PagingAndSortingRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
}
