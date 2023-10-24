package com.jagsii.springxx.common.oss;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.Getter;

import java.io.InputStream;
import java.util.Date;

public class AliyunOSSProvider implements OSSProvider {
    @Getter
    private final OSS client;
    @Getter
    private final OSSProperties ossProperties;

    public AliyunOSSProvider(OSSProperties ossProperties) {
        this.ossProperties = ossProperties;
        client = OSSClientBuilder.create().endpoint(ossProperties.getEndpoint()).credentialsProvider(new DefaultCredentialProvider(ossProperties.getAccessKey(), ossProperties.getSecretKey())).build();
    }

    private String fallbackToDefault(String bucket) {
        return bucket == null ? ossProperties.getBucket() : bucket;
    }

    @Override
    public void createBucket(String bucket) {
        try {
            client.createBucket(bucket);
        } catch (Exception e) {
            throw new OSSException("Failed to create bucket:" + bucket, e);
        }
    }

    @Override
    public void deleteBucket(String bucket) {
        try {
            client.deleteBucket(bucket);
        } catch (Exception e) {
            throw new OSSException("Failed to delete bucket:" + bucket, e);
        }

    }

    @Override
    public void putFile(String bucket, String key, InputStream inputStream, boolean overwrite) {
        bucket = fallbackToDefault(bucket);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader("x-oss-forbid-overwrite", !overwrite);
            client.putObject(bucket, key, inputStream, metadata);
        } catch (Exception e) {
            throw new OSSException("Failed to upload file, bucket:" + bucket + ", key:" + key, e);
        }
    }

    @Override
    public InputStream downloadFile(String bucket, String key) {
        bucket = fallbackToDefault(bucket);
        try {
            OSSObject obj = client.getObject(bucket, key);
            return obj.getObjectContent();
        } catch (Exception e) {
            throw new OSSException("Failed to download file, bucket:" + bucket + ", key:" + key, e);
        }
    }

    @Override
    public void deleteFile(String bucket, String key) {
        bucket = fallbackToDefault(bucket);
        try {
            client.deleteObject(bucket, key);
        } catch (Exception e) {
            throw new OSSException("Failed to delete file, bucket:" + bucket + ", key:" + key, e);
        }
    }

    @Override
    public String getUrl(String bucket, String key) {
        bucket = fallbackToDefault(bucket);
        int pos = ossProperties.getEndpoint().indexOf("//");
        return ossProperties.getEndpoint().substring(0, pos + 2) + bucket + "." + ossProperties.getEndpoint().substring(pos + 2) + "/" + key;
    }

    @Override
    public String getSignedUrl(String bucket, String key, long expiresInSeconds) {
        bucket = fallbackToDefault(bucket);
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key, HttpMethod.GET);
            request.setExpiration(new Date(System.currentTimeMillis() + expiresInSeconds * 1000));
            return client.generatePresignedUrl(request).toString();
        } catch (Exception e) {
            throw new OSSException("Failed to get signed url, bucket:" + bucket + ", key:" + key, e);
        }
    }
}
