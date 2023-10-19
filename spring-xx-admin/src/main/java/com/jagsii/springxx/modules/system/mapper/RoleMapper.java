package com.jagsii.springxx.modules.system.mapper;

import com.jagsii.springxx.common.base.BaseMapper;
import com.jagsii.springxx.common.security.data.RoleAndPermRawData;
import com.jagsii.springxx.modules.system.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role, Long> {

    @Select("SELECT r.id, r.parent_id AS parentId, r.value AS roleValue, p.value AS permValue FROM role r LEFT JOIN role_x_perm rxp on r.id = rxp.role_id LEFT JOIN perm p on rxp.perm_id = p.id")
    List<RoleAndPermRawData> selectRolesAndPerms();

    @ResultType(Long.class)
    @Select(value = "SELECT role_id FROM `user_x_role` WHERE user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
}
