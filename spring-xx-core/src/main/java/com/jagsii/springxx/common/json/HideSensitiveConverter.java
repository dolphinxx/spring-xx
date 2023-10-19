package com.jagsii.springxx.common.json;

import com.fasterxml.jackson.databind.util.StdConverter;

public class HideSensitiveConverter extends StdConverter<String, String> {
    @Override
    public String convert(String value) {
        return "******";
    }
}
