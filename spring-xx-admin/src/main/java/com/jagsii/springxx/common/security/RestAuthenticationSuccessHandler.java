package com.jagsii.springxx.common.security;

import com.jagsii.springxx.common.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> authInfo = new HashMap<>();
        authInfo.put("principal", authentication.getPrincipal());
        messageConverter.write(R.success(authInfo), MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
    }
}