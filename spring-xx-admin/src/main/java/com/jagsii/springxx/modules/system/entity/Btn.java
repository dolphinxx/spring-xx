package com.jagsii.springxx.modules.system.entity;

import com.jagsii.springxx.common.base.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Btn extends AuditableEntity {
    /**
     * 按钮名称
     */
    private String name;
    /**
     * 按钮的键, 用于客户端标识
     */
    private String key;
    /**
     * 类型, 1=左侧菜单, 2=顶部导航, 3=其它按钮
     */
    private Integer type;
    /**
     * 按钮目标, 如叶子菜单的链接
     */
    private String target;
    /**
     * 图标名称, 如 mdi-home
     */
    private String icon;
    /**
     * 所需权限值，domain:actions:targets格式
     */
    private String perm;
    /**
     * 父级按钮ID
     */
    private Long parentId;
    /**
     * 排序
     */
    private Integer order;
}
