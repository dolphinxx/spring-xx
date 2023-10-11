package com.jagsii.springxx.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private RestResponseWriter restResponseWriter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        restResponseWriter.write(RestExceptionConverter.convert(authException), response);
    }
}