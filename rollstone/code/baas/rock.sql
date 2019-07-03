/*
Navicat MySQL Data Transfer

Source Server         : 172.16.201.189
Source Server Version : 50633
Source Host           : 172.16.201.189:3306
Source Database       : rock

Target Server Type    : MYSQL
Target Server Version : 50633
File Encoding         : 65001

Date: 2018-10-18 15:21:17
*/

CREATE USER 'developer'@'%' IDENTIFIED WITH mysql_native_password BY 'szyz';

GRANT SELECT, INSERT, UPDATE, DELETE ON *.* TO 'developer'@'%';
FLUSH PRIVILEGES;

create database rock default character set utf8 COLLATE utf8_general_ci;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for company_info
-- ----------------------------
DROP TABLE IF EXISTS `company_info`;
CREATE TABLE `company_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `apt` varchar(100) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0' COMMENT '0weirenzheng 1renzheng 2shixiao',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for item_info
-- ----------------------------
DROP TABLE IF EXISTS `item_info`;
CREATE TABLE `item_info` (
  `id` varchar(40) NOT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  `type` tinyint(2) DEFAULT NULL,
  `title` varchar(20) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `path` varchar(100) DEFAULT NULL,
  `tid` varchar(40) DEFAULT NULL,
  `uperid` varchar(20) DEFAULT NULL,
  `xhash` varchar(40) DEFAULT NULL,
  `shash` varchar(40) DEFAULT NULL,
  `ihash` varchar(40) DEFAULT NULL,
  `cipher` varchar(20) DEFAULT NULL,
  `ikey` varchar(20) DEFAULT NULL,
  `iopen` tinyint(1) DEFAULT NULL,
  `level` tinyint(1) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for perm_info
-- ----------------------------
DROP TABLE IF EXISTS `perm_info`;
CREATE TABLE `perm_info` (
  `id` varchar(40) NOT NULL,
  `att` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_tbl
-- ----------------------------
DROP TABLE IF EXISTS `user_tbl`;
CREATE TABLE `user_tbl` (
  `user_id` bigint(15) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(16) DEFAULT NULL,
  `pwd` varchar(60) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `real_name` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `idcard` varchar(18) DEFAULT NULL,
  `idcard_photo` varchar(100) DEFAULT NULL,
  `company_id` int(10) DEFAULT NULL,
  `company_name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `company_apt` varchar(100) DEFAULT NULL,
  `addr` varchar(100) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `level` tinyint(2) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0' COMMENT '0 weirenzhen 1shenhezhong 2tongguo 3weitongguo',
  `hash` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ethaddr` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `onlyusername` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_temp_info
-- ----------------------------
DROP TABLE IF EXISTS `user_temp_info`;
CREATE TABLE `user_temp_info` (
  `user_id` bigint(15) NOT NULL,
  `user_name` varchar(16) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `real_name` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `idcard` varchar(18) DEFAULT NULL,
  `idcard_photo` varchar(100) DEFAULT NULL,
  `company_id` int(10) DEFAULT NULL,
  `company_name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `company_apt` varchar(100) DEFAULT NULL,
  `addr` varchar(100) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `level` tinyint(2) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0' COMMENT '0renzheng 1update 3weitongguo',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `onlyusername` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vaild_code
-- ----------------------------
DROP TABLE IF EXISTS `vaild_code`;
CREATE TABLE `vaild_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sms` varchar(40) DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
