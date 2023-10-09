package com.jagsii.springxx.common.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseEntity implements Serializable {
    protected Long id;
    protected Integer status;
    protected String remark;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;
}
