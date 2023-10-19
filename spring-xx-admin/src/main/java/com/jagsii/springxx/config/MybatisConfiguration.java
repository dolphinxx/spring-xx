package com.jagsii.springxx.config;

import com.jagsii.springxx.common.utils.PlatformConstants;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "**.mapper")
public class MybatisConfiguration {
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer(PlatformProperties platformProperties) {
        return configuration -> {
            String schema = PlatformConstants.DEFAULT_SCHEMA;
            if (platformProperties.getDb() != null && StringUtils.isNotEmpty(platformProperties.getDb().getSchema())) {
                schema = platformProperties.getDb().getSchema();
            }
            configuration.getVariables().setProperty("schema", schema);
        };
    }
}
