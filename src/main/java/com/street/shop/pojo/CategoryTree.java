package com.street.shop.pojo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTree {
    private Integer id;
    private String name;
    private String img;
    private List<String> specs;
    private List<CategoryTree> children;
}
