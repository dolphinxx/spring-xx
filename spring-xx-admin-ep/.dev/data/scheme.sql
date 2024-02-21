CREATE TABLE user (
  id TEXT (64) PRIMARY KEY,
  name TEXT (64) NOT NULL,
  username TEXT (64) NOT NULL,
  password TEXT (64) NOT NULL,
  status INTEGER NOT NULL DEFAULT (1),
  remark TEXT,
  createTime INTEGER NOT NULL,
  updateTime INTEGER,
  creatorId INTEGER,
  lastModifierId TEXT (64),
  lastModifyTime INTEGER
);