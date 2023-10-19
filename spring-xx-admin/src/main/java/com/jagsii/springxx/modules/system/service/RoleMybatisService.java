package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseMybatisService;
import com.jagsii.springxx.common.security.data.RoleAndPermRawData;
import com.jagsii.springxx.modules.system.entity.Role;
import com.jagsii.springxx.modules.system.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMybatisService extends BaseMybatisService<Role, Long, RoleMapper> implements RoleService {
    @Override
    public List<RoleAndPermRawData> findAllRolesAndPerms() {
        return getMapper().selectRolesAndPerms();
    }

    @Override
    public List<Long> findRoleIdsByUserId(Long userId) {
        return getMapper().selectRoleIdsByUserId(userId);
    }
}
