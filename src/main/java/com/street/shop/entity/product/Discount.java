package com.street.shop.entity.product;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "discount",
        indexes=@Index(name="discount_index", columnList="product_id"))
public class Discount implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT not null ")
    private int id;

    //商品id
    @Column(name = "product_id", columnDefinition = "INT not null default 0 ")
    private int productId;

    //优惠类型  0 为折扣 1为一口价
    @Column(name = "type", columnDefinition = "INT not null default 0 ")
    private int type;

    //优惠类型为折扣时value为打折0到1之间, 优惠类型为一口价时value为设定的的价格
    @Column(name = "value", columnDefinition = "INT not null default 0 ")
    private int value;

    //优惠开始时间
    @Column(name = "start_time", columnDefinition = "datetime ")
    private Date startTime;

    //优惠结束时间
    @Column(name = "end_time", columnDefinition = "datetime ")
    private Date endTime;

}
