package com.jagsii.springxx.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@ContextConfiguration(classes = {
        SpringTests.TestConfig.class,
})
@ImportAutoConfiguration(classes = {JacksonAutoConfiguration.class})
public abstract class SpringTests {
    @Autowired
    protected ObjectMapper objectMapper;

    @TestConfiguration
    @EnableConfigurationProperties
    public static class TestConfig {

    }

    @PropertySource(value = {"classpath:/application-test.yml"}, factory = YamlPropertySourceFactory.class)
    @Profile("test")
    @TestConfiguration
    public static class TestPropertiesConfig {

    }

    protected String loadClassData(String filename, Class<?> clazz) throws IOException {
        try (InputStream is = clazz.getResourceAsStream(clazz.getSimpleName() + File.separator + filename)) {
            byte[] buff = new byte[is.available()];
            is.read(buff);
            return new String(buff, StandardCharsets.UTF_8);
        }
    }
}