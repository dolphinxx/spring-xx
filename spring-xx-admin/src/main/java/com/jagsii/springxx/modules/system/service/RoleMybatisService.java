package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseMybatisService;
import com.jagsii.springxx.modules.system.entity.Role;
import com.jagsii.springxx.modules.system.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleMybatisService extends BaseMybatisService<Role, Long, RoleMapper> implements RoleService {
    @Override
    public List<Map<String, Object>> findAllRolesAndPerms() {
        return getMapper().selectRolesAndPerms();
    }

    @Override
    public List<Long> findRoleIdsByUserId(Long userId) {
        return getMapper().selectRoleIdsByUserId(userId);
    }
}
