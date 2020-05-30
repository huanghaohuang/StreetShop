package com.street.shop.entity.product;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "product_banner",
        indexes=@Index(name="product_banner_index", columnList="product_id"))
public class ProductBanner implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT not null ")
    private int id;

    //商品id
    @Column(name = "product_id", columnDefinition = "INT not null default 0 ")
    private int productId;

    //图片
    @Column(name = "url", columnDefinition = "VARCHAR(256) ")
    private String url;

    //视频
    @Column(name = "video", columnDefinition = "VARCHAR(256) ")
    private String video;

    //背景色
    @Column(name = "color", columnDefinition = "VARCHAR(256) ")
    private String color;

    //排序序号
    @Column(name = "sort", columnDefinition = "INT not null default 0 ")
    private int sort;

    //是否删除
    @Column(name = "deleted", columnDefinition = "INT not null default 0 ")
    private int deleted;

    //添加时间
    @Column(name = "create_at", columnDefinition = "datetime not null ")
    private Date createAt;


}
