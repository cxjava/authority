-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.27


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema authority
--

CREATE DATABASE IF NOT EXISTS authority;
USE authority;

--
-- Definition of table `t_base_field`
--

DROP TABLE IF EXISTS `t_base_field`;
CREATE TABLE `t_base_field` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `field` varchar(64) DEFAULT NULL,
  `field_name` varchar(128) DEFAULT NULL,
  `value_field` varchar(128) DEFAULT NULL,
  `display_field` varchar(128) DEFAULT NULL,
  `enabled` int(2) DEFAULT NULL,
  `sort` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_base_field`
--

/*!40000 ALTER TABLE `t_base_field` DISABLE KEYS */;
INSERT INTO `t_base_field` (`id`,`field`,`field_name`,`value_field`,`display_field`,`enabled`,`sort`) VALUES 
 (1,'sex','性别','0','男',1,1),
 (2,'sex','性别','1','女',1,2),
 (3,'sex','性别','2','其他',1,3),
 (4,'sex','性别','3','保密',0,4),
 (5,'pagesize','每页显示条数','10','10条/页',1,1),
 (6,'pagesize','每页显示条数','20','20条/页',1,2),
 (7,'pagesize','每页显示条数','30','30条/页',1,3),
 (8,'pagesize','每页显示条数','50','50条/页',1,4),
 (9,'theme','风格','xtheme-blue.css','经典蓝色',1,1),
 (10,'theme','风格','xtheme-gray.css','简约灰色',1,2),
 (11,'leaf','父模块','0','父节点',1,1),
 (12,'leaf','父模块','1','子节点',1,2),
 (13,'expanded','展开状态','0','收缩',1,1),
 (14,'expanded','展开状态','1','展开',1,2),
 (15,'isdisplay','是否显示','0','否',1,1),
 (16,'isdisplay','是否显示','1','是',1,2),
 (17,'pagesize','每页显示条数','100','100条/页',1,5),
 (18,'pagesize','每页显示条数','200','200条/页',1,6),
 (19,'pagesize','每页显示条数','500','500条/页',0,7),
 (20,'enabled','是否启用','0','禁用',1,2),
 (21,'enabled','是否启用','1','启用',1,1),
 (22,'theme','风格','ext-all-xtheme-brown02.css','灰棕色',1,5),
 (23,'theme','风格','xtheme-calista.css','绿黄双重色',0,8),
 (24,'theme','风格','xtheme-indigo.css','靛青',1,9),
 (25,'theme','风格','xtheme-slate.css','石板色',1,14),
 (26,'theme','风格','xtheme-olive.css','绿色',1,11),
 (27,'theme','风格','xtheme-black.css','黑色',1,6),
 (28,'theme','风格','xtheme-darkgray.css','暗灰',1,7),
 (29,'theme','风格','xtheme-slickness.css','全黑',1,15),
 (30,'theme','风格','ext-all-xtheme-brown.css','红棕色',1,4),
 (31,'theme','风格','ext-all-xtheme-red03.css','粉红',1,6),
 (32,'theme','风格','xtheme-purple.css','紫色',1,13),
 (33,'theme','风格','ext-all-xtheme-blue03.css','灰色',1,3),
 (34,'theme','风格','xtheme-midnight.css','午夜',1,10),
 (35,'theme','风格','xtheme-pink.css','粉红色',1,12);
/*!40000 ALTER TABLE `t_base_field` ENABLE KEYS */;


--
-- Definition of table `t_base_module`
--

DROP TABLE IF EXISTS `t_base_module`;
CREATE TABLE `t_base_module` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `module_name` varchar(64) NOT NULL,
  `module_url` varchar(64) DEFAULT NULL,
  `parent_id` int(10) unsigned DEFAULT NULL,
  `leaf` int(1) unsigned DEFAULT NULL,
  `expanded` int(1) unsigned DEFAULT NULL,
  `display_index` int(2) unsigned DEFAULT NULL,
  `is_display` int(1) unsigned DEFAULT NULL,
  `en_module_name` varchar(64) DEFAULT NULL,
  `icon_css` varchar(128) DEFAULT NULL,
  `information` varchar(128) DEFAULT NULL,
  `parent_url` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='系统模块表';

--
-- Dumping data for table `t_base_module`
--

/*!40000 ALTER TABLE `t_base_module` DISABLE KEYS */;
INSERT INTO `t_base_module` (`id`,`module_name`,`module_url`,`parent_id`,`leaf`,`expanded`,`display_index`,`is_display`,`en_module_name`,`icon_css`,`information`,`parent_url`) VALUES 
 (1,'系统设置',NULL,0,0,1,1,1,'System Settings','system_settings',NULL,NULL),
 (2,'供应商管理',NULL,0,0,1,2,1,'Operator','abc',NULL,NULL),
 (11,'角色管理','/role',1,1,0,3,1,'Role Management','role',NULL,NULL),
 (12,'用户管理','/user',1,1,0,2,1,'User Management','user',NULL,NULL),
 (13,'模块管理','/module',1,1,0,1,1,'Module Management','module',NULL,NULL),
 (14,'系统字段管理','/field',1,1,1,4,1,'field','field',NULL,NULL),
 (21,'供应商信息','/oprator',2,1,0,1,1,'oprator','cde',NULL,NULL);
/*!40000 ALTER TABLE `t_base_module` ENABLE KEYS */;


--
-- Definition of table `t_base_role`
--

DROP TABLE IF EXISTS `t_base_role`;
CREATE TABLE `t_base_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(64) DEFAULT NULL COMMENT '角色名称',
  `role_desc` varchar(128) DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色表';

