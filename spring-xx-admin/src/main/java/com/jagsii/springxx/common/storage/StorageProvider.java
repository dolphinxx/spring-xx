package com.jagsii.springxx.common.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageProvider {
    String store(MultipartFile file, StorageKeyGenerator keyGenerator);

    String buildUrl(String key);

    void remove(String key);
}
