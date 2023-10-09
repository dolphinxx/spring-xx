package com.jagsii.springxx.modules.system.entity;

import com.jagsii.springxx.common.base.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGroup extends AuditableEntity {
    private String name;
    private Long parentId;
}
