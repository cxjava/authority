
INSERT INTO t_base_module (id,module_name,module_url,parent_id,leaf,expanded,display_index,is_display,en_module_name,icon_css,information,parent_url) VALUES (1,'系统设置',NULL,0,0,1,1,1,'System Settings','system_settings',NULL,NULL);
INSERT INTO t_base_module (id,module_name,module_url,parent_id,leaf,expanded,display_index,is_display,en_module_name,icon_css,information,parent_url) VALUES (2,'供应商管理',NULL,0,0,1,2,1,'Operator','abc',NULL,NULL);
INSERT INTO t_base_module (id,module_name,module_url,parent_id,leaf,expanded,display_index,is_display,en_module_name,icon_css,information,parent_url) VALUES (11,'角色管理','/role',1,1,0,3,1,'Role Management','role',NULL,NULL);
INSERT INTO t_base_module (id,module_name,module_url,parent_id,leaf,expanded,display_index,is_display,en_module_name,icon_css,information,parent_url) VALUES (12,'用户管理','/user',1,1,0,2,1,'User Management','user',NULL,NULL);
INSERT INTO t_base_module (id,module_name,module_url,parent_id,leaf,expanded,display_index,is_display,en_module_name,icon_css,information,parent_url) VALUES (13,'模块管理','/module',1,1,0,1,1,'Module Management','module',NULL,NULL);
INSERT INTO t_base_module (id,module_name,module_url,parent_id,leaf,expanded,display_index,is_display,en_module_name,icon_css,information,parent_url) VALUES (14,'系统字段管理','/field',1,1,1,4,1,'field','field',NULL,NULL);
INSERT INTO t_base_module (id,module_name,module_url,parent_id,leaf,expanded,display_index,is_display,en_module_name,icon_css,information,parent_url) VALUES  (21,'供应商信息','/oprator',2,1,0,1,1,'oprator','cde',NULL,NULL);

INSERT INTO t_base_role (id,role_name,role_desc) VALUES (1,'管理员','管理员');
INSERT INTO t_base_role (id,role_name,role_desc) VALUES (2,'测试角色','测试角色');
 
INSERT INTO t_base_role_module (id,role_id,module_id) VALUES (1,2,2);
INSERT INTO t_base_role_module (id,role_id,module_id) VALUES (2,2,21);
INSERT INTO t_base_role_module (id,role_id,module_id) VALUES (3,1,1);
INSERT INTO t_base_role_module (id,role_id,module_id) VALUES (4,1,2);
INSERT INTO t_base_role_module (id,role_id,module_id) VALUES (5,1,13);
INSERT INTO t_base_role_module (id,role_id,module_id) VALUES (6,1,12);
INSERT INTO t_base_role_module (id,role_id,module_id) VALUES (7,1,11);
INSERT INTO t_base_role_module (id,role_id,module_id) VALUES (8,1,14);
INSERT INTO t_base_role_module (id,role_id,module_id) VALUES (9,1,21);

INSERT INTO t_base_user (id,account,password,real_name,sex,email,mobile,office_phone,error_count,last_login_time,last_login_ip,remark) VALUES (1,'admin','6043ae1095884cf9663d140ee6450b49b8489b3aa073a8eec024492b976ee2a24aee0c272369121b','超级管理员',0,'admin@qq.com.cn','119','110',0,'2012-11-07 15:52:04','127.0.0.1','用户信息');
INSERT INTO t_base_user (id,account,password,real_name,sex,email,mobile,office_phone,error_count,last_login_time,last_login_ip,remark) VALUES (2,'test','ddee6e95fae5bb5f8890a6f9ef7d0d1db744ca4417e94c05595ef280046a49021eba3291ee9c9cf8','测试用户',0,'test@qq.com','119','110',0,'2012-11-06 16:52:07',NULL,NULL);

INSERT INTO t_base_user_role (id,user_id,role_id) VALUES  (1,1,1);
INSERT INTO t_base_user_role (id,user_id,role_id) VALUES  (2,2,2);
 
 
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (1,'sex','性别','0','男',1,1);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (2,'sex','性别','1','女',1,2);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (3,'sex','性别','2','其他',1,3);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (4,'sex','性别','3','保密',0,4);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (5,'pagesize','每页显示条数','10','10条/页',1,1);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (6,'pagesize','每页显示条数','20','20条/页',1,2);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (7,'pagesize','每页显示条数','30','30条/页',1,3);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (8,'pagesize','每页显示条数','50','50条/页',1,4);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (9,'theme','风格','xtheme-blue.css','经典蓝色',1,1);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (10,'theme','风格','xtheme-gray.css','简约灰色',1,2);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (11,'leaf','父模块','0','父节点',1,1);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (12,'leaf','父模块','1','子节点',1,2);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (13,'expanded','展开状态','0','收缩',1,1);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (14,'expanded','展开状态','1','展开',1,2);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (15,'isdisplay','是否显示','0','否',1,1);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (16,'isdisplay','是否显示','1','是',1,2);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (17,'pagesize','每页显示条数','100','100条/页',1,5);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (18,'pagesize','每页显示条数','200','200条/页',1,6);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (19,'pagesize','每页显示条数','500','500条/页',0,7);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (20,'enabled','是否启用','0','禁用',1,2);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (21,'enabled','是否启用','1','启用',1,1);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (22,'theme','风格','ext-all-xtheme-brown02.css','灰棕色',1,5);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (23,'theme','风格','xtheme-calista.css','绿黄双重色',0,8);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (24,'theme','风格','xtheme-indigo.css','靛青',1,9);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (25,'theme','风格','xtheme-slate.css','石板色',1,14);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (26,'theme','风格','xtheme-olive.css','绿色',1,11);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (27,'theme','风格','xtheme-black.css','黑色',1,6);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (28,'theme','风格','xtheme-darkgray.css','暗灰',1,7);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (29,'theme','风格','xtheme-slickness.css','全黑',1,15);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (30,'theme','风格','ext-all-xtheme-brown.css','红棕色',1,4);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (31,'theme','风格','ext-all-xtheme-red03.css','粉红',1,6);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (32,'theme','风格','xtheme-purple.css','紫色',1,13);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (33,'theme','风格','ext-all-xtheme-blue03.css','灰色',1,3);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (34,'theme','风格','xtheme-midnight.css','午夜',1,10);
INSERT INTO t_base_field (id,field,field_name,value_field,display_field,enabled,sort) VALUES (35,'theme','风格','xtheme-pink.css','粉红色',1,12);