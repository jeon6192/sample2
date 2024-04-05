package com.taekwang.tcast.util;

import org.springframework.jdbc.support.JdbcUtils;

import java.util.LinkedHashMap;

public class CamelHashMap extends LinkedHashMap {
    @Override
    public Object put(Object key, Object value) {
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String) key), value);
    }

    public Object put(Object key, Object value, boolean isActive) {
        if (isActive) {
            return put(key, value);
        } else {
            return super.put(key, value);
        }
    }
}
