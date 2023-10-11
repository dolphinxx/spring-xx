package com.jagsii.springxx.common.web;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * Write R&lt;?&gt; object to HttpServletResponse.
 */
public class RestResponseWriter {
    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    @SneakyThrows
    public void write(R<?> body, HttpServletResponse response) {
        messageConverter.write(body, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
    }
}
