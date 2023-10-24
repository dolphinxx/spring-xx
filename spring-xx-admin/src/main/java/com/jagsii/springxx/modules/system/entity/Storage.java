package com.jagsii.springxx.modules.system.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Storage {
    private Long id;
    private String key;
    private Long creatorId;
    private LocalDateTime createTime;
}
