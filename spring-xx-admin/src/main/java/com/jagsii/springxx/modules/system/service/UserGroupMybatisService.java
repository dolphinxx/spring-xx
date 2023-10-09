package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseMybatisService;
import com.jagsii.springxx.modules.system.entity.UserGroup;
import com.jagsii.springxx.modules.system.mapper.UserGroupMapper;
import org.springframework.stereotype.Service;

@Service
public class UserGroupMybatisService extends BaseMybatisService<UserGroup, Long, UserGroupMapper> implements UserGroupService {
}
