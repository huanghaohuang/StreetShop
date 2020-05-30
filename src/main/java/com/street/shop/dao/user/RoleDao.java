package com.street.shop.dao.user;

import com.street.shop.entity.user.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface RoleDao extends PagingAndSortingRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
}
