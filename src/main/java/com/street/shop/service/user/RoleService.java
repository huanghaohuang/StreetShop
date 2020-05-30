package com.street.shop.service.user;

import com.street.shop.dao.user.RoleDao;
import com.street.shop.entity.user.Role;
import com.street.shop.pojo.ConstDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    private static Map<String, Role> roleMap = new HashMap<>();

    @PostConstruct
    public void init() {
        String nameEn = "SUPER_ADMIN";
        String nameCh = "网站管理员";
        String description = "网站管理员";
        addRole(nameCh, nameEn, description);

        nameEn = "SHOP_ADMIN";
        nameCh = "商铺管理员";
        description = "商铺管理员";
        addRole(nameCh, nameEn, description);

        nameEn = "SHOP_WAITER";
        nameCh = "商铺客服";
        description = "商铺客服";
        addRole(nameCh, nameEn, description);

        nameEn = "COURIER";
        nameCh = "快递员";
        description = "快递员";
        addRole(nameCh, nameEn, description);

        nameEn = "LIAISON";
        nameCh = "商家关系维护员";
        description = "商家关系维护员";
        addRole(nameCh, nameEn, description);
    }


    public String addRole(String nameCh, String nameEn, String description) {
        String result = "";
        if (nameCh == null || nameCh.length() <= 0) {
            return result;
        }
        if (nameEn == null || nameEn.length() <= 0) {
            return result;
        }
        if (description == null) {
            description = "";
        }
        try {
            Role role = new Role();
            role.setNameEn(nameEn);
            role.setNameCh(nameEn);
            role.setDescription(description);
            role.setCreateAt(new Date());
            roleDao.save(role);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    public Role getRoleById(int roleId) {
        Role role = null;
        if (roleId <= 0) {
            return role;
        }
        try {
            Optional<Role> roleOptional = roleDao.findById(roleId);
            if (roleOptional != null && roleOptional.isPresent()) {
                role = roleOptional.get();
            }
        } catch (Exception e) {
        }
        return role;
    }


    public Role getRoleByName(String nameEn) {
        Role role = null;
        if (nameEn == null || nameEn.length() <= 0) {
            return role;
        }
        if (roleMap == null || roleMap.size() <= 0) {
            try {
                Iterable<Role> roleIterable = roleDao.findAll();
                if (roleIterable != null) {
                    Iterator<Role> roleIterator = roleIterable.iterator();
                    if (roleIterator != null) {
                        while (roleIterator.hasNext()) {
                            Role roleInfo = roleIterator.next();
                            String roleNameEn = roleInfo.getNameEn();
                            if (!roleMap.containsKey(roleNameEn)) {
                                roleMap.put(roleNameEn, roleInfo);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        if (roleMap.containsKey(nameEn)) {
            role = roleMap.get(nameEn);
        }
        return role;
    }


}
