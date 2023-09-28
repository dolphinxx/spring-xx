SET MODE MYSQL;

CREATE TABLE `user`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT,
    `name`        varchar(50)     NOT NULL,
    `address`     varchar(255)    NOT NULL DEFAULT '',
    `birth`       date                     DEFAULT NULL,
    `create_time` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;