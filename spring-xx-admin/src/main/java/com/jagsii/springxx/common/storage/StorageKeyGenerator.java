package com.jagsii.springxx.common.storage;

import com.jagsii.springxx.common.id.TimeBasedId;
import com.jagsii.springxx.common.utils.DateUtils;
import com.jagsii.springxx.common.utils.FileNameInfo;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Locale;

@FunctionalInterface
public interface StorageKeyGenerator {
    StorageKeyGenerator MONTH_PREFIXED_TIME_BASED_KEY_GENERATOR = (name, originFilename, contentType) -> {
        String prefix = DateUtils.COMPACT_MONTH_FORMATTER.format(LocalDateTime.now());
        String basename = TimeBasedId.INSTANCE.nextId();
        return prefix + "/" + basename + new FileNameInfo(originFilename).getExt().toLowerCase(Locale.ENGLISH);
    };

    /**
     * @param name             the name of the parameter in the multipart form.
     * @param originalFilename the original filename in the client's filesystem.
     * @param contentType      the content type of the file.
     * @return the generated key
     */
    String generate(String name, @Nullable String originalFilename, @Nullable String contentType);
}
