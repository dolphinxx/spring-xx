-- CREATE DATABASE IF NOT EXISTS `xx` DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- USE `xx`;

CREATE TABLE IF NOT EXISTS `user`
(
    `id`               bigint unsigned  NOT NULL AUTO_INCREMENT,
    `name`             varchar(50)      NOT NULL COMMENT '用户名称',
    `username`         varchar(50)      NOT NULL COMMENT '用户账号',
    `password`         varchar(50)      NOT NULL COMMENT '用户密码',
    `status`           tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
    `remark`           varchar(255)     NOT NULL DEFAULT '' COMMENT '备注',
    `creator_id`       bigint unsigned COMMENT '创建者ID',
    `last_modifier_id` bigint unsigned COMMENT '最后修改者ID',
    `last_modify_time` timestamp COMMENT '最后修改时间',
    `create_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT '系统用户';

CREATE TABLE IF NOT EXISTS `role`
(
    `id`               bigint unsigned  NOT NULL AUTO_INCREMENT,
    `name`             varchar(50)      NOT NULL COMMENT '角色名称',
    `value`            varchar(50)      NOT NULL COMMENT '角色值，用于鉴权',
    `parent_id`        bigint unsigned COMMENT '' COMMENT '父级角色ID',
    `status`           tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
    `remark`           varchar(255)     NOT NULL DEFAULT '' COMMENT '备注',
    `creator_id`       bigint unsigned COMMENT '创建者ID',
    `last_modifier_id` bigint unsigned COMMENT '最后修改者ID',
    `last_modify_time` timestamp COMMENT '最后修改时间',
    `create_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT '系统角色';

CREATE TABLE IF NOT EXISTS `perm`
(
    `id`               bigint unsigned  NOT NULL AUTO_INCREMENT,
    `name`             varchar(50)      NOT NULL COMMENT '权限名称',
    `value`            varchar(50)      NOT NULL COMMENT '权限值，domain:actions:targets格式，用于鉴权',
    `status`           tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
    `remark`           varchar(255)     NOT NULL DEFAULT '' COMMENT '备注',
    `creator_id`       bigint unsigned COMMENT '创建者ID',
    `last_modifier_id` bigint unsigned COMMENT '最后修改者ID',
    `last_modify_time` timestamp COMMENT '最后修改时间',
    `create_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT '系统权限';

CREATE TABLE IF NOT EXISTS `user_x_role`
(
    `id`      bigint unsigned NOT NULL AUTO_INCREMENT,
    `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
    `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE user_role_uk (`user_id`, `role_id`)
) ENGINE = InnoDB COMMENT '用户角色关联';

CREATE TABLE IF NOT EXISTS `role_x_perm`
(
    `id`      bigint unsigned NOT NULL AUTO_INCREMENT,
    `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
    `perm_id` bigint unsigned NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`id`),
    UNIQUE role_perm_uk (`role_id`, `perm_id`)
) ENGINE = InnoDB COMMENT '角色权限关联';

CREATE TABLE IF NOT EXISTS `btn`
(
    `id`               bigint unsigned  NOT NULL AUTO_INCREMENT,
    `name`             varchar(50)      NOT NULL COMMENT '按钮名称',
    `key`              varchar(50)      NOT NULL COMMENT '按钮的键, 用于客户端标识',
    `type`             tinyint unsigned NOT NULL DEFAULT '1' COMMENT '类型, 1=左侧菜单, 2=顶部导航, 3=其它按钮',
    `target`           varchar(50) COMMENT '按钮目标, 如叶子菜单的链接',
    `icon`             varchar(50) COMMENT '图标名称, 如 mdi-home',
    `perm`             varchar(50)      NOT NULL COMMENT '所需权限值，domain:actions:targets格式',
    `parent_id`        bigint unsigned COMMENT '父级按钮ID',
    `order`            int unsigned     NOT NULL DEFAULT '0' COMMENT '排序',
    `status`           tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
    `remark`           varchar(255)     NOT NULL DEFAULT '' COMMENT '备注',
    `creator_id`       bigint unsigned COMMENT '创建者ID',
    `last_modifier_id` bigint unsigned COMMENT '最后修改者ID',
    `last_modify_time` timestamp COMMENT '最后修改时间',
    `create_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT '按钮';

CREATE TABLE IF NOT EXISTS `parameter`
(
    `id`               bigint unsigned  NOT NULL AUTO_INCREMENT,
    `key`              varchar(50)      NOT NULL COMMENT '配置键名',
    `value`            text             NOT NULL COMMENT '配置值',
    `status`           tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
    `remark`           varchar(255)     NOT NULL DEFAULT '' COMMENT '备注',
    `creator_id`       bigint unsigned COMMENT '创建者ID',
    `last_modifier_id` bigint unsigned COMMENT '最后修改者ID',
    `last_modify_time` timestamp COMMENT '最后修改时间',
    `create_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT '服务端配置';