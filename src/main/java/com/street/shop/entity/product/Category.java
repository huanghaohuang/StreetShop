package com.street.shop.entity.product;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品类别
 */


@Data
@Entity
@Table(name = "category",
        uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
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
    @Column(name = "img", columnDefinition = "VARCHAR(512) ")
    private String img;

    //顺序
    @Column(name = "order_flag", columnDefinition = "int not null default 0  ")
    private int orderFlag;

    //删除标志
    @Column(name = "deleted", columnDefinition = "int not null default 0  ")
    private int deleted;

    //规格列表
    @Transient
    private List<String> specs;

    //创建时间
    @Column(name = "created_at", columnDefinition = "datetime not null ")
    private Date createdAt;

}
