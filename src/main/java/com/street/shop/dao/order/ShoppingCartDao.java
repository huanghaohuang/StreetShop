package com.street.shop.dao.order;

import com.street.shop.entity.order.ShoppingCart;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;


@Transactional
public interface ShoppingCartDao extends PagingAndSortingRepository<ShoppingCart, Integer>, JpaSpecificationExecutor<ShoppingCart> {
}
