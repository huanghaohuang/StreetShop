package com.street.shop.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Unit {
    //线上价格
    private int price;
    //规格详情
    private List<UnitSpec> unitSpecList;
    //线下价格
    private int offlinePrice;
    private String uniqueCode;  //唯一编号
}
