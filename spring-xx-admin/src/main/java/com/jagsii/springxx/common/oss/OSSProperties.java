package com.jagsii.springxx.common.oss;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "platform.oss")
public class OSSProperties {
    private boolean enabled;
    private String provider;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private Map<String, Object> props;
}
