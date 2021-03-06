package com.street.shop.dao.product;

import com.street.shop.entity.product.Discount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;


@Transactional
public interface DiscountDao extends PagingAndSortingRepository<Discount, Integer>, JpaSpecificationExecutor<Discount> {
}
