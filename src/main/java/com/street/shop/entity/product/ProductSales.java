package com.street.shop.entity.product;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "product_sales",
        indexes=@Index(name="product_sales_index", columnList="product_id, product_unit_id"))
public class ProductSales implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id", columnDefinition = "INT not null ")
    private int saleId;

    //产品规格id
    @Column(name = "product_unit_id", columnDefinition = "INT not null default 0 ")
    private int productUnitId;

    //产品id
    @Column(name = "product_id", columnDefinition = "INT not null  default 0 ")
    private int productId;

    //月份
    @Column(name = "month", columnDefinition = "VARCHAR(16) not null default '' ")
    private String month;

    //数量
    @Column(name = "num", columnDefinition = "INT not null default 0 ")
    private int num;

    //金额
    @Column(name = "amount", columnDefinition = "INT not null default 0 ")
    private int amount;

    @Column(name = "create_at", columnDefinition = "datetime not null ")
    private Date createAt;

}
