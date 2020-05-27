package com.street.shop.dao.product;

import com.street.shop.entity.product.ProductBanner;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;


@Transactional
public interface ProductBannerDao extends PagingAndSortingRepository<ProductBanner, Integer>, JpaSpecificationExecutor<ProductBanner> {
}
