/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : nevs

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2018-09-26 00:57:34
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
  `shareholderReward` int(11) NOT NULL COMMENT '股东奖励积分',
  `shareholderCarReward` int(11) NOT NULL COMMENT '股东购车积分',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system
-- ----------------------------
DROP TABLE IF EXISTS `system`;
CREATE TABLE `system` (
  `id` varchar(100) NOT NULL COMMENT 'id',
  `companyProfile` longtext NOT NULL COMMENT '公司简介',
  `homePageImageUrl` varchar(255) NOT NULL COMMENT '首页图片轮播地址，多个,隔开',
  `companyAddress` varchar(255) NOT NULL COMMENT '公司地址',
  `contactNumber` varchar(30) NOT NULL COMMENT '联系电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `roleId` tinyint(1) NOT NULL COMMENT '角色，1：超级管理员，2：部门管理员，3：销售，4：财务，5：系统管理员，6：伪超级管理员，7：已离职，8：股东',
  `departmentId` tinyint(1) NOT NULL COMMENT '部门',
  `integral` int(11) NOT NULL DEFAULT '0' COMMENT '积分',
  `totalSales` int(11) NOT NULL DEFAULT '0' COMMENT '总销售量',
  `indirectSales` int(11) NOT NULL DEFAULT '0' COMMENT '间接销售量',
  `carIntegral` int(11) DEFAULT '0' COMMENT '购车的积分',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `deductionPrice` int(11) DEFAULT '0' COMMENT '抵扣车价',
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
