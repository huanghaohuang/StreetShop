package com.street.shop.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("商品规格")
@Data
public class Unit {
    //线上价格
    @ApiModelProperty(value = "线上价格", required = true)
    private int price;
    //线下价格
    @ApiModelProperty(value = "线下价格", required = true)
    private int offlinePrice;
    @ApiModelProperty(value = "唯一编号, 最长32位")
    private String uniqueCode;  //唯一编号
    //规格详情
    @ApiModelProperty(value = "规格详情列表")
    private List<UnitSpec> unitSpecList;
}
