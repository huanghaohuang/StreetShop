package com.street.shop.entity.product;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "category_spec")
public class CategorySpec implements Serializable {

    //主键
    @Id
    @Column(name = "id", columnDefinition = "int not null ")
    private int id;

    //类别id
    @Column(name = "category_id", columnDefinition = "int not null default 0 ")
    private int categoryId;

    //规格
    @Column(name = "spec", columnDefinition = "VARCHAR(64) not null default '' ")
    private String spec;

    //创建时间
    @Column(name = "created_time", columnDefinition = "datetime not null ")
    private Date createdTime;

}
