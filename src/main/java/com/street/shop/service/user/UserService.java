package com.street.shop.service.user;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    //获取人员的角色信息
    public Set<String> getUserRole(int userId) {
        Set<String> roleSet = new HashSet<>();
        if (userId <= 0) {
            return roleSet;
        }
        return roleSet;
    }


}
