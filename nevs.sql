/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : nevs

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2018-09-09 19:52:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for award
-- ----------------------------
DROP TABLE IF EXISTS `award`;
CREATE TABLE `award` (
  `id` varchar(100) NOT NULL COMMENT 'id',
  `levelOfReward` int(11) NOT NULL COMMENT '一级奖励',
  `secondaryReward` int(11) NOT NULL COMMENT '二级奖励',
  `managerReward` int(11) NOT NULL COMMENT '部门管理员奖励',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of award
-- ----------------------------
INSERT INTO `award` VALUES ('9e2b6707-b35b-11e8-ae37-54ee75c0f47a', '3000', '1000', '1000');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '部门名',
  `managerId` varchar(100) NOT NULL COMMENT '部门管理员id',
  `managerName` varchar(20) NOT NULL COMMENT '部门管理员姓名',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `totalSales` int(11) NOT NULL DEFAULT '0' COMMENT '总销售量',
  `quarterlySales` int(11) NOT NULL DEFAULT '0' COMMENT '季度销售量',
  `lastLiquidationTime` datetime NOT NULL COMMENT '上一次清算时间',
  `flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '卖的多标志位，0：不直接打积分，1：直接打积分',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('0', '未分配部门', 'd1705345-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', '2018-09-01 15:50:30', '0', '0', '2018-09-01 15:50:30', '0');
INSERT INTO `department` VALUES ('1', '管理部门', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', '2018-09-01 15:50:37', '0', '0', '2018-09-01 15:30:37', '0');
INSERT INTO `department` VALUES ('2', '销售部1', 'c84980d3-16ce-4e6a-8d4f-55d372c7f3d6', 'yasuo', '2018-09-08 13:17:11', '0', '0', '2018-09-08 13:17:11', '0');
INSERT INTO `department` VALUES ('3', '销售部2', '71407266-c8ac-4684-bbd8-bb94003ecc39', 'test1', '2018-09-09 17:54:38', '0', '0', '2018-09-09 17:54:38', '0');

-- ----------------------------
-- Table structure for exception
-- ----------------------------
DROP TABLE IF EXISTS `exception`;
CREATE TABLE `exception` (
  `id` varchar(100) NOT NULL COMMENT 'id',
  `type` varchar(255) NOT NULL COMMENT '异常类型',
  `message` text NOT NULL COMMENT '具体信息',
  `time` datetime NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exception
-- ----------------------------
INSERT INTO `exception` VALUES ('63420f7f-2905-4248-aa1b-3cf980705523', 'org.springframework.web.bind.MissingServletRequestParameterException', 'Required String parameter \'userId\' is not present', '2018-09-08 09:53:28');
INSERT INTO `exception` VALUES ('68eeaef5-7a5d-4cbf-a15d-80daeb0b7739', 'org.springframework.dao.DataIntegrityViolationException', 'could not execute statement; SQL [n/a]; constraint [PRIMARY]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement', '2018-09-09 17:54:22');
INSERT INTO `exception` VALUES ('804bb7a0-8468-4969-b145-23d422b76e34', '部门管理员奖励积分', '开始', '2018-09-08 23:13:15');
INSERT INTO `exception` VALUES ('c00452f2-771e-4f10-ab41-ae54b394245d', '部门管理员奖励积分', '结束', '2018-09-08 23:17:12');

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence` VALUES ('4');

-- ----------------------------
-- Table structure for integraltrading
-- ----------------------------
DROP TABLE IF EXISTS `integraltrading`;
CREATE TABLE `integraltrading` (
  `id` varchar(100) NOT NULL COMMENT '积分交易id',
  `userId` varchar(100) NOT NULL COMMENT '积分改变的销售id',
  `userName` varchar(20) NOT NULL COMMENT '用户姓名',
  `departmentId` int(11) NOT NULL COMMENT '部门id',
  `departmentName` varchar(20) NOT NULL COMMENT '部门名',
  `changeWay` tinyint(1) NOT NULL COMMENT '积分变化方式，1：下单增加，2：提现减少',
  `changeIntegral` int(11) NOT NULL COMMENT '变化积分',
  `auditor` varchar(50) DEFAULT NULL COMMENT '审核人',
  `auditPassTime` datetime DEFAULT NULL COMMENT '审核通过时间',
  `orderNo` varchar(100) DEFAULT NULL COMMENT '订单号',
  `relevantIntegralTradingId` varchar(100) DEFAULT NULL COMMENT '相关积分交易id',
  `withdrawTime` datetime DEFAULT NULL COMMENT '提现时间',
  `withdrawStatus` tinyint(1) DEFAULT NULL COMMENT '提现状态，1：待审核，2：审核通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of integraltrading
-- ----------------------------

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` varchar(100) NOT NULL COMMENT 'id',
  `action` tinyint(1) NOT NULL COMMENT '操作，1：登录，2：注销，3：创建部门，4：修改部门，5：修改奖励数目，6：修改用户资料，7：下载用户信息',
  `detail` varchar(255) DEFAULT NULL COMMENT '细节',
  `time` datetime NOT NULL COMMENT '时间',
  `creator` varchar(20) NOT NULL COMMENT '人物',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log
-- ----------------------------
INSERT INTO `log` VALUES ('2d7ac13d-f8d4-43be-9fb9-12aa39974ad2', '5', '部门管理员奖励由：100修改为：1000', '2018-09-08 23:02:16', '超级管理员');
INSERT INTO `log` VALUES ('59758120-a985-4215-8348-5cbd115ae2a4', '5', '二级奖励由：100修改为：1000', '2018-09-08 23:02:16', '超级管理员');
INSERT INTO `log` VALUES ('60e7664f-b143-4b78-a9d8-297823cbdb24', '5', '一级奖励由：300修改为：3000', '2018-09-08 23:02:16', '超级管理员');
INSERT INTO `log` VALUES ('63c039d6-8f25-40aa-8052-14954c100757', '3', '部门名：销售部2，部门管理员：test1', '2018-09-09 17:54:38', '超级管理员');
INSERT INTO `log` VALUES ('b3da6b9e-d65b-45af-ac2d-58929f9fd75b', '5', '二级奖励由：1000修改为：100', '2018-09-08 19:48:49', '超级管理员');
INSERT INTO `log` VALUES ('b9a054d8-4ec6-4531-b4e5-13600503d51a', '5', '一级奖励由：3000修改为：300', '2018-09-08 19:48:47', '超级管理员');
INSERT INTO `log` VALUES ('c28778fd-6a51-4865-ac09-e7c593eaa8be', '5', '部门管理员奖励由：1000修改为：100', '2018-09-08 19:48:49', '超级管理员');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` varchar(100) NOT NULL COMMENT 'id',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `content` varchar(255) NOT NULL COMMENT '内容',
  `announceTime` datetime NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of notice
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '超级管理员');
INSERT INTO `role` VALUES ('2', '部门管理员');
INSERT INTO `role` VALUES ('3', '销售');
INSERT INTO `role` VALUES ('4', '财务');
INSERT INTO `role` VALUES ('5', '系统管理员');
INSERT INTO `role` VALUES ('6', '伪超级管理员');
INSERT INTO `role` VALUES ('7', '已离职');

-- ----------------------------
-- Table structure for system
-- ----------------------------
DROP TABLE IF EXISTS `system`;
CREATE TABLE `system` (
  `id` varchar(100) NOT NULL COMMENT 'id',
  `companyProfile` varchar(255) NOT NULL COMMENT '公司简介',
  `homePageImageUrl` varchar(255) NOT NULL COMMENT '首页图片轮播地址，多个,隔开',
  `companyAddress` varchar(255) NOT NULL COMMENT '公司地址',
  `contactNumber` varchar(30) NOT NULL COMMENT '联系电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system
-- ----------------------------
INSERT INTO `system` VALUES ('0bd2adf7-af5c-11e8-a062-9615c1985358', '公司简介一般包括以下几个方面： 1. 公司概况：这里面可以包括注册时间，注册资本，公司性质，技术力量，规模，员工人数，员工素质等； 2. 公司发展状况：公司的发展速度，有何成绩，有何荣誉称号等； 3. 公司文化：公司的目标，理念，宗旨，使命，愿景，寄语等； 4. 公司主要产品：性能，特色，创新，超前； 5. 销售业绩及网络：销售量，各地销售点等； 6. 售后服务：主要是公司售后服务的承诺。', '/upload/system/1536164334182_67039391.jpg', '成都市武侯区天府大道北段1700号', '12345678987');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(100) NOT NULL COMMENT '用户id，同时也是自己的邀请码',
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `invitationCode` varchar(100) NOT NULL COMMENT '注册时填写的别人的邀请码',
  `inviter` varchar(20) NOT NULL COMMENT '邀请人',
  `invitationCodeOfInviter` varchar(100) NOT NULL COMMENT '邀请人的邀请码',
  `inviterOfInviter` varchar(20) NOT NULL COMMENT '邀请人的邀请人',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `idCardNo` varchar(50) DEFAULT NULL COMMENT '身份证号码',
  `imageUrl1` varchar(255) DEFAULT NULL COMMENT '照片路径1',
  `imageUrl2` varchar(255) DEFAULT NULL COMMENT '照片路径2',
  `creditCardNo` varchar(50) DEFAULT NULL COMMENT '银行卡号码',
  `bankOfDeposit` varchar(255) DEFAULT NULL COMMENT '开户银行',
  `phone` varchar(30) DEFAULT NULL COMMENT '手机号码',
  `roleId` tinyint(1) NOT NULL COMMENT '角色，1：超级管理员，2：部门管理员，3：销售，4：财务，5：系统管理员',
  `departmentId` tinyint(1) NOT NULL COMMENT '部门',
  `integral` int(11) NOT NULL DEFAULT '0' COMMENT '积分',
  `totalSales` int(11) NOT NULL DEFAULT '0' COMMENT '总销售量',
  `indirectSales` int(11) NOT NULL DEFAULT '0' COMMENT '间接销售量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('50002aae-fd69-40b5-be9d-fceba690b2bb', 'liqing', 'liqing', 'cf331466-f49f-416c-8208-e6209cf86980', 'ruiwen', 'c84980d3-16ce-4e6a-8d4f-55d372c7f3d6', 'yasuo', 'liqing', null, null, null, null, null, null, '3', '2', '0', '0', '0');
INSERT INTO `user` VALUES ('71407266-c8ac-4684-bbd8-bb94003ecc39', 'test1', 'test1', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', 'test1', null, null, null, null, null, null, '2', '3', '0', '0', '0');
INSERT INTO `user` VALUES ('ade77a31-7b0b-48d3-832c-0c4872b27fb4', 'fakeSuperAdmin', 'fakeSuperAdmin', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', '伪超级管理员', null, null, null, null, null, null, '6', '1', '0', '0', '0');
INSERT INTO `user` VALUES ('c84980d3-16ce-4e6a-8d4f-55d372c7f3d6', 'yasuo', 'yasuo', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', 'yasuo', null, null, null, null, null, null, '3', '2', '0', '0', '0');
INSERT INTO `user` VALUES ('cf331466-f49f-416c-8208-e6209cf86980', 'ruiwen', 'ruiwen', 'c84980d3-16ce-4e6a-8d4f-55d372c7f3d6', 'yasuo', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', 'ruiwen', null, null, null, null, null, null, '3', '2', '0', '0', '0');
INSERT INTO `user` VALUES ('d1705310-adb8-11e8-b58c-54ee75c0f47a', 'superAdmin', 'superAdmin', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', '超级管理员', null, null, null, null, null, null, '1', '1', '0', '0', '0');
INSERT INTO `user` VALUES ('d1743563-adb8-11e8-b58c-54ee75c0f47a', 'finance1', 'finance1', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', '财务1', null, null, null, null, null, null, '4', '1', '0', '0', '0');
INSERT INTO `user` VALUES ('d1781289-adb8-11e8-b58c-54ee75c0f47a', 'systemAdmin', 'systemAdmin', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', 'd1705310-adb8-11e8-b58c-54ee75c0f47a', '超级管理员', '系统管理员', null, null, null, null, null, null, '5', '1', '0', '0', '0');

-- ----------------------------
-- Table structure for vehicle
-- ----------------------------
DROP TABLE IF EXISTS `vehicle`;
CREATE TABLE `vehicle` (
  `id` varchar(100) NOT NULL COMMENT '车辆id',
  `name` varchar(20) NOT NULL COMMENT '名字',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `subscription` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订金',
  `quantityOfSale` int(11) DEFAULT '0' COMMENT '销售量',
  `imageUrl` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `store` tinyint(1) NOT NULL COMMENT '是否有库存，1：有，0：没有',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vehicle
-- ----------------------------
INSERT INTO `vehicle` VALUES ('691eb1e6-bfc8-4f54-a8d1-4bbd54f66214', '小车车1', '小车车1', '200000.00', '10000.00', '0', '/upload/vehicle/example.jpg', '1');

-- ----------------------------
-- Table structure for vehicleorder
-- ----------------------------
DROP TABLE IF EXISTS `vehicleorder`;
CREATE TABLE `vehicleorder` (
  `orderNo` varchar(100) NOT NULL COMMENT '订单号',
  `orderTime` datetime NOT NULL COMMENT '订单时间',
  `invoiceName` varchar(50) NOT NULL COMMENT '开发票名称',
  `identity` tinyint(1) NOT NULL COMMENT '身份，1：个人，2：公司，3：单位',
  `department` varchar(50) NOT NULL COMMENT '单位',
  `idCardNo` varchar(20) NOT NULL COMMENT '身份证号',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `tel` varchar(20) NOT NULL COMMENT 'tel',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `gender` varchar(1) DEFAULT NULL COMMENT '性别',
  `profession` varchar(20) DEFAULT NULL COMMENT '职业',
  `eMail` varchar(20) DEFAULT NULL COMMENT 'e-mail',
  `vehicleName` varchar(50) NOT NULL COMMENT '车名',
  `version` varchar(20) NOT NULL COMMENT '型号',
  `inColor` varchar(10) NOT NULL COMMENT '颜色内',
  `outColor` varchar(10) NOT NULL COMMENT '颜色外',
  `canChangeColor` tinyint(1) NOT NULL COMMENT '是否同意更换颜色，1：同意，0：不同意',
  `quantity` int(11) NOT NULL COMMENT '数量（台）',
  `price` decimal(10,2) NOT NULL COMMENT '车价（元）',
  `paymentMethod` tinyint(1) NOT NULL COMMENT '付款方式。1：现金，2：支票，3：转账',
  `cashOrInstallment` tinyint(1) NOT NULL COMMENT '现款或按揭，1：现款，0：按揭',
  `cashSubscription` decimal(10,2) DEFAULT NULL COMMENT '现款订金',
  `cashSpareMoney` decimal(10,2) DEFAULT NULL COMMENT '现款余款',
  `installmentDownPayment` decimal(10,2) DEFAULT NULL COMMENT '按揭首付',
  `installmentLoanAmount` decimal(10,2) DEFAULT NULL COMMENT '按揭贷款金额',
  `giveVehicleDate` datetime NOT NULL COMMENT '预计交车日期',
  `vehicleDamageInsurance` varchar(200) DEFAULT NULL COMMENT '车辆损失险',
  `thirdPartyLiabilityInsurance` varchar(200) DEFAULT NULL COMMENT '第三者责任险',
  `totalVehicleTheftInsurance` varchar(200) DEFAULT NULL COMMENT '全车盗抢险',
  `vehiclePersonnelLiabilityInsurance` varchar(200) DEFAULT NULL COMMENT '车上人员责任险',
  `noLiabilityInsurance` varchar(200) DEFAULT NULL COMMENT '无过失责任险',
  `riskOfGlassBreakageInsurance` varchar(200) DEFAULT NULL COMMENT '玻璃单独破碎险',
  `spontaneousCombustionLossesInsurance` varchar(200) DEFAULT NULL COMMENT '自燃损失险',
  `nonDeductibleSpecialRiskInsurance` varchar(200) DEFAULT NULL COMMENT '不计免赔特约险',
  `otherInsurance` varchar(200) DEFAULT NULL COMMENT '其他险',
  `insuranceEstimate` varchar(200) DEFAULT NULL COMMENT '保险费预估',
  `insuranceCompanyName` varchar(50) DEFAULT NULL COMMENT '保险公司',
  `fineDetail` varchar(200) DEFAULT NULL COMMENT '精品明细',
  `reviewerComment` varchar(200) NOT NULL COMMENT '评审意见',
  `subscriptionConfirmationAmountLower` varchar(200) NOT NULL COMMENT '订购金确认金额小写',
  `subscriptionConfirmationAmountUpper` varchar(200) NOT NULL COMMENT '订购金确认金额大写',
  `subscriptionConfirmationArrivalDate` datetime NOT NULL COMMENT '订购金确认到账日',
  `salesConsultant` varchar(20) NOT NULL COMMENT '销售顾问/tel',
  `salesDirector` varchar(20) NOT NULL COMMENT '销售总监',
  `salesId` varchar(100) NOT NULL COMMENT '销售id',
  `departmentId` int(11) NOT NULL COMMENT '部门id',
  `vehicleId` varchar(100) NOT NULL COMMENT '车辆id',
  `orderStatus` tinyint(1) NOT NULL COMMENT '订单状态，0：待审核，1：审核通过，2：审核未通过',
  `orderExpire` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否到期，0：未过期，1：即将过期，2：已过期',
  `orderImageUrl` varchar(200) NOT NULL COMMENT '订单图片地址',
  `reasonOfCanNotPass` varchar(255) DEFAULT NULL COMMENT '订单审核不通过原因',
  PRIMARY KEY (`orderNo`),
  KEY `FKff3gnuw9g3cb78jknbcqjsq1v` (`salesId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of vehicleorder
-- ----------------------------

-- ----------------------------
-- Procedure structure for myProc
-- ----------------------------
DROP PROCEDURE IF EXISTS `myProc`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `myProc`()
BEGIN
    DECLARE a, b INT;
    SET a=60000, b = 0;
    WHILE b < 110000 DO
      INSERT INTO `vehicleorder` VALUES (a, '2018-08-27 00:00:00', '开发票名称', '1', '欧米伽小队', '510921199505253276', '四川省成都市', '12398719823798172', '1995-05-25 00:00:00', '男', '个体经营', '429721743@qq.com', '小车车', '经典款', '黑色', '绿色', '1', '1', '200000.00', '1', '1', '10000.00', '190000.00', '0.00', '0.00', '2018-09-10 00:00:00', null, null, null, null, null, null, null, null, null, null, null, null, '统一', '壹万元整', '一万元整', '2018-09-01 00:00:00', '李青', '亚索', '50002aae-fd69-40b5-be9d-fceba690b2bb', '0', '691eb1e6-bfc8-4f54-a8d1-4bbd54f66214', '0', '1', '/upload/order/1535959023317_-1259354224.jpeg', null);
      SET b = b + 1;
      set a = a + 1;
    END WHILE;
  END
;;
DELIMITER ;
