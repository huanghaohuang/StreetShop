package com.street.shop.pojo;


import com.street.shop.entity.product.Discount;
import com.street.shop.entity.product.ProductUnit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品信息
 */

@Data
public class ProductInfoForAdmin {

    @ApiModelProperty("商品Id")
    private int productId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty("商品唯一编号")
    private String uniquecode;

    @ApiModelProperty("商品分类Id")
    private int categoryId;

    @ApiModelProperty("商品优惠信息")
    private Discount discount;

    @ApiModelProperty("商品缩略图")
    private String logo;

    @ApiModelProperty("商品状态  0为商家  1为下架  4为被管理员禁用 ")
    private int status;

    @ApiModelProperty("商品本月销量")
    private int salesThisMonth;

    @ApiModelProperty("商品不同规格列表")
    private List<ProductUnit> units;

    @ApiModelProperty("商品规格的key值列表")
    private List<String> unitKeys;

    @ApiModelProperty("商品创建时间")
    private Date createdAt;

    @ApiModelProperty("商品关键字列表 没有时为空列表")
    private List<String> keyWords;

    @ApiModelProperty("商品销量总计")
    private int salesTotal;

    @ApiModelProperty("商品禁用原因 当商品状态为被禁用时此项有值  否者为null")
    private String forbiddenReason;

    @ApiModelProperty("商品优惠类型  0为无  1为新品上架   2为畅销商品  3为断码")
    private int type;

}
