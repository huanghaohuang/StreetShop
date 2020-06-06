package com.street.shop.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


//用户角色表

@Data
@Entity
@Table(name = "user_role",
        uniqueConstraints={@UniqueConstraint(columnNames={"user_id", "role_id"})})
public class UserRole implements Serializable {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT not null ")
    private int id;

    //用户id
    @Column(name = "user_id", columnDefinition = "int not null default 0 ")
    private int userId;

    //角色id
    @Column(name = "role_id", columnDefinition = "int not null default 0 ")
    private int roleId;

    //创建时间
    @Column(name = "create_at", columnDefinition = "datetime not null ")
    private Date createAt;


}
