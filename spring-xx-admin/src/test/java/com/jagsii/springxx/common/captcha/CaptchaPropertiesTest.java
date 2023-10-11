package com.jagsii.springxx.common.captcha;

import com.jagsii.springxx.test.SpringTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = {
        "platform.captcha.path-matches[0]=login? POST:/login",
        "platform.captcha.path-matches[1]=foo /foo",
})
@Import({CaptchaProperties.class})
class CaptchaPropertiesTest extends SpringTests {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CaptchaProperties captchaProperties;

    @Test
    void testProperties() {
        List<CaptchaProperties.PathCaptcha> matches = captchaProperties.getPathMatches();
        assertThat(matches).hasSize(2);
        assertThat(matches.get(0))
                .hasFieldOrPropertyWithValue("id", "login")
                .hasFieldOrPropertyWithValue("always", false)
                .hasFieldOrPropertyWithValue("matcher", new AntPathRequestMatcher("/login", "POST"));
        assertThat(matches.get(1))
                .hasFieldOrPropertyWithValue("id", "foo")
                .hasFieldOrPropertyWithValue("always", true)
                .hasFieldOrPropertyWithValue("matcher", new AntPathRequestMatcher("/foo"));
    }
}