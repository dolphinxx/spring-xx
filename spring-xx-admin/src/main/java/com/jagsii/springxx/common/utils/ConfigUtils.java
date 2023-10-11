package com.jagsii.springxx.common.utils;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Locale;

public class ConfigUtils {
    public static AntPathRequestMatcher parseMatcher(String matcher) {
        int pos = matcher.indexOf(":");
        if (pos == -1) {
            return new AntPathRequestMatcher(matcher);
        }
        return new AntPathRequestMatcher(matcher.substring(pos + 1), matcher.substring(0, pos).toUpperCase(Locale.ENGLISH));
    }
}
