package com.jagsii.springxx.common.oss;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Getter;

import java.io.InputStream;
import java.net.URL;

public class QiniuOSSProvider implements OSSProvider {
    private static final String DEFAULT_REGION = "z0";
    @Getter
    private final OSSProperties ossProperties;
    @Getter
    private final Auth auth;
    @Getter
    private final BucketManager bucketManager;
    @Getter
    private final UploadManager uploadManager;
    private final String region;

    public QiniuOSSProvider(OSSProperties ossProperties) {
        this.ossProperties = ossProperties;
        auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        region = ossProperties.getProps() == null ? DEFAULT_REGION : (String) ossProperties.getProps().getOrDefault("region", DEFAULT_REGION);
        Configuration cfg = new Configuration();
        bucketManager = new BucketManager(auth, cfg);
        uploadManager = new UploadManager(cfg);
    }

    private String fallbackToDefault(String bucket) {
        return bucket == null ? ossProperties.getBucket() : bucket;
    }

    @Override
    public void createBucket(String bucket) {
        try {
            bucketManager.createBucket(bucket, region);
        } catch (Exception e) {
            throw new OSSException("Failed to create bucket:" + bucket, e);
        }
    }

    @Override
    public void deleteBucket(String bucket) {
        try {
            bucketManager.deleteBucket(bucket);
        } catch (Exception e) {
            throw new OSSException("Failed to delete bucket:" + bucket, e);
        }
    }

    @Override
    public void putFile(String bucket, String key, InputStream inputStream, boolean overwrite) {
        bucket = fallbackToDefault(bucket);
        try {
            uploadManager.put(inputStream, key, auth.uploadToken(bucket, overwrite ? key : null), null, null);
        } catch (Exception e) {
            throw new OSSException("Failed to upload file, bucket:" + bucket + ", key:" + key, e);
        }
    }

    @Override
    public InputStream downloadFile(String bucket, String key) {
        bucket = fallbackToDefault(bucket);
        try {
            String url = getSignedUrl(bucket, key, 3600);
            return new URL(url).openStream();
        } catch (Exception e) {
            throw new OSSException("Failed to download file, bucket:" + bucket + ", key:" + key, e);
        }
    }

    @Override
    public void deleteFile(String bucket, String key) {
        bucket = fallbackToDefault(bucket);
        try {
            bucketManager.delete(bucket, key);
        } catch (Exception e) {
            throw new OSSException("Failed to delete file, bucket:" + bucket + ", key:" + key, e);
        }
    }

    @Override
    public String getUrl(String bucket, String key) {
        return ossProperties.getEndpoint() + "/" + key;
    }

    @Override
    public String getSignedUrl(String bucket, String key, long expiresInSeconds) {
        return auth.privateDownloadUrl(getUrl(bucket, key), expiresInSeconds);
    }
}
