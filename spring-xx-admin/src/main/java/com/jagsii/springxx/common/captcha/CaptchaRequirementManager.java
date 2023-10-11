package com.jagsii.springxx.common.captcha;

import javax.servlet.http.HttpServletRequest;

/**
 * Manage the captcha requirement for an {@link HttpServletRequest}.
 *
 * @see SessionCaptchaRequirementManager
 */
public interface CaptchaRequirementManager {
    /**
     * Whether the captcha is required for the request.
     */
    boolean isRequired(HttpServletRequest request, String captchaId);

    /**
     * Update captcha requirement to required.
     */
    void updateRequired(HttpServletRequest request, String captchaId);

    /**
     * Clear the captcha requirement for the request.
     */
    void clearRequirement(HttpServletRequest request, String captchaId);
}
