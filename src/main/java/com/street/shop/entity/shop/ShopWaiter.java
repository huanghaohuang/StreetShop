package com.street.shop.entity.shop;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "shop_waiter",
        uniqueConstraints={@UniqueConstraint(columnNames={"shop_id", "user_id", "role_id"})},
        indexes=@Index(name="shop_waiter_index", columnList="shop_id, user_id"))
public class ShopWaiter implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT not null ")
    private int id;

    @Column(name = "shop_id", columnDefinition = "INT not null default 0 ")
    private int shopId;

    //用户id
    @Column(name = "user_id", columnDefinition = "INT not null default 0 ")
    private int userId;

    //角色id
    @Column(name = "role_id", columnDefinition = "INT not null default 0 ")
    private int roleId;

    //删除
    @Column(name = "deleted", columnDefinition = "INT not null default 0 ")
    private int deleted;

    @Column(name = "create_at", columnDefinition = "datetime not null ")
    private Date createAt;

}
