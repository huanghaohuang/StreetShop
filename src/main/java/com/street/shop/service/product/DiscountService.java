package com.street.shop.service.product;

import com.street.shop.dao.product.DiscountDao;
import com.street.shop.entity.product.Discount;
import com.street.shop.entity.product.Product;
import com.street.shop.pojo.ConstDefine;
import com.street.shop.pojo.DiscountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;


@Service
public class DiscountService {

    @Autowired
    private DiscountDao discountDao;

    @Autowired
    private ProductService productService;

    /**
     * 添加商品优惠
     */
    public String addProductDiscount(Discount discount) {
        String result = "";
        if (discount == null) {
            result = "优惠信息为空!";
            return result;
        }
        //商品id
        int productId = discount.getProductId();
        if (productId <= 0) {
            result = "商品id为空!";
            return result;
        }
        Date today = new Date();
        if (discount.getStartTime() == null) {
            discount.setStartTime(today);
        }
        if (discount.getEndTime() == null) {
            result = "优惠结束时间为空!";
            return result;
        }
        if (discount.getEndTime().before(discount.getStartTime())) {
            result = "优惠结束时间必须晚于开始时间!";
            return result;
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            result = "未知的商品Id!";
            return result;
        }
        if (DiscountType.DISCOUNT == discount.getType()) {
            if (discount.getValue() < 0 || discount.getValue() > 100) {
                result = "折扣取值在0-100之间!";
                return result;
            }

        } else if (discount.getType() == DiscountType.PRICE) {
            if (discount.getValue() < 0) {
                result = "一口价设计价格不能小于0!";
                return result;
            }
        }
        try {
            discountDao.save(discount);
            int discountId = discount.getId();
            product.setDiscountId(discountId);      //设置商品的优惠信息
            productService.addProduct(product);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    public Discount getDisCount(int productId) {
        Discount discount = null;
        Product product = productService.getProductById(productId);
        if (product == null) {
            return discount;
        }
        try {
            int discountId = product.getDiscountId();
            if (discountId > 0) {
                Optional<Discount> discountOptional = discountDao.findById(discountId);
                if (discountOptional != null &&
                        discountOptional.isPresent()) {
                    discount = discountOptional.get();
                }
            }
        } catch (Exception e) {
        }
        return discount;
    }

    // 取消商品优惠
    public String delDiscount(int productId) {
        String result = "";
        Product product = productService.getProductById(productId);
        if (product == null) {
            result = "商品信息不存在!";
            return result;
        }
        try {
            int discountId = 0;
            product.setDiscountId(discountId);
            productService.addProduct(product);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    /**
     * 获取折扣价格
     *
     * @param discount 折扣
     * @param price    原价
     * @return
     */
    public int getDiscoutPrice(Discount discount, int price) {
        int discountPrice = price;
        if (discount == null) {
            return discountPrice;
        }
        Date today = new Date();
        if (discount.getStartTime() != null &&
                discount.getEndTime() != null) {
            if (today.compareTo(discount.getStartTime()) < 0 ||
                    today.compareTo(discount.getEndTime()) > 0) {
                return discountPrice;
            }
        }
        if (discount.getType() == DiscountType.PRICE) {
            discountPrice = discount.getValue();
        } else {
            discountPrice = ((price * discount.getValue()) / 100);
        }
        return discountPrice;
    }



}
