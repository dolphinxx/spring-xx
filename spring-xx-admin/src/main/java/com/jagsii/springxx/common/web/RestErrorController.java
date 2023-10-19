package com.jagsii.springxx.common.web;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Override restful handler to return unified model.
 */
@Controller
public class RestErrorController extends BasicErrorController {
    public RestErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
                               List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
    }

    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        Map<String, Object> actualBody = new HashMap<>();
        actualBody.put("status", status.value());
        actualBody.put("data", null);
        actualBody.put("msg", body.get("error"));
        return new ResponseEntity<>(actualBody, HttpStatus.OK);
    }
}
