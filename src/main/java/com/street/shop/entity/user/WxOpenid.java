package com.street.shop.entity.user;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户openid
 */

@Data
@Entity
@Table(name = "wx_openid",
        uniqueConstraints={@UniqueConstraint(columnNames={"user_id", "type"})},
        indexes=@Index(name="wx_openid_index", columnList="user_id"))
public class WxOpenid implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT not null ")
    private int id;

    //用户id
    @Column(name = "user_id", columnDefinition = "INT not null default 0 ")
    private int userId;

    //类型(用户、商家、配送)
    @Column(name = "type", columnDefinition = "INT not null default 1 ")
    private int type;

    //openid
    @Column(name = "open_id", columnDefinition = "VARCHAR(128) not null default '' ")
    private String openId;

    //时间
    @Column(name = "create_time", columnDefinition = "datetime not null ")
    private Date createTime;

}
