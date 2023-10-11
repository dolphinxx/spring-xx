package com.jagsii.springxx.common.security;

import com.jagsii.springxx.common.web.R;
import com.jagsii.springxx.common.web.RestResponseWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private RestResponseWriter restResponseWriter;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        restResponseWriter.write(R.success(), response);
    }
}
