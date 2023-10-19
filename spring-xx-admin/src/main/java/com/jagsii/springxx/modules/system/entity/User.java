package com.jagsii.springxx.modules.system.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jagsii.springxx.common.base.AuditableEntity;
import com.jagsii.springxx.common.json.HideSensitiveConverter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends AuditableEntity {
    private String name;
    private String username;
    @JsonSerialize(converter = HideSensitiveConverter.class)
    private String password;
}
