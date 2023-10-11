package com.jagsii.springxx.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jagsii.springxx.common.captcha.*;
import com.jagsii.springxx.common.captcha.ta.TianAiCaptchaProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaConfiguration {
    @Bean
    public CaptchaProperties captchaProperties() {
        return new CaptchaProperties();
    }

    @Bean
    public CaptchaProvider<?> captchaProvider(ObjectMapper objectMapper) {
        return new TianAiCaptchaProvider(objectMapper);
    }

    @Bean
    public CaptchaRequirementManager captchaRequirementManager() {
        return new SessionCaptchaRequirementManager();
    }

    @Bean
    public CaptchaFilter captchaFilter(CaptchaProvider<?> captchaProvider) {
        return new CaptchaFilter(captchaProvider, captchaRequirementManager(), captchaProperties());
    }

    @Bean
    public CaptchaHandlerInterceptor captchaHandlerInterceptor(CaptchaProvider<?> captchaProvider) {
        return new CaptchaHandlerInterceptor(captchaProvider, captchaRequirementManager());
    }
}
