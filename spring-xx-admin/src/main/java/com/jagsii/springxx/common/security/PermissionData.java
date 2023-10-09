package com.jagsii.springxx.common.security;

public class PermissionData {
    public static final String WILDCARD = "*";
    public static final String DELIMITER = ":";
    public final String domain;
    public final String actions;
    public final String targets;

    public PermissionData(String domain, String actions, String targets) {
        this.domain = domain;
        this.actions = actions == null ? WILDCARD : actions;
        this.targets = targets == null ? WILDCARD : targets;
    }

    public PermissionData(String perm) {
        String[] tokens = perm.split(DELIMITER);
        domain = tokens[0];
        if (tokens.length > 1) {
            actions = tokens[1];
            if (tokens.length > 2) {
                targets = tokens[2];
            } else {
                targets = WILDCARD;
            }
        } else {
            actions = WILDCARD;
            targets = WILDCARD;
        }
    }

    public boolean hasPermission(String perm) {
        return hasPermission(new PermissionData(perm));
    }

    public boolean hasPermission(PermissionData another) {
        if (this.domain.equals(WILDCARD)) {
            return true;
        }
        if (!this.domain.equals(another.domain)) {
            return false;
        }
        if (this.actions.equals(WILDCARD)) {
            return true;
        }
        if (!this.actions.equals(another.actions)) {
            return false;
        }
        if (this.targets.equals(WILDCARD)) {
            return true;
        }
        return this.targets.equals(another.targets);
    }
}
