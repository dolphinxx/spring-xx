package com.jagsii.springxx.common.oss;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfiguration
public class OSSConfiguration {
    @Configuration
    @RequiredArgsConstructor
    @EnableConfigurationProperties(OSSProperties.class)
    @ConditionalOnExpression("${platform.oss.enabled:true} and '${platform.oss.provider}' == 'aliyun'")
    public static class OSSVendorConfiguration {
        private final OSSProperties ossProperties;

        @Bean
        public OSSProvider ossProvider() {
            return new AliyunOSSProvider(ossProperties);
        }
    }

    @Configuration
    @RequiredArgsConstructor
    @EnableConfigurationProperties(OSSProperties.class)
    @ConditionalOnExpression("${platform.oss.enabled:true} and '${platform.oss.provider}' == 'qiniu'")
    public static class QiniuOSSConfiguration {
        private final OSSProperties ossProperties;

        @Bean
        public OSSProvider ossProvider() {
            return new QiniuOSSProvider(ossProperties);
        }
    }
}
