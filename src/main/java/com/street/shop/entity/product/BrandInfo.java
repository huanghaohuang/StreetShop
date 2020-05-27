package com.street.shop.entity.product;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 品牌
 */

@Data
@Entity
@Table(name = "brand_info")
public class BrandInfo implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id", columnDefinition = "INT not null ")
    private int brandId;

    //名字
    @Column(name = "brand_name", columnDefinition = "VARCHAR(64) not null default '' ")
    private String brandName;

    //图片
    @Column(name = "brand_logo", columnDefinition = "VARCHAR(512) ")
    private String brandLogo;

    //序号
    @Column(name = "brand_order", columnDefinition = "INT not null default 0 ")
    private int brandOrder;

    //状态
    @Column(name = "brand_status", columnDefinition = "INT not null default 0 ")
    private int brandStatus;


}
