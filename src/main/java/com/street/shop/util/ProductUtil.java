package com.street.shop.util;

import com.street.shop.pojo.UnitSpec;
import org.springframework.util.StringUtils;

import java.util.*;

public class ProductUtil {


    public static Map<String, String> makeUnitMap(String unitStr) {
        if (StringUtils.isEmpty(unitStr)) {
            return Collections.emptyMap();
        }
        String[] list = unitStr.split(",");

        if (list.length == 0) {
            return Collections.emptyMap();
        }
        Map<String, String> map = new HashMap<>(list.length);
        try {
            for (String in : list) {
                String[] kv = in.split(":");
                String key = kv[0];
                String value = kv[1];
                map.put(key, value);
            }
        } catch (Exception e) {
        }
        return map;
    }


    public static List<UnitSpec> getUnitSpecList(String unitStr) {
        if (StringUtils.isEmpty(unitStr)) {
            return Collections.emptyList();
        }
        String[] list = unitStr.split(",");
        if (list.length == 0) {
            return Collections.emptyList();
        }

        List<UnitSpec> unitSpecList = new ArrayList<>(list.length);
        try {
            for (String in : list) {
                String[] kv = in.split(":");
                UnitSpec unitSpec = new UnitSpec();
                unitSpec.setKey(kv[0]);
                unitSpec.setValue(kv[1]);
                unitSpecList.add(unitSpec);
            }
        } catch (Exception e) {
        }
        return unitSpecList;
    }


    public static String makeSpecStr(List<UnitSpec> unitSpecList) {
        if (unitSpecList == null || unitSpecList.size() <= 0) {
            return "";
        }
        TreeMap<String, String> map = new TreeMap<>();
        for (UnitSpec vo : unitSpecList) {
            map.put(vo.getKey(), vo.getValue());
        }
        StringBuilder builder = new StringBuilder();
        map.forEach((k, v) -> {
            builder.append(k);
            builder.append(":");
            builder.append(v);
            builder.append(",");
        });
        return builder.toString();
    }

}
