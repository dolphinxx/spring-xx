package com.jagsii.springxx.common.storage;

import com.jagsii.springxx.common.oss.OSSProvider;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class OSSStorageProvider implements StorageProvider {
    private final OSSProvider ossProvider;
    private final MessageSourceAccessor messages;

    public OSSStorageProvider(OSSProvider ossProvider, MessageSourceAccessor messages) {
        this.ossProvider = ossProvider;
        this.messages = messages;
    }

    @Override
    public String store(MultipartFile file, StorageKeyGenerator keyGenerator) {
        if (file.isEmpty()) {
            throw new StorageException(messages.getMessage("StorageService.emptyFile", "Failed to store empty file."));
        }
        String key = keyGenerator.generate(file.getName(), file.getOriginalFilename(), file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            ossProvider.putFile(null, key, inputStream, true);
        } catch (IOException e) {
            throw new StorageException(messages.getMessage("StorageService.storeFailed", "Failed to store the file."), e);
        }
        return key;
    }

    @Override
    public String buildUrl(String key) {
        return ossProvider.getSignedUrl(null, key, 7 * 24);
    }

    @Override
    public void remove(String key) {
        ossProvider.deleteFile(null, key);
    }
}
