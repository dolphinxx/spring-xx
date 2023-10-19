package com.jagsii.springxx.common.security.access.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define a permission for a method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HasPerm {
    /**
     * The required permission, in domain:actions:targets format.
     */
    String value();
}
