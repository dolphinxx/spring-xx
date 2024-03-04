SET MODE MYSQL;

CREATE TABLE `user`
(
    `id`          bigint unsigned  NOT NULL AUTO_INCREMENT,
    `name`        varchar(50)      NOT NULL,
    `username`    varchar(50)      NOT NULL,
    `password`    varchar(50)      NOT NULL,
    `birth`       date                      DEFAULT NULL,
    `status`      tinyint unsigned NOT NULL DEFAULT '1',
    `remark`      varchar(255)     NOT NULL DEFAULT '',
    `create_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;