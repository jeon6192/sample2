package com.example.springbootsample.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ColumnConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        String convertedData;

        try {
            convertedData = AESUtil.encrypt(attribute);
        } catch (Exception ignored) {
            convertedData = null;
        }

        return convertedData;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        String convertedData;

        try {
            convertedData = AESUtil.decrypt(dbData);
        } catch (Exception ignored) {
            convertedData = null;
        }

        return convertedData;
    }
}
