package com.nevs.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author YETA
 * 积分交易实体
 * @date 2018/08/27/16:22
 */
@Entity
@Table(name = "integralTrading")
public class IntegralTrading {

    /**
     * 积分交易id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 积分改变的用户id
     */
    @Column(name = "userId")
    private String userId;

    /**
     * 积分变化的用户姓名
     */
    @Column(name = "userName")
    private String userName;

    /**
     * 积分变化的用户的部门id
     */
    @Column(name = "departmentId")
    private Integer departmentId;

    /**
     * 积分变化的用户的部门名
     */
    @Column(name = "departmentName")
    private String departmentName;

    /**
     * 变化方式，1：下单增加，0：提现减少
     */
    @Column(name = "changeWay")
    private Integer changeWay;

    /**
     * 变化积分
     */
    @Column(name = "changeIntegral")
    private Integer changeIntegral;

    /**
     * 审核人
     */
    @Column(name = "auditor")
    private String auditor;

    /**
     * 审核通过时间
     */
    @Column(name = "auditPassTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditPassTime;

    /**
     * 订单号
     */
    @Column(name = "orderNo")
    private String orderNo;

    /**
     * 相关积分交易id
     */
    @Column(name = "relevantIntegralTradingId")
    private String relevantIntegralTradingId;

    /**
     * 提现时间
     */
    @Column(name = "withdrawTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date withdrawTime;

    /**
     * 提现状态，1：待审核，2：审核通过
     */
    @Column(name = "withdrawStatus")
    private Integer withdrawStatus;

    public IntegralTrading() {
    }

    //用于审核订单成功-增加销售的邀请人的积分交易
    public IntegralTrading(String id, String userId, String userName, Integer departmentId, String departmentName, Integer changeWay, Integer changeIntegral, String auditor, Date auditPassTime, String orderNo) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.changeWay = changeWay;
        this.changeIntegral = changeIntegral;
        this.auditor = auditor;
        this.auditPassTime = auditPassTime;
        this.orderNo = orderNo;
    }

    //用于审核订单成功-增加销售的邀请人的邀请人的积分交易
    public IntegralTrading(String id, String userId, String userName, Integer departmentId, String departmentName, Integer changeWay, Integer changeIntegral, String auditor, Date auditPassTime, String orderNo, String relevantIntegralTradingId) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.changeWay = changeWay;
        this.changeIntegral = changeIntegral;
        this.auditor = auditor;
        this.auditPassTime = auditPassTime;
        this.orderNo = orderNo;
        this.relevantIntegralTradingId = relevantIntegralTradingId;
    }

    //用于积分提现
    public IntegralTrading(String id, String userId, String userName, Integer departmentId, String departmentName, Integer changeWay, Integer changeIntegral, Date withdrawTime, Integer withdrawStatus) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.changeWay = changeWay;
        this.changeIntegral = changeIntegral;
        this.withdrawTime = withdrawTime;
        this.withdrawStatus = withdrawStatus;
    }

    //用于部门销售量奖励
    public IntegralTrading(String id, String userId, String userName, Integer departmentId, String departmentName, Integer changeWay, Integer changeIntegral, String auditor, Date auditPassTime) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.changeWay = changeWay;
        this.changeIntegral = changeIntegral;
        this.auditor = auditor;
        this.auditPassTime = auditPassTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getChangeWay() {
        return changeWay;
    }

    public void setChangeWay(Integer changeWay) {
        this.changeWay = changeWay;
    }

    public Integer getChangeIntegral() {
        return changeIntegral;
    }

    public void setChangeIntegral(Integer changeIntegral) {
        this.changeIntegral = changeIntegral;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Date getAuditPassTime() {
        return auditPassTime;
    }

    public void setAuditPassTime(Date auditPassTime) {
        this.auditPassTime = auditPassTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRelevantIntegralTradingId() {
        return relevantIntegralTradingId;
    }

    public void setRelevantIntegralTradingId(String relevantIntegralTradingId) {
        this.relevantIntegralTradingId = relevantIntegralTradingId;
    }

    public Date getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(Date withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public Integer getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Integer withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    @Override
    public String toString() {
        return "IntegralTrading{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", changeWay=" + changeWay +
                ", changeIntegral=" + changeIntegral +
                ", auditor='" + auditor + '\'' +
                ", auditPassTime=" + auditPassTime +
                ", orderNo='" + orderNo + '\'' +
                ", relevantIntegralTradingId='" + relevantIntegralTradingId + '\'' +
                ", withdrawTime=" + withdrawTime +
                ", withdrawStatus=" + withdrawStatus +
                '}';
    }
}
