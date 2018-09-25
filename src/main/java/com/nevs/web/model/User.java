package com.nevs.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author YETA
 * 用户实体
 * @date 2018/08/26/14:13
 */
@Entity
public class User {

    /**
     * 用户id，同时也是自己的邀请码
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    @Column(name = "password")
    private String password;

    /**
     * 邀请码
     */
    @JsonIgnore
    @Column(name = "invitationCode")
    private String invitationCode;

    /**
     * 邀请人
     */
    @Column(name = "inviter")
    private String inviter;

    /**
     * 邀请人的邀请码
     */
    @JsonIgnore
    @Column(name = "invitationCodeOfInviter")
    private String invitationCodeOfInviter;

    /**
     * 邀请人的邀请人
     */
    @Column(name = "inviterOfInviter")
    private String inviterOfInviter;

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 身份证号码
     */
    @Column(name = "idCardNo")
    private String idCardNo;

    /**
     * 照片路径1
     */
    @Column(name = "imageUrl1")
    private String imageUrl1;

    /**
     * 照片路径2
     */
    @Column(name = "imageUrl2")
    private String imageUrl2;

    /**
     * 银行卡号
     */
    @Column(name = "creditCardNo")
    private String creditCardNo;

    /**
     * 开户银行
     */
    @Column(name = "bankOfDeposit")
    private String bankOfDeposit;

    /**
     * 手机号码
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 角色id
     */
    @Column(name = "roleId")
    private Integer roleId;

    /**
     * 部门id
     */
    @Column(name = "departmentId")
    private Integer departmentId;

    /**
     * 积分
     */
    @Column(name = "integral")
    private Integer integral;

    /**
     * 购车积分
     */
    @Column(name = "carIntegral")
    private Integer carIntegral;

    /**
     * 总销售量
     */
    @Column(name = "totalSales")
    private Integer totalSales;

    /**
     * 间接销售量
     */
    @Column(name = "indirectSales")
    private Integer indirectSales;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getInvitationCodeOfInviter() {
        return invitationCodeOfInviter;
    }

    public void setInvitationCodeOfInviter(String invitationCodeOfInviter) {
        this.invitationCodeOfInviter = invitationCodeOfInviter;
    }

    public String getInviterOfInviter() {
        return inviterOfInviter;
    }

    public void setInviterOfInviter(String inviterOfInviter) {
        this.inviterOfInviter = inviterOfInviter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getBankOfDeposit() {
        return bankOfDeposit;
    }

    public void setBankOfDeposit(String bankOfDeposit) {
        this.bankOfDeposit = bankOfDeposit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getCarIntegral() {
        return carIntegral;
    }

    public void setCarIntegral(Integer carIntegral) {
        this.carIntegral = carIntegral;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public Integer getIndirectSales() {
        return indirectSales;
    }

    public void setIndirectSales(Integer indirectSales) {
        this.indirectSales = indirectSales;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", invitationCode='" + invitationCode + '\'' +
                ", inviter='" + inviter + '\'' +
                ", invitationCodeOfInviter='" + invitationCodeOfInviter + '\'' +
                ", inviterOfInviter='" + inviterOfInviter + '\'' +
                ", name='" + name + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", imageUrl1='" + imageUrl1 + '\'' +
                ", imageUrl2='" + imageUrl2 + '\'' +
                ", creditCardNo='" + creditCardNo + '\'' +
                ", bankOfDeposit='" + bankOfDeposit + '\'' +
                ", phone='" + phone + '\'' +
                ", roleId=" + roleId +
                ", departmentId=" + departmentId +
                ", integral=" + integral +
                ", carIntegral=" + carIntegral +
                ", totalSales=" + totalSales +
                ", indirectSales=" + indirectSales +
                '}';
    }
}
