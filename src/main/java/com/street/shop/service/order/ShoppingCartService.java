package com.street.shop.service.order;

import com.street.shop.dao.order.ShoppingCartDao;
import com.street.shop.entity.order.ShoppingCart;
import com.street.shop.entity.product.Product;
import com.street.shop.entity.product.ProductUnit;
import com.street.shop.pojo.ConstDefine;
import com.street.shop.pojo.ProductStatus;
import com.street.shop.pojo.ProductUnitStatus;
import com.street.shop.service.product.ProductService;
import com.street.shop.service.product.ProductUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * 购物车
 */
@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private ProductUnitService productUnitService;

    @Autowired
    private ProductService productService;


    /**
     * 添加购物车
     *
     * @param userId        用户id
     * @param productUnitId 商品id
     * @param count         数量
     * @return
     */
    public String addShoppingCart(int userId, int productUnitId, int count) {
        String result = "";
        if (userId <= 0) {
            result = "人员id为空!";
            return result;
        }
        if (productUnitId <= 0) {
            result = "商品id为空!";
            return result;
        }
        if (count <= 0) {
            result = "商品数量为空!";
            return result;
        }
        ProductUnit productUnit = productUnitService.getProductUnitById(productUnitId);
        if (productUnit == null) {
            result = "未知的商品规格!";
            return result;
        }
        if (productUnit.getStatus() == ProductUnitStatus.SHORTAGE) {
            result = "商品缺货,不能添加!";
            return result;
        }
        //判断商品是否在线
        Product product = productService.getProductById(productUnit.getProductId());
        if (product == null) {
            result = "商品信息不存在!";
            return result;
        }
        if (product.getStatus() == ProductStatus.FORBIDDEN) {
            result = "该商品已被禁用!";
            return result;
        }
        if (product.getStatus() == ProductStatus.OFFLINE) {
            result = "该商品已下架!";
            return result;
        }
        int shopId = productUnit.getShopId();   //商家id
        try {
            //判断是否已经存在相同的商品
            //存在的情况下,直接更新数量
            ShoppingCart shoppingCart = getShoppingCart(userId, productUnitId);
            if (shoppingCart != null) {
                int oldCount = shoppingCart.getCount(); //原来的数量
                shoppingCart.setCount(oldCount + count);   //更新数量
            } else {
                shoppingCart = new ShoppingCart();
                shoppingCart.setUserId(userId);
                shoppingCart.setProductUnitId(productUnitId);
                shoppingCart.setCount(count);
                shoppingCart.setShopId(shopId);
                shoppingCart.setCreateAt(new Date());
            }
            shoppingCartDao.save(shoppingCart);
            result = ConstDefine.SUCCESS;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


    public ShoppingCart getShoppingCart(int userId, int productUnitId) {
        ShoppingCart shoppingCart = null;
        try {
            Specification<ShoppingCart> spec = (Specification<ShoppingCart>) (root, crite, cb) -> {
                List<Predicate> pr = new ArrayList<>();
                pr.add(cb.equal(root.get("userId").as(Integer.class), userId));
                pr.add(cb.equal(root.get("productUnitId").as(Integer.class), productUnitId));
                return cb.and(pr.toArray(new Predicate[pr.size()]));
            };
            Optional<ShoppingCart> shoppingCartOptional = shoppingCartDao.findOne(spec);
            if (shoppingCartOptional != null &&
                    shoppingCartOptional.isPresent()) {
                shoppingCart = shoppingCartOptional.get();
            }
        } catch (Exception e) {
        }
        return shoppingCart;
    }





}
