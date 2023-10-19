package com.jagsii.springxx.common.web;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.springframework.http.HttpStatus;

public class RR extends R<String> {
    public RR(String data) {
        super(HttpStatus.OK.value(), data, null);
    }

    @JsonRawValue
    public String getData() {
        return data;
    }
}
