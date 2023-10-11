package com.jagsii.springxx.common.captcha;

import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

public interface CaptchaProvider<T> {
    T generate(String key);

    boolean verify(CaptchaCode value);

    @Nullable
    CaptchaCode extract(HttpServletRequest request);

    @Nullable
    String verifyBehavior(HttpServletRequest request);
}
