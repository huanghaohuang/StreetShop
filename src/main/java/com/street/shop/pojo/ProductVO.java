package com.street.shop.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("新建商品")
@Data
public class ProductVO {
    @ApiModelProperty(value = "商品名称", required = true)
    private String name;
    @ApiModelProperty(value = "商品唯一编号")
    private String uniqueCode;
    @ApiModelProperty(value = "商户id", required = true)
    private int shopId;
    @ApiModelProperty(value = "分类id", required = true)
    private int categoryId;
    @ApiModelProperty(value = "品牌id")
    private int bandId;
    @ApiModelProperty(value = "商品图片url")
    private String logo;
    @ApiModelProperty(value = "商品规格列表",  required = true)
    private List<Unit> units;
    @ApiModelProperty(value = "商品搜索关键字 方便用户搜索 最多10个 单个关键字应控制在6个字内 可以不填写")
    private List<String> keyWords;

}
