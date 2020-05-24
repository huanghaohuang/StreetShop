package com.street.shop.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品类别
 */


@Data
@Entity
@Table(name = "category")
public class Category implements Serializable {


    //主键
    @Id
    @Column(name = "id", columnDefinition = "int not null ")
    private int id;

    //名字
    @Column(name = "name", columnDefinition = "VARCHAR(50) not null default '' ")
    private String name;

    //父id
    @Column(name = "parent_id", columnDefinition = "int not null default 0 ")
    private int parentId;

    //图片
    @Column(name = "img", columnDefinition = "VARCHAR(255) ")
    private String img;

    //删除标志
    @Column(name = "deleted", columnDefinition = "int not null default 0  ")
    private int deleted;

    //修改时间
    @Column(name = "updated_time", columnDefinition = "datetime ")
    private Date updatedTime;

    //创建时间
    @Column(name = "created_time", columnDefinition = "datetime not null ")
    private Date createdTime;

}
