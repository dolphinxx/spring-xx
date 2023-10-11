package com.jagsii.springxx.common.captcha;

import com.jagsii.springxx.common.utils.ConfigUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@ConfigurationProperties(prefix = "platform.captcha")
public class CaptchaProperties {
    @Getter
    @Setter
    private List<PathCaptcha> pathMatches;

    public static class PathCaptcha {
        @Getter
        private final AntPathRequestMatcher matcher;
        @Getter
        private final boolean always;
        @Getter
        private final String id;

        public PathCaptcha(String raw) {
            String[] tokens = raw.split(" ");
            if (tokens.length != 2) {
                throw new IllegalArgumentException("Invalid captcha config[" + raw + "], expected format {id}[?] [method:]path");
            }
            String token0 = tokens[0].trim();
            String token1 = tokens[1].trim();
            if (token0.endsWith("?")) {
                id = token0.substring(0, token0.length() - 1);
                always = false;
            } else {
                id = token0;
                always = true;
            }
            matcher = ConfigUtils.parseMatcher(token1);
        }
    }
}
