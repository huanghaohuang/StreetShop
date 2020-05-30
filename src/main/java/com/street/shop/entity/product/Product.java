package com.street.shop.entity.product;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品信息
 */

@Data
@Entity
@Table(name = "product",
        uniqueConstraints={@UniqueConstraint(columnNames={"shop_id", "unique_code"})},
        indexes=@Index(name="product_index", columnList="shop_id, category_id"))
public class Product implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", columnDefinition = "INT not null ")
    private int productId;

    //商户id
    @Column(name = "shop_id", columnDefinition = "INT not null default 0 ")
    private int shopId;

    //唯一编号
    @Column(name = "unique_code", columnDefinition = "VARCHAR(32) not null default '' ")
    private String uniqueCode;

    //商品分类Id
    @Column(name = "category_id", columnDefinition = "INT not null default 0 ")
    private int categoryId;

    //商品名称
    @Column(name = "name", columnDefinition = "VARCHAR(256) not null default '' ")
    private String name;

    //商品状态定义
    @Column(name = "status", columnDefinition = "INT not null default 0 ")
    private int status;

    //商品缩略图
    @Column(name = "logo", columnDefinition = "VARCHAR(512) not null default '' ")
    private String logo;

    //品牌Id
    @Column(name = "band_id", columnDefinition = "INT not null default 0 ")
    private int bandId;

    //最低线上价格
    @Column(name = "min_price", columnDefinition = "INT not null default 0 ")
    private int minPrice;

    //最高线上价格
    @Column(name = "max_price", columnDefinition = "INT not null default 0 ")
    private int maxPrice;

    //最低线下价格
    @Column(name = "min_offline_price", columnDefinition = "INT not null default 0 ")
    private int minOfflinePrice;

    //最高线下价格
    @Column(name = "max_offline_price", columnDefinition = "INT not null default 0 ")
    private int maxOfflinePrice;

    //关键词
    @Column(name = "keyWords", columnDefinition = "VARCHAR(512) ")
    private String keyWords;

    //商品优惠记录 默认为0 表示没有
    @Column(name = "discount_id", columnDefinition = "INT not null default 0 ")
    private int discountId;

    //类型
    @Column(name = "type", columnDefinition = "INT not null default 0 ")
    private int type;

    //规格列表
    @Transient
    private List<ProductUnit> productUnitList = new ArrayList<>();

    //添加时间
    @Column(name = "create_at", columnDefinition = "datetime not null ")
    private Date createAt;

}
