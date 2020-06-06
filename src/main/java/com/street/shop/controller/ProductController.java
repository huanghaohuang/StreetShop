package com.street.shop.controller;


import com.street.shop.entity.product.Product;
import com.street.shop.pojo.ConstDefine;
import com.street.shop.pojo.ProductVO;
import com.street.shop.pojo.ResponseMessage;
import com.street.shop.pojo.Unit;
import com.street.shop.service.product.ProductService;
import com.street.shop.service.user.TokenService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "商品管理接口")
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private TokenService tokenService;


    @ApiOperation("添加新的商品")
    @PostMapping("/addProduct")
    public ResponseMessage addProduct(
            @ApiParam(value = "令牌")
            @RequestHeader(ConstDefine.JWT_AUTH_HEADER) String token,

            @ApiParam(value = "商品信息")
            @RequestBody ProductVO product) {
        ResponseMessage ack = new ResponseMessage();
        //根据令牌获取用户信息
        String userId = tokenService.getUserIdByToken(token);
        if (userId == null || userId.length() <= 0) {
            ack.setStatusCode(ConstDefine.GET_USER_ID_BY_TOKEN_FAIL_CODE);
            ack.setMessage("根据令牌获取用户id失败!");
            return ack;
        }
        //判断人员角色
        String name = product.getName();
        int shopId = product.getShopId();
        String uniqueCode = product.getUniqueCode();
        int categoryId = product.getCategoryId();
        int bandId = product.getBandId();
        String logo = product.getLogo();
        List<Unit> unitList = product.getUnits();
        List<String> keyWords = product.getKeyWords();
        String productId = "";
        String result = productService.addProduct(
                name, shopId, uniqueCode, categoryId, bandId, logo, unitList, keyWords);
        if (result != null && result.startsWith(ConstDefine.SUCCESS)) {
            productId = result.replace(ConstDefine.SUCCESS, "");
            ack.setStatusCode(ConstDefine.SUCCESS_CODE);
            ack.setMessage("添加成功!");
            Map<String, Object> data = new HashMap<>();
            data.put("productId", productId);
            ack.setData(data);
        } else {
            ack.setStatusCode(ConstDefine.ADD_PRODUCT_FAIL_CODE);
            ack.setMessage(result);
        }
        return ack;
    }


    /**
     * 添加商品规格
     * @param token
     * @param productId
     * @param unitList
     * @return
     */
    @ApiOperation("添加商品规格")
    @PostMapping("/addProductUnit/{productId}")
    public ResponseMessage addProductUnit(
            @ApiParam(value = "令牌")
            @RequestHeader(ConstDefine.JWT_AUTH_HEADER) String token,

            @ApiParam(value = "商品id")
            @PathVariable int productId,
            @ApiParam(value = "商品规格信息")
            @Valid
            @RequestBody List<Unit> unitList) {
        ResponseMessage ack = new ResponseMessage();
        //根据令牌获取用户信息
        String userId = tokenService.getUserIdByToken(token);
        if (userId == null || userId.length() <= 0) {
            ack.setStatusCode(ConstDefine.GET_USER_ID_BY_TOKEN_FAIL_CODE);
            ack.setMessage("根据令牌获取用户id失败!");
            return ack;
        }
        //判断人员角色
        int operatorUserId = 0;
        try{
            operatorUserId = Integer.parseInt(userId);
        }
        catch(Exception e){
        }
        //判断人员角色
        String result = productService.addProductUnitBatch(operatorUserId, productId, unitList);
        if (result != null && result.equals(ConstDefine.SUCCESS)) {
            ack.setStatusCode(ConstDefine.SUCCESS_CODE);
            ack.setMessage("操作成功!");
        } else {
            ack.setStatusCode(ConstDefine.ADD_PRODUCT_UNIT_FAIL_CODE);
            ack.setMessage(result);
        }
        return ack;
    }

    //修改商品基本信息
    @ApiOperation("修改商品基本信息")
    @PostMapping("/updateProduct")
    public ResponseMessage updateProduct(
            @ApiParam(value = "令牌")
            @RequestHeader(ConstDefine.JWT_AUTH_HEADER) String token,
            @ApiParam(value = "修改的信息")
            @RequestBody Map<String, Object> updateInfo) {
        ResponseMessage ack = new ResponseMessage();
        //根据令牌获取用户信息
        String userId = tokenService.getUserIdByToken(token);
        if (userId == null || userId.length() <= 0) {
            ack.setStatusCode(ConstDefine.GET_USER_ID_BY_TOKEN_FAIL_CODE);
            ack.setMessage("根据令牌获取用户id失败!");
            return ack;
        }
        //判断人员角色
        int operatorUserId = 0;
        try{
            operatorUserId = Integer.parseInt(userId);
        }
        catch(Exception e){
        }
        String result = productService.updateProduct(operatorUserId, updateInfo);
        if (result != null && result.equals(ConstDefine.SUCCESS)) {
            ack.setStatusCode(ConstDefine.SUCCESS_CODE);
            ack.setMessage("操作成功!");
        } else {
            ack.setStatusCode(ConstDefine.UPDATE_PRODUCT_FAIL_CODE);
            ack.setMessage(result);
        }
        return ack;
    }

    // 修改价格信息

    @ApiOperation("修改商品价格")
    @PostMapping("/modifyProductPrice")
    public ResponseMessage modifyProductPrice(
            @ApiParam(value = "令牌")
            @RequestHeader(ConstDefine.JWT_AUTH_HEADER) String token,
            @ApiParam(value = "商品id")
            @RequestParam("productId") int productId,
            @ApiParam(value = "线上价格")
            @RequestParam("price") int price,
            @ApiParam(value = "线下价格")
            @RequestParam("offlinePrice") int offlinePrice) {
        ResponseMessage ack = new ResponseMessage();
        //根据令牌获取用户信息
        String userId = tokenService.getUserIdByToken(token);
        if (userId == null || userId.length() <= 0) {
            ack.setStatusCode(ConstDefine.GET_USER_ID_BY_TOKEN_FAIL_CODE);
            ack.setMessage("根据令牌获取用户id失败!");
            return ack;
        }
        //判断人员角色
        int operatorUserId = 0;
        try{
            operatorUserId = Integer.parseInt(userId);
        }
        catch(Exception e){
        }
        String result = productService.modifyProductPrice(operatorUserId, productId, price, offlinePrice);
        if (result != null && result.equals(ConstDefine.SUCCESS)) {
            ack.setStatusCode(ConstDefine.SUCCESS_CODE);
            ack.setMessage("操作成功!");
        } else {
            ack.setStatusCode(ConstDefine.UPDATE_PRODUCT_FAIL_CODE);
            ack.setMessage(result);
        }
        return ack;
    }



    //修改规格信息








    //通过excel批量上传商品信息








    //获取规格详情






}
