package com.street.shop.dao.product;

import com.street.shop.entity.product.ProductUnit;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface ProductUnitDao extends PagingAndSortingRepository<ProductUnit, Integer>, JpaSpecificationExecutor<ProductUnit> {
}
