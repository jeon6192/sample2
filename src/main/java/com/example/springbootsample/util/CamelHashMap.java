package com.example.springbootsample.util;

import org.springframework.jdbc.support.JdbcUtils;

import java.util.LinkedHashMap;

public class CamelHashMap extends LinkedHashMap {
    @Override
    public Object put(Object key, Object value) {
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String) key), value);
    }
}
