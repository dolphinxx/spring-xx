package com.jagsii.springxx;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@ContextConfiguration(classes = {
        SpringTests.TestConfig.class,
})
public abstract class SpringTests {
    @TestConfiguration
    @EnableConfigurationProperties
    public static class TestConfig {

    }

    @PropertySource(value = {"classpath:/application-test.yml"}, factory = YamlPropertySourceFactory.class)
    @Profile("test")
    @TestConfiguration
    public static class TestPropertiesConfig {

    }
}
