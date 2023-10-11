package com.jagsii.springxx.common.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class SessionCaptchaRequirementManager implements CaptchaRequirementManager {
    private static final String CAPTCHA_REQUIREMENT_FOR_SESSION_KEY_PREFIX = "CAPTCHA_REQUIRED_";

    @Override
    public boolean isRequired(HttpServletRequest request, String captchaId) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        return Objects.equals(session.getAttribute(CAPTCHA_REQUIREMENT_FOR_SESSION_KEY_PREFIX + captchaId), true);
    }

    @Override
    public void updateRequired(HttpServletRequest request, String captchaId) {
        HttpSession session = request.getSession(true);
        session.setAttribute(CAPTCHA_REQUIREMENT_FOR_SESSION_KEY_PREFIX + captchaId, true);
    }

    @Override
    public void clearRequirement(HttpServletRequest request, String captchaId) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(CAPTCHA_REQUIREMENT_FOR_SESSION_KEY_PREFIX + captchaId);
    }
}
