package com.jagsii.springxx.common.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

class AuthorityPermissionEvaluatorTest {
    @Test
    void hasPermission() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new PermGrantedAuthority("a:read:1"));
        authorities.add(new PermGrantedAuthority("b:read"));
        authorities.add(new PermGrantedAuthority("c"));
        String permission;
        boolean actual;
        permission = "a:read:1";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertTrue(actual);

        permission = "a:read:11";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertFalse(actual);

        permission = "a:read";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertFalse(actual);

        permission = "b:read:1";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertTrue(actual);

        permission = "b:read";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertTrue(actual);

        permission = "b:delete";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertFalse(actual);

        permission = "b";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertFalse(actual);

        permission = "c:read:1";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertTrue(actual);

        permission = "c:read";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertTrue(actual);

        permission = "c";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertTrue(actual);

        permission = "d";
        actual = AuthorityPermissionEvaluator.hasPermission(authorities, permission);
        Assertions.assertFalse(actual);
    }
}