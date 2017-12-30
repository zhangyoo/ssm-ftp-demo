/*
Navicat MySQL Data Transfer

Source Server         : loclahost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : nfhy

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-12-29 16:13:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for nj_user
-- ----------------------------
DROP TABLE IF EXISTS `nj_user`;
CREATE TABLE `nj_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(40) NOT NULL,
  `password` varchar(255) NOT NULL,
  `age` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of nj_user
-- ----------------------------
INSERT INTO `nj_user` VALUES ('1', 'test', '123456', '24');
INSERT INTO `nj_user` VALUES ('2', '事务1', '123', '18');
INSERT INTO `nj_user` VALUES ('3', '事务2', '123', '18');
INSERT INTO `nj_user` VALUES ('4', '事务3', '123', '18');
INSERT INTO `nj_user` VALUES ('5', '事务4', '123', '18');
INSERT INTO `nj_user` VALUES ('6', '事务5', '123', '18');
INSERT INTO `nj_user` VALUES ('7', '事务7', '123', '18');
INSERT INTO `nj_user` VALUES ('8', '事务', '123', '18');
INSERT INTO `nj_user` VALUES ('9', '事务', '123', '18');

-- ----------------------------
-- Table structure for nj_user_ext
-- ----------------------------
DROP TABLE IF EXISTS `nj_user_ext`;
CREATE TABLE `nj_user_ext` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(40) NOT NULL,
  `user_run` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of nj_user_ext
-- ----------------------------
INSERT INTO `nj_user_ext` VALUES ('1', '1', 'eweweeww', '22');
INSERT INTO `nj_user_ext` VALUES ('2', '1', 'fdfsfsd', '33');

-- ----------------------------
-- Table structure for sys_authority
-- ----------------------------
DROP TABLE IF EXISTS `sys_authority`;
CREATE TABLE `sys_authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_url` varchar(100) NOT NULL COMMENT '连接路径或方法',
  `menu_class` varchar(50) NOT NULL COMMENT '菜单样式',
  `menu_code` varchar(50) NOT NULL COMMENT '菜单编码',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_menucode` varchar(50) DEFAULT NULL COMMENT '上级菜单编码',
  `sequence` bigint(20) DEFAULT '0' COMMENT '排序',
  `menu_type` varchar(2) DEFAULT '1' COMMENT '菜单类型(1是左导航菜单 2是按钮权限)',
  `create_time` varchar(30) NOT NULL COMMENT '创建时间',
  `update_time` varchar(30) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_authority_menu_code` (`menu_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_authority
-- ----------------------------
INSERT INTO `sys_authority` VALUES ('1', '#', '#', '100', '苏宁官网', '0', '0', '1', '2017-08-09 06:42:45', null);
INSERT INTO `sys_authority` VALUES ('2', '#', '#', '200', '柯莱特官网', '0', '0', '1', '2017-08-09 06:42:45', null);
INSERT INTO `sys_authority` VALUES ('3', '#', '#', '100100', '监控中心网站', '100', '0', '1', '2017-08-09 06:42:45', null);
INSERT INTO `sys_authority` VALUES ('4', '#', '#', '100100100', '增加', '100100', '0', '2', '2017-08-09 06:42:45', null);
INSERT INTO `sys_authority` VALUES ('5', '#', '#', '100100101', '更新', '100100', '0', '2', '2017-08-09 06:42:45', null);
INSERT INTO `sys_authority` VALUES ('6', '#', '#', '100100102', '删除', '100100', '0', '2', '2017-08-09 06:42:45', null);
INSERT INTO `sys_authority` VALUES ('7', '#', '#', '200100100', '增加', '200', '0', '2', '2017-08-09 06:42:45', null);

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `department_key` varchar(20) NOT NULL COMMENT '部门编码',
  `department_value` varchar(40) NOT NULL COMMENT '部门名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `parent_departmentkey` varchar(20) DEFAULT NULL COMMENT '上级部门编码',
  `create_time` varchar(30) NOT NULL COMMENT '创建时间',
  `update_time` varchar(30) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_department_department_key` (`department_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

-- ----------------------------
-- Records of sys_department
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_key` varchar(30) DEFAULT NULL COMMENT '角色编码',
  `create_time` varchar(30) NOT NULL COMMENT '创建时间',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `role_value` varchar(40) NOT NULL COMMENT '角色名称',
  `company_id` bigint(20) DEFAULT NULL,
  `update_time` varchar(30) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'ROLE_USER', '2017-08-09 06:42:45', null, '', null, null);
INSERT INTO `sys_role` VALUES ('2', 'ROLE_ADMIN', '2017-08-09 06:42:45', null, '', null, null);

-- ----------------------------
-- Table structure for sys_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_authority`;
CREATE TABLE `sys_role_authority` (
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `menu_code` varchar(50) NOT NULL COMMENT '菜单编码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单表';

-- ----------------------------
-- Records of sys_role_authority
-- ----------------------------
INSERT INTO `sys_role_authority` VALUES ('2', '100');
INSERT INTO `sys_role_authority` VALUES ('2', '100100');
INSERT INTO `sys_role_authority` VALUES ('1', '200');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `role_id` int(11) NOT NULL COMMENT '角色主键编号',
  `permission_menu_code` varchar(50) NOT NULL COMMENT '按钮权限(编码)',
  KEY `FK9q28ewrhntqeipl1t04kh1be7` (`role_id`),
  CONSTRAINT `FK9q28ewrhntqeipl1t04kh1be7` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`),
  CONSTRAINT `fk_sys_role_permission_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色按钮权限表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('2', '100100100');
INSERT INTO `sys_role_permission` VALUES ('2', '100100101');
INSERT INTO `sys_role_permission` VALUES ('1', '200100100');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_account` varchar(30) NOT NULL COMMENT '登录账号',
  `login_pass` varchar(65) NOT NULL COMMENT '登录密码',
  `user_name` varchar(20) DEFAULT NULL COMMENT '昵称',
  `user_head` varchar(30) DEFAULT NULL COMMENT '头像',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '手机',
  `user_email` varchar(30) DEFAULT NULL COMMENT '邮箱',
  `user_sex` int(11) DEFAULT NULL COMMENT '性别',
  `user_birthday` varchar(30) DEFAULT NULL COMMENT '生日',
  `create_time` varchar(30) NOT NULL COMMENT '创建时间',
  `department_key` varchar(20) DEFAULT NULL COMMENT '部门编码',
  `update_time` varchar(30) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_sys_user_login_account` (`login_account`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('2', 'admin', 'e10adc3949ba59abbe56e057f20f883e', null, null, null, 'hzw2312@sina.com', null, null, '2017-01-18 14:39:23', null, null);
INSERT INTO `sys_user` VALUES ('3', 'zhangyong', 'e10adc3949ba59abbe56e057f20f883e', null, null, null, 'hzw23d12@sina.com', null, null, '2017-01-18 15:25:08', null, null);
INSERT INTO `sys_user` VALUES ('4', 'algo', 'e10adc3949ba59abbe56e057f20f883e', null, null, null, 'hhs2312@sina.com', null, null, '2017-01-18 15:25:47', null, null);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `role_id` int(20) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKhh52n8vd4ny9ff4x9fb8v65qx` (`role_id`),
  CONSTRAINT `FKb40xxfch70f5qnyfw8yme1n1s` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`),
  CONSTRAINT `FKhh52n8vd4ny9ff4x9fb8v65qx` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`),
  CONSTRAINT `fk_sys_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`),
  CONSTRAINT `fk_sys_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色映射表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('3', '1');
INSERT INTO `sys_user_role` VALUES ('4', '1');
INSERT INTO `sys_user_role` VALUES ('2', '2');

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `testa` varchar(255) DEFAULT NULL,
  `testb` varchar(255) DEFAULT NULL,
  `ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO `test` VALUES ('A', 'B', '1');
INSERT INTO `test` VALUES ('C', 'D', '2');
INSERT INTO `test` VALUES ('E', 'F', '3');
INSERT INTO `test` VALUES ('A', 'D', '4');
INSERT INTO `test` VALUES ('E', 'C', '5');
INSERT INTO `test` VALUES ('E', 'B', '6');
