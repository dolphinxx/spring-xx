package com.jagsii.springxx;

import com.jagsii.springxx.common.web.RestExceptionHandler;
import com.jagsii.springxx.config.PlatformProperties;
import com.jagsii.springxx.config.SecurityConfiguration;
import com.jagsii.springxx.config.WebMvcConfiguration;
import com.jagsii.springxx.config.WebSecurityConfiguration;
import com.jagsii.springxx.test.SpringTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(useDefaultFilters = false)
@Import({
        SecurityConfiguration.class,
        WebMvcConfiguration.class,
        WebSecurityConfiguration.class,
        PlatformProperties.class,
        RestExceptionHandler.class,
        WebTests.WebTestsConfig.class,
})
public class WebTests extends SpringTests {
    @Autowired
    protected MockMvc mvc;

    @TestConfiguration
    public static class WebTestsConfig {
        @Bean
        public PersistentTokenRepository rememberMeTokenRepository() {
            return new InMemoryTokenRepositoryImpl();
        }
    }
}
