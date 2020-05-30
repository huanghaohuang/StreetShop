package com.street.shop.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "role",
        uniqueConstraints={@UniqueConstraint(columnNames={"name_en"})})
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT not null ")
    private int id;

    @Column(name = "name_en", columnDefinition = "VARCHAR(32) not null default '' ")
    private String nameEn;

    @Column(name = "name_ch", columnDefinition = "VARCHAR(32) not null default '' ")
    private String nameCh;

    @Column(name = "description", columnDefinition = "VARCHAR(32) not null default '' ")
    private String description;

    @Column(name = "create_at", columnDefinition = "datetime not null ")
    private Date createAt;

}
