package com.jagsii.springxx.common.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"children", "perms"})
public class RoleInfo {
    private Long id;
    private String value;
    private Long parentId;
    private Set<RoleInfo> children;
    private Set<String> perms;

    public RoleInfo(Long id, String value, Long parentId) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;
        this.children = new HashSet<>();
        this.perms = new HashSet<>();
    }

    public void addPerm(String perm) {
        perms.add(perm);
    }

    public void addChild(RoleInfo child) {
        children.add(child);
    }
}
