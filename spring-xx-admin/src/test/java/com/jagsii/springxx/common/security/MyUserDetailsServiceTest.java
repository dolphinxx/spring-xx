package com.jagsii.springxx.common.security;

import com.jagsii.springxx.common.security.data.RoleAndPermRawData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MyUserDetailsServiceTest {
    @Autowired
    MyUserDetailsService service;

    private static RoleAndPermRawData createDate(Long id, Long parentId, String role, String perm) {
        RoleAndPermRawData data = new RoleAndPermRawData();
        data.setId(id);
        data.setParentId(parentId);
        data.setRoleValue(role);
        data.setPermValue(perm);
        return data;
    }

    @Test
    void collectAuthorities() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        RoleInfo role1 = new RoleInfo(1L, "role1", null);
        RoleInfo role2 = new RoleInfo(2L, "role2", 1L);
        role2.addPerm("perm1");
        role2.addPerm("perm2");
        RoleInfo role3 = new RoleInfo(3L, "role3", 1L);
        role3.addPerm("perm3");
        role3.addPerm("perm4");
        RoleInfo role4 = new RoleInfo(4L, "role4", 2L);
        role4.addPerm("perm5");
        role4.addPerm("perm6");
        RoleInfo role5 = new RoleInfo(5L, "role5", 2L);
        role5.addPerm("perm7");
        RoleInfo role6 = new RoleInfo(6L, "role6", 4L);
        role6.addPerm("perm8");
        RoleInfo role7 = new RoleInfo(7L, "role7", 4L);
        role7.addPerm("perm9");
        MyUserDetailsServiceImpl.collectAuthorities(authorities, role1);
        assertThat(authorities).extracting(e -> e instanceof PermGrantedAuthority && ((PermGrantedAuthority) e).getRaw().equals("perm9")).isNotEmpty();
        assertThat(authorities).extracting(e -> e instanceof RoleGrantedAuthority && ((RoleGrantedAuthority) e).getRole().equals("role4")).isNotEmpty();
    }

    @Test
    void formatAuthorities() throws Exception {
        List<RoleAndPermRawData> data = new ArrayList<>();
        data.add(createDate(1L, null, "role1", null));
        data.add(createDate(2L, null, "role2", null));
        data.add(createDate(3L, 1L, "role3", "perm3"));
        data.add(createDate(4L, 2L, "role4", "perm4"));
        data.add(createDate(5L, 3L, "role5", "perm5"));
        data.add(createDate(5L, 3L, "role5", "perm7"));
        data.add(createDate(6L, 5L, "role6", "perm6"));
        data.add(createDate(6L, 5L, "role6", "perm8"));
        Map<Long, RoleInfo> actual = MyUserDetailsServiceImpl.formatAuthorities(data);
        assertThat(actual).hasSize(6);
        assertThat(actual.get(1L))
                .hasFieldOrPropertyWithValue("value", "role1")
                .hasFieldOrPropertyWithValue("parentId", null);
        assertThat(actual.get(1L).getChildren())
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("value", "role3");
        assertThat(actual.get(2L).getChildren())
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("id", 4L).hasFieldOrPropertyWithValue("value", "role4");
        assertThat(actual.get(3L)).hasFieldOrPropertyWithValue("value", "role3").hasFieldOrPropertyWithValue("parentId", 1L);
        assertThat(actual.get(3L).getPerms()).containsExactly("perm3");
        assertThat(actual.get(5L).getPerms()).containsExactly("perm5", "perm7");
    }
}