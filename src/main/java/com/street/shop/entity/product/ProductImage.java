package com.street.shop.entity.product;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品图片
 */

@Data
@Entity
@Table(name = "product_image",
        indexes=@Index(name="product_image_index", columnList="product_id"))
public class ProductImage implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", columnDefinition = "INT not null ")
    private int imageId;

    //商品id
    @Column(name = "product_id", columnDefinition = "INT not null ")
    private int productId;

    //图片链接
    @Column(name = "pic", columnDefinition = "VARCHAR(512) not null default '' ")
    private String pic;

    //排序
    @Column(name = "sort", columnDefinition = "INT not null  default 0 ")
    private int sort;

    //是否删除
    @Column(name = "deleted", columnDefinition = "INT not null  default 0 ")
    private int deleted;

    //添加时间
    @Column(name = "create_at", columnDefinition = "datetime not null ")
    private Date createAt;

}
