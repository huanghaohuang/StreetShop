package com.street.shop.dao.shop;

import com.street.shop.entity.shop.ShopWaiter;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface ShopWaiterDao extends PagingAndSortingRepository<ShopWaiter, Integer>, JpaSpecificationExecutor<ShopWaiter> {
}
