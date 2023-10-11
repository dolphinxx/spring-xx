package com.jagsii.springxx.common.captcha;

import com.jagsii.springxx.common.web.RestExceptionConverter;
import com.jagsii.springxx.common.web.RestResponseWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Handle captcha before all handler methods.
 */
public class CaptchaFilter extends OncePerRequestFilter {
    private final CaptchaProvider<?> captchaProvider;
    private final CaptchaRequirementManager captchaRequirementManager;
    private final List<CaptchaProperties.PathCaptcha> matchers;
    @Autowired
    private RestResponseWriter restResponseWriter;

    public CaptchaFilter(CaptchaProvider<?> captchaProvider, CaptchaRequirementManager captchaRequirementManager, CaptchaProperties properties) {
        this.captchaProvider = captchaProvider;
        this.captchaRequirementManager = captchaRequirementManager;
        this.matchers = properties.getPathMatches() == null ? Collections.emptyList() : properties.getPathMatches();
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (matchers.size() > 0) {
            for (CaptchaProperties.PathCaptcha matcher : matchers) {
                if (!matcher.getMatcher().matches(request)) {
                    continue;
                }
                if (matcher.isAlways() || captchaRequirementManager.isRequired(request, matcher.getId())) {
                    CaptchaCode captchaCode = captchaProvider.extract(request);
                    if (captchaCode == null || !captchaProvider.verify(captchaCode)) {
                        // TODO: i18n
                        restResponseWriter.write(RestExceptionConverter.convertClientException(new CaptchaInvalidException()), response);
                        return;
                    }
                    // Clear the captcha requirement flag after successful validation.
                    captchaRequirementManager.clearRequirement(request, matcher.getId());
                }
                break;
            }
        }

        filterChain.doFilter(request, response);
    }
}
