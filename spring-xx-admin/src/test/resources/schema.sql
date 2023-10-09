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
    `creator_id`       bigint unsigned  NOT NULL COMMENT '创建者ID',
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
    `creator_id`       bigint unsigned  NOT NULL COMMENT '创建者ID',
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
    `creator_id`       bigint unsigned  NOT NULL COMMENT '创建者ID',
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

