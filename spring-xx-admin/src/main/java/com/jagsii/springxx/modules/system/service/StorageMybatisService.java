package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseMybatisService;
import com.jagsii.springxx.modules.system.entity.Storage;
import com.jagsii.springxx.modules.system.mapper.StorageMapper;
import com.jagsii.springxx.mybatis.Criterion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageMybatisService extends BaseMybatisService<Storage, Long, StorageMapper> implements StorageService {
    @Override
    public Storage findByKey(String key) {
        List<Storage> list = getMapper().selectByCriteria(Criterion.lambdaQuery(Storage.class).eq(Storage::getKey, key).limit(1));
        return list.isEmpty() ? null : list.get(0);
    }
}
