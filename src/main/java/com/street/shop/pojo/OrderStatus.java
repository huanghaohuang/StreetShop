package com.street.shop.pojo;

/**
 * 订单状态
 */

public class OrderStatus {
    //等待支付
    public final static int WAITING_PAY = 0;
    // 用户取消 未支付取消
    public final static int USER_CANCEL_NO_PAY = 500;
    //订单已支付 等待商家接单
    public final static int PAYED = 1000;
    //用户取消
    public final static int USER_CANCEL_AFTER_PAY = 1200;
    //商家取消
    public final static int SHOP_CANCEL = 1300;
    //商家已接单
    public final static int SHOP_ACCEPTED = 2000;
    //商品已送出
    public final static int SENED = 3000;
    //用户已收货
    public final static int USER_ACCEPTED = 4000;


}
