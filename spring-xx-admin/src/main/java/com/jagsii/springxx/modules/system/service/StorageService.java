package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseService;
import com.jagsii.springxx.modules.system.entity.Storage;

public interface StorageService extends BaseService<Storage, Long> {
    Storage findByKey(String key);
}
