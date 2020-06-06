package com.street.shop.dao.user;

import com.street.shop.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserRoleDao extends PagingAndSortingRepository<UserRole, Integer>, JpaSpecificationExecutor<UserRole> {
}
