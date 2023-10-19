package com.jagsii.springxx.modules.system.mapper;

import com.jagsii.springxx.DbTests;
import com.jagsii.springxx.common.security.data.RoleAndPermRawData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

class RoleMapperTest extends DbTests {

    @Test
    void selectRolesAndPerms() {
        testMapper(RoleMapper.class, mapper -> {
            String insertRoleSql = "INSERT INTO `role`(`id`, `name`, `value`, `creator_id`, `parent_id`)VALUES(?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertRoleSql, 2L, "role2", "role2", 1L, null);
            jdbcTemplate.update(insertRoleSql, 4L, "role4", "role4", 1L, null);
            jdbcTemplate.update(insertRoleSql, 6L, "role6", "role6", 1L, 2L);
            jdbcTemplate.update(insertRoleSql, 8L, "role8", "role8", 1L, 4L);
            jdbcTemplate.update(insertRoleSql, 1L, "role1", "role1", 1L, 6L);
            jdbcTemplate.update(insertRoleSql, 3L, "role3", "role3", 1L, 1L);
            jdbcTemplate.update(insertRoleSql, 5L, "role5", "role5", 1L, 3L);
            jdbcTemplate.update(insertRoleSql, 7L, "role7", "role7", 1L, 5L);
            String insertPermSql = "INSERT INTO `perm`(`id`, `name`, `value`, `creator_id`)VALUES(?, ?, ?, ?)";
            jdbcTemplate.update(insertPermSql, 1L, "perm1", "perm1", 1L);
            jdbcTemplate.update(insertPermSql, 2L, "perm2", "perm2", 1L);
            jdbcTemplate.update(insertPermSql, 3L, "perm3", "perm3", 1L);
            jdbcTemplate.update(insertPermSql, 4L, "perm4", "perm4", 1L);
            jdbcTemplate.update(insertPermSql, 5L, "perm5", "perm5", 1L);
            jdbcTemplate.update(insertPermSql, 6L, "perm6", "perm6", 1L);
            jdbcTemplate.update(insertPermSql, 7L, "perm7", "perm7", 1L);
            String insertRoleXPermSql = "INSERT INTO `role_x_perm`(`role_id`, `perm_id`)VALUES(?, ?)";
            for (int i = 1; i < 8; i++) {
                jdbcTemplate.update(insertRoleXPermSql, i, i);
            }
            List<RoleAndPermRawData> actual = mapper.selectRolesAndPerms();
            actual.sort(Comparator.comparingLong(RoleAndPermRawData::getId));
            Assertions.assertThat(actual).hasSize(8);
            Assertions.assertThat(actual.get(0)).hasFieldOrPropertyWithValue("parentId", 6L).hasFieldOrPropertyWithValue("permValue", "perm1");
            Assertions.assertThat(actual.get(1)).hasFieldOrPropertyWithValue("parentId", null).hasFieldOrPropertyWithValue("permValue", "perm2");
            Assertions.assertThat(actual.get(2)).hasFieldOrPropertyWithValue("parentId", 1L).hasFieldOrPropertyWithValue("permValue", "perm3");
            Assertions.assertThat(actual.get(3)).hasFieldOrPropertyWithValue("parentId", null).hasFieldOrPropertyWithValue("permValue", "perm4");
            Assertions.assertThat(actual.get(4)).hasFieldOrPropertyWithValue("parentId", 3L).hasFieldOrPropertyWithValue("permValue", "perm5");
            Assertions.assertThat(actual.get(5)).hasFieldOrPropertyWithValue("parentId", 2L).hasFieldOrPropertyWithValue("permValue", "perm6");
            Assertions.assertThat(actual.get(6)).hasFieldOrPropertyWithValue("parentId", 5L).hasFieldOrPropertyWithValue("permValue", "perm7");
            Assertions.assertThat(actual.get(7)).hasFieldOrPropertyWithValue("parentId", 4L).hasFieldOrPropertyWithValue("permValue", null);
        });
    }

    @Test
    void selectRoleIdsByUserId() {
        String insertRoleSql = "INSERT INTO `role`(`id`, `name`, `value`, `creator_id`)VALUES(?, ?, ?, ?)";
        for (int i = 1; i < 5; i++) {
            jdbcTemplate.update(insertRoleSql, i, "role" + i, "role" + i, 1L);
        }
        String insertUserRoleSql = "INSERT INTO `user_x_role`(`user_id`, `role_id`)VALUES(?, ?)";
        for (int i = 1; i < 4; i++) {
            jdbcTemplate.update(insertUserRoleSql, 1L, i);
        }
        jdbcTemplate.update(insertUserRoleSql, 2L, 4L);
        testMapper(RoleMapper.class, mapper -> {
            List<Long> actual = mapper.selectRoleIdsByUserId(1L);
            Assertions.assertThat(actual).containsExactly(1L, 2L, 3L);
        });
    }
}