--
-- Dumping data for table `t_base_role`
--

/*!40000 ALTER TABLE `t_base_role` DISABLE KEYS */;
INSERT INTO `t_base_role` (`id`,`role_name`,`role_desc`) VALUES 
 (1,'管理员','管理员'),
 (2,'测试角色','测试角色');
/*!40000 ALTER TABLE `t_base_role` ENABLE KEYS */;


--
-- Definition of table `t_base_role_module`
--

DROP TABLE IF EXISTS `t_base_role_module`;
CREATE TABLE `t_base_role_module` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色模块ID',
  `role_id` int(10) unsigned DEFAULT NULL COMMENT '角色ID',
  `module_id` int(10) unsigned DEFAULT NULL COMMENT '模块ID',
  PRIMARY KEY (`id`),
  KEY `FK_base_role_module_1` (`role_id`),
  KEY `FK_base_role_module_2` (`module_id`),
  CONSTRAINT `FK_base_role_module_1` FOREIGN KEY (`role_id`) REFERENCES `t_base_role` (`id`),
  CONSTRAINT `FK_base_role_module_2` FOREIGN KEY (`module_id`) REFERENCES `t_base_module` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='角色模块表';

--
-- Dumping data for table `t_base_role_module`
--

/*!40000 ALTER TABLE `t_base_role_module` DISABLE KEYS */;
INSERT INTO `t_base_role_module` (`id`,`role_id`,`module_id`) VALUES 
 (1,2,2),
 (2,2,21),
 (3,1,1),
 (4,1,2),
 (5,1,13),
 (6,1,12),
 (7,1,11),
 (8,1,14),
 (9,1,21);
/*!40000 ALTER TABLE `t_base_role_module` ENABLE KEYS */;


--
-- Definition of table `t_base_user`
--

DROP TABLE IF EXISTS `t_base_user`;
CREATE TABLE `t_base_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `account` varchar(64) NOT NULL COMMENT '账号',
  `password` varchar(128) NOT NULL COMMENT '密码 123456',
  `real_name` varchar(64) DEFAULT NULL COMMENT '用户真实姓名',
  `sex` int(1) unsigned DEFAULT NULL COMMENT '性别 0:男 1:女',
  `email` varchar(64) DEFAULT NULL COMMENT '电子邮件地址',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机',
  `office_phone` varchar(32) DEFAULT NULL COMMENT '办公电话',
  `error_count` int(2) unsigned DEFAULT '0' COMMENT '密码错误次数',
  `last_login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上次登录时间',
  `last_login_ip` varchar(32) DEFAULT NULL COMMENT '上次登录IP地址',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户表';

--
-- Dumping data for table `t_base_user`
--

/*!40000 ALTER TABLE `t_base_user` DISABLE KEYS */;
INSERT INTO `t_base_user` (`id`,`account`,`password`,`real_name`,`sex`,`email`,`mobile`,`office_phone`,`error_count`,`last_login_time`,`last_login_ip`,`remark`) VALUES 
 (2,'test','f018df33bdf949e620af66275b2fa271046cc8ce6c1d2e9ab050e392c957a7452dcea1f7e38ec31c16c50527c845ff71e8ce9fcc38073be41b158c486e35b3a2','测试用户',0,'test@qq.com','119','110',0,'2012-11-06 16:52:07',NULL,NULL),
 (1,'admin','82e8f97c21f97ef47e49decd78f9c5030f980d5fad49e1fa03ac2446f97168f0966cb2b6983111c54a1bc6c70795b3c5711163072237638e29da68257bc4d927','超级管理员',0,'admin@qq.com.cn','119','110',0,'2012-11-07 15:52:04','127.0.0.1','用户信息');
/*!40000 ALTER TABLE `t_base_user` ENABLE KEYS */;


--
-- Definition of table `t_base_user_role`
--

DROP TABLE IF EXISTS `t_base_user_role`;
CREATE TABLE `t_base_user_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
  `user_id` int(10) unsigned DEFAULT NULL COMMENT '用户ID',
  `role_id` int(10) unsigned DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  KEY `FK_base_user_role_1` (`user_id`),
  KEY `FK_base_user_role_2` (`role_id`),
  CONSTRAINT `FK_base_user_role_1` FOREIGN KEY (`user_id`) REFERENCES `t_base_user` (`id`),
  CONSTRAINT `FK_base_user_role_2` FOREIGN KEY (`role_id`) REFERENCES `t_base_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户角色表';

--
-- Dumping data for table `t_base_user_role`
--

/*!40000 ALTER TABLE `t_base_user_role` DISABLE KEYS */;
INSERT INTO `t_base_user_role` (`id`,`user_id`,`role_id`) VALUES 
 (1,1,1),
 (2,2,2);
/*!40000 ALTER TABLE `t_base_user_role` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
