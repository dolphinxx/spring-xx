package com.jagsii.springxx.common.security.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleAndPermRawData {
    private Long id;
    private Long parentId;
    private String roleValue;
    private String permValue;
}
