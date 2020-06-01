package com.street.shop.controller;


import com.google.gson.Gson;
import com.street.shop.pojo.Unit;
import com.street.shop.pojo.UnitSpec;
import com.street.shop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "/addProduct", method = RequestMethod.GET)
    @ResponseBody
    public String addProduct() {
        String result = "";
        try {
            String name = "testName";
            int shopId = 1;
            String uniqueCode = "435112079129710592";
            int categoryId = 3;
            int bandId = 0;
            String logo = "";
            List<Unit> unitList = new ArrayList<>();
            Unit unit = new Unit();
            unit.setPrice(1000);
            unit.setOfflinePrice(3000);
            List<UnitSpec> unitSpecList = new ArrayList<>();
            UnitSpec unitSpec = new UnitSpec();
            unitSpec.setKey("型号");
            unitSpec.setValue("40, 41");
            unitSpecList.add(unitSpec);
            unitSpec = new UnitSpec();
            unitSpec.setKey("颜色");
            unitSpec.setValue("red, black");
            unitSpecList.add(unitSpec);
            unit.setUnitSpecList(unitSpecList);
            unit.setUniqueCode("");
            unitList.add(unit);
            List<String> keyWords = new ArrayList<>();
            keyWords.add("产品");
            keyWords.add("最新");
            result = productService.addProduct(name, shopId, uniqueCode, categoryId, bandId, logo, unitList, keyWords);
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }


}
