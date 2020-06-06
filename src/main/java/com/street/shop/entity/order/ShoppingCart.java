package com.street.shop.entity.order;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 购物车
 */

@Data
@Entity
@Table(name = "shopingcart",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "product_unit_id"})},
        indexes = @Index(name = "shopingcart_index", columnList = "user_id, product_unit_id"))
public class ShoppingCart implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopingcart_id", columnDefinition = "INT not null ")
    private int shopingcartId;

    //用户id
    @Column(name = "user_id", columnDefinition = "INT not null default 0 ")
    private int userId;

    //产品规格id
    @Column(name = "product_unit_id", columnDefinition = "INT not null default 0 ")
    private int productUnitId;

    //商户id
    @Column(name = "shop_id", columnDefinition = "INT not null default 0 ")
    private int shopId;

    //数量
    @Column(name = "count", columnDefinition = "INT not null default 0 ")
    private int count;

    //添加时间
    @Column(name = "create_at", columnDefinition = "datetime not null ")
    private Date createAt;

}
