package com.jagsii.springxx.common.storage;

import com.jagsii.springxx.common.security.Principal;
import com.jagsii.springxx.common.security.SecurityUtils;
import com.jagsii.springxx.common.web.R;
import com.jagsii.springxx.modules.system.entity.Storage;
import com.jagsii.springxx.modules.system.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage")
public class StorageEndPoint {
    private final StorageProvider storageProvider;
    private final StorageService storageService;

    @PostMapping("/upload")
    public R<UploadedFile> upload(@RequestParam("file") MultipartFile file, Principal principal) {
        String key = storageProvider.store(file, StorageKeyGenerator.MONTH_PREFIXED_TIME_BASED_KEY_GENERATOR);
        // save storage info
        Storage storage = new Storage();
        storage.setKey(key);
        storage.setCreatorId(principal.getId());
        storageService.save(storage);
        return R.success(new UploadedFile(key, storageProvider.buildUrl(key)));
    }

    @PostMapping("/remove")
    public R<Boolean> remove(String key, Principal principal) {
        Storage storage = storageService.findByKey(key);
        if (storage == null) {
            return R.success(false);
        }
        // check whether the current user created the file or has storage:delete permission
        if (!Objects.equals(storage.getCreatorId(), principal.getId()) && !SecurityUtils.getInstance().hasPermission("storage:delete")) {
            return R.success(false);
        }
        storageProvider.remove(key);
        storageService.deleteById(storage.getId());
        return R.success(true);
    }
}
