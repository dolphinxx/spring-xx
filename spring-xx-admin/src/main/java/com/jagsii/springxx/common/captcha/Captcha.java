package com.jagsii.springxx.common.captcha;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the handler method requires captcha validation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Captcha {
    /**
     * Identity of the required captcha.
     */
    String value();

    /**
     * Whether a captcha is always required.
     */
    boolean always() default true;
}
