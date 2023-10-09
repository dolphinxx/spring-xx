package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseService;
import com.jagsii.springxx.modules.system.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role, Long> {
    List<Long> findRoleIdsByUserId(Long userId);

    List<Map<String, Object>> findAllRolesAndPerms();
}
