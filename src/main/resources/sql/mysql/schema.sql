
DROP TABLE IF EXISTS t_base_module;
DROP TABLE IF EXISTS t_base_role;
DROP TABLE IF EXISTS t_base_role_module;
DROP TABLE IF EXISTS t_base_user;
DROP TABLE IF EXISTS t_base_user_role;
DROP TABLE IF EXISTS t_base_field;


CREATE TABLE t_base_field (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  field varchar(64) DEFAULT NULL,
  field_name varchar(128) DEFAULT NULL,
  value_field varchar(128) DEFAULT NULL,
  display_field varchar(128) DEFAULT NULL,
  enabled int(2) DEFAULT NULL,
  sort int(2) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;


CREATE TABLE t_base_module (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  module_name varchar(64) NOT NULL,
  module_url varchar(64) DEFAULT NULL,
  parent_id int(10) unsigned DEFAULT NULL,
  leaf int(1) unsigned DEFAULT NULL,
  expanded int(1) unsigned DEFAULT NULL,
  display_index int(2) unsigned DEFAULT NULL,
  is_display int(1) unsigned DEFAULT NULL,
  en_module_name varchar(64) DEFAULT NULL,
  icon_css varchar(128) DEFAULT NULL,
  information varchar(128) DEFAULT NULL,
  parent_url bigint(20) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='系统模块表';

CREATE TABLE t_base_role (
  id int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  role_name varchar(64) DEFAULT NULL COMMENT '角色名称',
  role_desc varchar(128) DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE t_base_role_module (
  id int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色模块ID',
  role_id int(10) unsigned DEFAULT NULL COMMENT '角色ID',
  module_id int(10) unsigned DEFAULT NULL COMMENT '模块ID',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='角色模块表';

CREATE TABLE t_base_user (
  id int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  account varchar(64) NOT NULL COMMENT '账号',
  password varchar(128) NOT NULL COMMENT '密码\r\norg.springframework.security.crypto.password.StandardPasswordEncoder.StandardPasswordEncoder(CharSequence secret)',
  real_name varchar(64) DEFAULT NULL COMMENT '用户真实姓名',
  sex int(1) unsigned DEFAULT NULL COMMENT '性别 0:男 1:女',
  email varchar(64) DEFAULT NULL COMMENT '电子邮件地址',
  mobile varchar(32) DEFAULT NULL COMMENT '手机',
  office_phone varchar(32) DEFAULT NULL COMMENT '办公电话',
  error_count int(2) unsigned DEFAULT '0' COMMENT '密码错误次数',
  last_login_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上次登录时间',
  last_login_ip varchar(32) DEFAULT NULL COMMENT '上次登录IP地址',
  remark varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE t_base_user_role (
  id int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
  user_id int(10) unsigned DEFAULT NULL COMMENT '用户ID',
  role_id int(10) unsigned DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户角色表';
