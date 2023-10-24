package com.jagsii.springxx.common.storage;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileSystemStorageProvider implements StorageProvider {
    private final MessageSourceAccessor messages;
    private final Path rootLocation;
    private final URI baseURI;

    public FileSystemStorageProvider(Path rootLocation, URI baseURI, MessageSourceAccessor messages) {
        this.messages = messages;
        this.rootLocation = rootLocation;
        this.baseURI = baseURI;
    }

    @Override
    public String store(MultipartFile file, StorageKeyGenerator keyGenerator) {
        if (file.isEmpty()) {
            throw new StorageException(messages.getMessage("StorageService.emptyFile", "Failed to store empty file."));
        }
        String key = keyGenerator.generate(file.getName(), file.getOriginalFilename(), file.getContentType());
        Path dest = this.rootLocation.resolve(key).normalize().toAbsolutePath();
        if (!dest.getParent().startsWith(this.rootLocation.toAbsolutePath())) {
            throw new StorageException(messages.getMessage("StorageService.illegalPath", "Cannot store file outside current directory."));
        }
        try (InputStream inputStream = file.getInputStream()) {
            if (Files.notExists(dest.getParent())) {
                Files.createDirectories(dest.getParent());
            }
            Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException(messages.getMessage("StorageService.storeFailed", "Failed to store the file."), e);
        }
        return key;
    }

    @Override
    public String buildUrl(String key) {
        return baseURI.resolve("/storage/preview/" + key).toString();
    }

    @Override
    public void remove(String key) {
        try {
            Path dest = this.rootLocation.resolve(key).normalize().toAbsolutePath();
            if (Files.exists(dest)) {
                Files.delete(dest);
            }
        } catch (IOException e) {
            throw new StorageException(messages.getMessage("StorageService.removeFailed", new Object[]{key}, "Failed to remove " + key), e);
        }
    }
}
