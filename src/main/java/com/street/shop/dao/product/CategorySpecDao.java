package com.street.shop.dao.product;

import com.street.shop.entity.product.CategorySpec;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface CategorySpecDao extends PagingAndSortingRepository<CategorySpec, Integer>, JpaSpecificationExecutor<CategorySpec> {
}
