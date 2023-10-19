INSERT INTO `user`(`id`, `name`, `username`, `password`)VALUES(1, 'Admin', 'admin', '{noop}123456');

INSERT INTO `role`(`id`, `name`, `value`)VALUES(1, 'Admin', 'admin');

INSERT INTO `perm`(`id`, `name`, `value`)VALUES(1, 'All', '*');

INSERT INTO `user_x_role`(`id`, `user_id`, `role_id`)VALUES(1, 1, 1);

INSERT INTO `role_x_perm`(`id`, `role_id`, `perm_id`)VALUES(1, 1, 1);

INSERT INTO `parameter`(`id`, `key`, `value`)VALUES(1, 'settings', '{"name":"SpringXX"}');

INSERT INTO `btn`(`id`, `name`, `key`, `type`, `target`, `icon`, `perm`, `order`)VALUES(1, '系统管理', 'system_root', 1, null, 'mdi-cog-outline', '', 100);
INSERT INTO `btn`(`id`, `name`, `key`, `type`, `target`, `icon`, `perm`, `order`, `parent_id`)VALUES(2, '系统管理', 'system_manage', 1, '/sys/sys/', 'mdi-console', 'sys', 101, 1);
INSERT INTO `btn`(`id`, `name`, `key`, `type`, `target`, `icon`, `perm`, `order`, `parent_id`)VALUES(3, '用户管理', 'user_root', 1, '/sys/user/', 'mdi-account-outline', 'user:read', 102, 1);
INSERT INTO `btn`(`id`, `name`, `key`, `type`, `target`, `icon`, `perm`, `order`, `parent_id`)VALUES(4, '角色管理', 'role_root', 1, '/sys/role/', 'mdi-account-multiple-outline', 'role:read', 103, 1);
INSERT INTO `btn`(`id`, `name`, `key`, `type`, `target`, `icon`, `perm`, `order`, `parent_id`)VALUES(5, '权限管理', 'perm_root', 1, '/sys/perm/', 'mdi-shield-lock-outline', 'perm:read', 104, 1);
INSERT INTO `btn`(`id`, `name`, `key`, `type`, `target`, `icon`, `perm`, `order`, `parent_id`)VALUES(6, '按钮管理', 'btn_root', 1, '/sys/btn/', 'mdi-gesture-tap-button', 'btn:read', 105, 1);
INSERT INTO `btn`(`id`, `name`, `key`, `type`, `target`, `icon`, `perm`, `order`, `parent_id`)VALUES(7, '参数管理', 'parameter_root', 1, '/sys/parameter/', 'mdi-code-json', 'parameter:read', 106, 1);
