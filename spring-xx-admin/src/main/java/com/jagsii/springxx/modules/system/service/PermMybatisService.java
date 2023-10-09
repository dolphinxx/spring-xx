package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseMybatisService;
import com.jagsii.springxx.modules.system.entity.Perm;
import com.jagsii.springxx.modules.system.mapper.PermMapper;
import org.springframework.stereotype.Service;

@Service
public class PermMybatisService extends BaseMybatisService<Perm, Long, PermMapper> implements PermService {
}
