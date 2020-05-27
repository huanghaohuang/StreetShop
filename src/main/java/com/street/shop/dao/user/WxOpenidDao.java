package com.street.shop.dao.user;

import com.street.shop.entity.user.WxOpenid;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface WxOpenidDao extends PagingAndSortingRepository<WxOpenid, Integer>, JpaSpecificationExecutor<WxOpenid> {
}
