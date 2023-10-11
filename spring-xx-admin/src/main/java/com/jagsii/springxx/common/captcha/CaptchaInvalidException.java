package com.jagsii.springxx.common.captcha;

import com.jagsii.springxx.common.utils.ClientException;

public class CaptchaInvalidException extends ClientException {
    public CaptchaInvalidException() {
        this("Invalid captcha.");
    }

    public CaptchaInvalidException(String message) {
        super(message);
    }
}
