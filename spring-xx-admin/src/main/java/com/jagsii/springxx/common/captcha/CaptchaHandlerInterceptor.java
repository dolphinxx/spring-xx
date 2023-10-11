package com.jagsii.springxx.common.captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Handle captcha for handlers annotated with {@link Captcha}.
 */
public class CaptchaHandlerInterceptor implements HandlerInterceptor {
    private final CaptchaProvider<?> captchaProvider;
    private final CaptchaRequirementManager captchaRequirementManager;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    public CaptchaHandlerInterceptor(CaptchaProvider<?> captchaProvider, CaptchaRequirementManager captchaRequirementManager) {
        this.captchaProvider = captchaProvider;
        this.captchaRequirementManager = captchaRequirementManager;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(Captcha.class)) {
            CaptchaCode captchaCode = captchaProvider.extract(request);
            if (captchaCode == null && !isCaptchaRequired(Objects.requireNonNull(((HandlerMethod) handler).getMethodAnnotation(Captcha.class)), request)) {
                return true;
            }
            if (!captchaProvider.verify(captchaCode)) {
                // TODO: i18n
                resolver.resolveException(request, response, handler, new CaptchaInvalidException());
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    public boolean isCaptchaRequired(Captcha anno, HttpServletRequest request) {
        if (anno.always()) {
            return true;
        }
        return captchaRequirementManager.isRequired(request, anno.value());
    }
}
