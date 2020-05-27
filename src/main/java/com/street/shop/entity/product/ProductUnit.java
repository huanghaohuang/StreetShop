package com.street.shop.entity.product;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 产品规格
 */

@Data
@Entity
@Table(name = "product_unit",
        uniqueConstraints={@UniqueConstraint(columnNames={"product_id", "unites"})},
        indexes=@Index(name="product_unit_index", columnList="product_id, shop_id"))
public class ProductUnit implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_unit_id", columnDefinition = "INT not null ")
    private int productUnitId;

    //商品id
    @Column(name = "product_id", columnDefinition = "INT not null default 0 ")
    private int productId;

    //规格详细信息(键值对)
    @Column(name = "unites", columnDefinition = "VARCHAR(2048) not null default '' ")
    private String unites;

    //商户id
    @Column(name = "shop_id", columnDefinition = "INT not null default 0 ")
    private int shopId;

    //线上价格
    @Column(name = "price", columnDefinition = "INT not null default 0 ")
    private int price;

    //线下价格
    @Column(name = "offline_price", columnDefinition = "INT not null default 0 ")
    private int offlinePrice;

    //状态(1:缺货)
    @Column(name = "status", columnDefinition = "INT not null default 0 ")
    private int status;

    //规格唯一编号
    @Column(name = "unit_id", columnDefinition = "VARCHAR(128) not null default '' ")
    private String unitId;

    //添加时间
    @Column(name = "create_time", columnDefinition = "datetime not null ")
    private Date createTime;


}
