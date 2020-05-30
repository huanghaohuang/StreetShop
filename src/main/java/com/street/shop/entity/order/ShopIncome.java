package com.street.shop.entity.order;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 商家收入明细
 */


@Data
@Entity
@Table(name = "shop_income_record",
        uniqueConstraints={@UniqueConstraint(columnNames={"shop_id", "order_no"})},
        indexes=@Index(name="shop_income_record_index", columnList="shop_id, order_no"))
public class ShopIncome implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "income_id", columnDefinition = "BIGINT not null ")
    private long incomeId;

    //商户id
    @Column(name = "shop_id", columnDefinition = "INT not null default 0 ")
    private int shopId;

    //订单编号
    @Column(name = "order_no", columnDefinition = "VARCHAR(32) not null default ''  ")
    private String orderNo;

    //类型(用户退款或者商家取消需要扣减所有的收入)
    @Column(name = "type", columnDefinition = "INT not null default 0 ")
    private int type;

    //收入
    @Column(name = "amount", columnDefinition = "INT not null default 0 ")
    private int amount;

    //状态
    @Column(name = "status", columnDefinition = "INT not null default 0 ")
    private int status;

    //原因
    @Column(name = "reason", columnDefinition = "VARCHAR(256) ")
    private String reason;

    //添加时间
    @Column(name = "create_at", columnDefinition = "datetime not null ")
    private Date createAt;


}
