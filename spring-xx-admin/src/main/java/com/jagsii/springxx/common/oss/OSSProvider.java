package com.jagsii.springxx.common.oss;

import org.springframework.lang.Nullable;

import java.io.InputStream;

public interface OSSProvider {
    void createBucket(String bucket);

    void deleteBucket(String bucket);

    void putFile(@Nullable String bucket, String key, InputStream inputStream, boolean overwrite);

    InputStream downloadFile(@Nullable String bucket, String key);

    void deleteFile(@Nullable String bucket, String key);

    String getUrl(@Nullable String bucket, String key);

    String getSignedUrl(@Nullable String bucket, String key, long expiresInSeconds);
}
