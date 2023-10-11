package com.jagsii.springxx.common.security;

import com.jagsii.springxx.common.web.RestExceptionConverter;
import com.jagsii.springxx.common.web.RestResponseWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    private RestResponseWriter restResponseWriter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        restResponseWriter.write(RestExceptionConverter.convert(accessDeniedException), response);
    }
}
