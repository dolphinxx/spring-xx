package com.jagsii.springxx.common.security;

import com.jagsii.springxx.common.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        messageConverter.write(R.success(), MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
    }
}
