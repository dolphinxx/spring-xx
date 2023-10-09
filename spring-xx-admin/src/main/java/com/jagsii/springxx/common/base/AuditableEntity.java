package com.jagsii.springxx.common.base;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AuditableEntity extends BaseEntity {
    protected Long creatorId;
    protected Long lastModifierId;
    protected LocalDateTime lastModifyTime;
}
