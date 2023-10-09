package com.jagsii.springxx.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "platform")
public class PlatformProperties {
    private Security security;
    private Database db;

    @Getter
    @Setter
    public static class Security {
        private List<String> permitAll;
    }

    @Getter
    @Setter
    public static class Database {
        private String schema;
    }
}
