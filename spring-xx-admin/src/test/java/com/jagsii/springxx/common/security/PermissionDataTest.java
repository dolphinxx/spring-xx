package com.jagsii.springxx.common.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PermissionDataTest {
    @Test
    void construct() {
        PermissionData perm;

        perm = new PermissionData("*");
        Assertions.assertEquals("*", perm.domain);
        Assertions.assertEquals("*", perm.actions);
        Assertions.assertEquals("*", perm.targets);

        perm = new PermissionData("a");
        Assertions.assertEquals("a", perm.domain);
        Assertions.assertEquals("*", perm.actions);
        Assertions.assertEquals("*", perm.targets);

        perm = new PermissionData("a:b");
        Assertions.assertEquals("a", perm.domain);
        Assertions.assertEquals("b", perm.actions);
        Assertions.assertEquals("*", perm.targets);

        perm = new PermissionData("a:b:c");
        Assertions.assertEquals("a", perm.domain);
        Assertions.assertEquals("b", perm.actions);
        Assertions.assertEquals("c", perm.targets);
    }

    @Test
    void hasPermission() {
        PermissionData perm;
        boolean actual;

        perm = new PermissionData("a");
        actual = perm.hasPermission("a:b:c");
        Assertions.assertTrue(actual);

        perm = new PermissionData("a");
        actual = perm.hasPermission("a:b");
        Assertions.assertTrue(actual);

        perm = new PermissionData("a");
        actual = perm.hasPermission("a");
        Assertions.assertTrue(actual);

        perm = new PermissionData("a");
        actual = perm.hasPermission("b");
        Assertions.assertFalse(actual);

        perm = new PermissionData("a:b");
        actual = perm.hasPermission("a");
        Assertions.assertFalse(actual);

        perm = new PermissionData("a:b");
        actual = perm.hasPermission("a:b");
        Assertions.assertTrue(actual);

        perm = new PermissionData("a:b");
        actual = perm.hasPermission("a:b:c");
        Assertions.assertTrue(actual);

        perm = new PermissionData("a:b:c");
        actual = perm.hasPermission("a:b:c");
        Assertions.assertTrue(actual);

        perm = new PermissionData("a:b:c");
        actual = perm.hasPermission("a:b");
        Assertions.assertFalse(actual);

        perm = new PermissionData("a:b:c");
        actual = perm.hasPermission("a");
        Assertions.assertFalse(actual);

        perm = new PermissionData("a:b:c");
        actual = perm.hasPermission("*");
        Assertions.assertFalse(actual);

        perm = new PermissionData("*");
        actual = perm.hasPermission("*");
        Assertions.assertTrue(actual);

        perm = new PermissionData("*");
        actual = perm.hasPermission("a");
        Assertions.assertTrue(actual);

        perm = new PermissionData("*");
        actual = perm.hasPermission("a:b");
        Assertions.assertTrue(actual);

        perm = new PermissionData("*");
        actual = perm.hasPermission("a:b:c");
        Assertions.assertTrue(actual);
    }
}