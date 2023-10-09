package com.jagsii.springxx.modules.system.entity;

import com.jagsii.springxx.common.base.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends AuditableEntity {
    private String name;
    private String username;
    private String password;
}
