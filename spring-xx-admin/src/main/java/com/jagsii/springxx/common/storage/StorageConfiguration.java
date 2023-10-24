package com.jagsii.springxx.common.storage;

import com.jagsii.springxx.common.oss.OSSProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;

import java.net.URI;
import java.nio.file.Paths;

@AutoConfiguration
public class StorageConfiguration {
    @Configuration
    @RequiredArgsConstructor
    @ConditionalOnExpression("${platform.storage.enabled:true} and '${platform.storage.provider}' == 'filesystem'")
    public static class FileSystemStorageConfiguration {
        private final MessageSourceAccessor messages;

        @Bean
        public StorageProvider storageProvider(@Value("${platform.storage.base-location}") String location, @Value("${platform.storage.base-uri}") String uri) {
            Assert.hasText(location, "`platform.storage.base-location` can not be empty.");
            Assert.hasText(uri, "`platform.storage.base-uri` can not be empty.");
            return new FileSystemStorageProvider(Paths.get(location), URI.create(uri), messages);
        }

        @Bean
        @ConditionalOnExpression("${platform.storage.dev-server:false}")
        public FileSystemStorageServer fileSystemStorageServer(@Value("${platform.storage.base-location}") String location, @Value("${platform.storage.base-uri}") String uri) {
            URI u = URI.create(uri);
            int port = u.getPort();
            if (port == -1) {
                port = 80;
            }
            FileSystemStorageServer server = new FileSystemStorageServer(port, u.getPath(), location);
            server.start();
            return server;
        }
    }

    @Configuration
    @RequiredArgsConstructor
    @ConditionalOnExpression("${platform.storage.enabled:true} and '${platform.storage.provider}' == 'oss'")
    public static class OSSStorageConfiguration {
        private final MessageSourceAccessor messages;
        private final OSSProvider ossProvider;

        @Bean
        public StorageProvider storageProvider() {
            return new OSSStorageProvider(ossProvider, messages);
        }
    }
}
