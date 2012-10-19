alter table base_role_module
   drop constraint FK_BASE_ROL_REFERENCE_BASE_ROL;

alter table base_role_module
   drop constraint FK_BASE_ROL_REFERENCE_BASE_MOD;

alter table base_user_role
   drop constraint FK_BASE_USE_REFERENCE_BASE_USE;

alter table base_user_role
   drop constraint FK_BASE_USE_REFERENCE_BASE_ROL;

drop table base_fields cascade constraints;

drop table base_modules cascade constraints;

drop table base_role_module cascade constraints;

drop table base_roles cascade constraints;

drop table base_user_role cascade constraints;

drop table base_users cascade constraints;

drop SEQUENCE SEQ_BASE_MODULES; 

-- Create sequence 
create sequence SEQ_BASE_MODULES
minvalue 1
maxvalue 99999999999999999999999999
start with 140
increment by 1;


/*==============================================================*/
/* Table: base_fields                                         */
/*==============================================================*/
create table base_fields  (
   field_id           VARCHAR2(32)                    not null,
   field              VARCHAR2(64),
   field_name         VARCHAR2(128),
   value_field        VARCHAR2(128),
   display_field      VARCHAR2(128),
   enabled            NUMBER(2),
   sort               NUMBER(2),
   constraint PK_BASE_FIELDS primary key (field_id)
);

comment on table base_fields is
'系统字段设置表';

comment on column base_fields.field_id is
'字段ID';

comment on column base_fields.field is
'字段';

comment on column base_fields.field_name is
'字段名称';

comment on column base_fields.value_field is
'字段值';

comment on column base_fields.display_field is
'字段显示值';

comment on column base_fields.enabled is
'是否启用';

comment on column base_fields.sort is
'排序';

/*==============================================================*/
/* Table: base_modules                                        */
/*==============================================================*/
create table base_modules  (
   module_id          NUMBER(9)                       not null,
   module_name        VARCHAR2(64)                    not null,
   module_url         VARCHAR2(64),
   parent_id          NUMBER(9),
   leaf               NUMBER(1),
   expanded           NUMBER(1),
   display_index      NUMBER(2),
   is_display         NUMBER(1),
   en_module_name     VARCHAR2(64),
   icon_css           VARCHAR2(128),
   information        VARCHAR2(128),
   constraint PK_BASE_MODULES primary key (module_id)
);

comment on table base_modules is
'系统模块表';

comment on column base_modules.module_id is
'模块ID';

comment on column base_modules.module_name is
'模块名称';

comment on column base_modules.module_url is
'模块URL';

comment on column base_modules.parent_id is
'父模块ID';

comment on column base_modules.leaf is
'叶子节点(0:树枝节点;1:叶子节点)';

comment on column base_modules.expanded is
'展开状态(1:展开;0:收缩)';

comment on column base_modules.display_index is
'显示顺序';

comment on column base_modules.is_display is
'是否显示 0:否 1:是';

comment on column base_modules.en_module_name is
'模块英文名称';

comment on column base_modules.icon_css is
'图标或者样式';

comment on column base_modules.information is
'节点说明';

/*==============================================================*/
/* Table: base_role_module                                    */
/*==============================================================*/
create table base_role_module  (
   role_module_id     VARCHAR2(32)                    not null,
   role_id            VARCHAR2(32)                    not null,
   module_id          NUMBER(9)                       not null,
   constraint PK_BASE_ROLE_MODULE primary key (role_module_id)
);

comment on table base_role_module is
'角色模块表';

comment on column base_role_module.role_module_id is
'角色模块ID';

comment on column base_role_module.role_id is
'角色ID';

comment on column base_role_module.module_id is
'模块ID';

/*==============================================================*/
/* Table: base_roles                                          */
/*==============================================================*/
create table base_roles  (
   role_id            VARCHAR2(32)                    not null,
   role_name          VARCHAR2(64),
   role_desc          VARCHAR2(128),
   constraint PK_BASE_ROLES primary key (role_id)
);

comment on table base_roles is
'角色表';

comment on column base_roles.role_id is
'角色ID';

comment on column base_roles.role_name is
'角色名称';

comment on column base_roles.role_desc is
'角色描述';

/*==============================================================*/
/* Table: base_user_role                                      */
/*==============================================================*/
create table base_user_role  (
   user_role_id       VARCHAR2(32)                    not null,
   user_id            VARCHAR2(32)                    not null,
   role_id            VARCHAR2(32)                    not null,
   constraint PK_BASE_USER_ROLE primary key (user_role_id)
);

comment on table base_user_role is
'用户角色表';

comment on column base_user_role.user_role_id is
'用户角色ID';

comment on column base_user_role.user_id is
'用户ID';

comment on column base_user_role.role_id is
'角色ID';



/*==============================================================*/
/* Table: base_users                                          */
/*==============================================================*/
create table base_users  (
   user_id            VARCHAR2(32)                    not null,
   account            VARCHAR2(64)                    not null,
   password           VARCHAR2(128)                   not null,
   real_name          VARCHAR2(64),
   sex                NUMBER(1),
   email              VARCHAR2(64),
   mobile             VARCHAR2(32),
   office_phone       VARCHAR2(32),
   error_count        NUMBER(2)                      default 0,
   last_login_time    DATE,
   last_login_ip      VARCHAR2(32),
   remark             VARCHAR2(128),
   constraint PK_BASE_USERS primary key (user_id)
);

comment on table base_users is
'用户表';

comment on column base_users.user_id is
'用户ID';

comment on column base_users.account is
'账号';

comment on column base_users.password is
'密码
org.springframework.security.crypto.password.StandardPasswordEncoder.StandardPasswordEncoder(CharSequence secret)';

comment on column base_users.real_name is
'用户真实姓名';

comment on column base_users.sex is
'性别 0:男 1:女';

comment on column base_users.email is
'电子邮件地址';

comment on column base_users.mobile is
'手机';

comment on column base_users.office_phone is
'办公电话';

comment on column base_users.error_count is
'密码错误次数';

comment on column base_users.last_login_time is
'上次登录时间';

comment on column base_users.last_login_ip is
'上次登录IP地址';

comment on column base_users.remark is
'备注';

alter table base_role_module
   add constraint FK_BASE_ROL_REFERENCE_BASE_ROL foreign key (role_id)
      references base_roles (role_id);

alter table base_role_module
   add constraint FK_BASE_ROL_REFERENCE_BASE_MOD foreign key (module_id)
      references base_modules (module_id);

alter table base_user_role
   add constraint FK_BASE_USE_REFERENCE_BASE_USE foreign key (user_id)
      references base_users (user_id);

alter table base_user_role
   add constraint FK_BASE_USE_REFERENCE_BASE_ROL foreign key (role_id)
      references base_roles (role_id);



