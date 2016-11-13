
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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='ϵͳģ���';

CREATE TABLE t_base_role (
  id int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '��ɫID',
  role_name varchar(64) DEFAULT NULL COMMENT '��ɫ����',
  role_desc varchar(128) DEFAULT NULL COMMENT '��ɫ����',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='��ɫ��';

CREATE TABLE t_base_role_module (
  id int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '��ɫģ��ID',
  role_id int(10) unsigned DEFAULT NULL COMMENT '��ɫID',
  module_id int(10) unsigned DEFAULT NULL COMMENT 'ģ��ID',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='��ɫģ���';

CREATE TABLE t_base_user (
  id int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '�û�ID',
  account varchar(64) NOT NULL COMMENT '�˺�',
  password varchar(128) NOT NULL COMMENT '����\r\norg.springframework.security.crypto.password.StandardPasswordEncoder.StandardPasswordEncoder(CharSequence secret)',
  real_name varchar(64) DEFAULT NULL COMMENT '�û���ʵ����',
  sex int(1) unsigned DEFAULT NULL COMMENT '�Ա� 0:�� 1:Ů',
  email varchar(64) DEFAULT NULL COMMENT '�����ʼ���ַ',
  mobile varchar(32) DEFAULT NULL COMMENT '�ֻ�',
  office_phone varchar(32) DEFAULT NULL COMMENT '�칫�绰',
  error_count int(2) unsigned DEFAULT '0' COMMENT '����������',
  last_login_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '�ϴε�¼ʱ��',
  last_login_ip varchar(32) DEFAULT NULL COMMENT '�ϴε�¼IP��ַ',
  remark varchar(128) DEFAULT NULL COMMENT '��ע',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='�û���';

CREATE TABLE t_base_user_role (
  id int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '�û���ɫID',
  user_id int(10) unsigned DEFAULT NULL COMMENT '�û�ID',
  role_id int(10) unsigned DEFAULT NULL COMMENT '��ɫID',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='�û���ɫ��';
