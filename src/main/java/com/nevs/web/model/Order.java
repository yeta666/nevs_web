package com.nevs.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author YETA
 * 订单实体
 * @date 2018/08/27/11:11
 */
@Entity
@Table(name = "vehicleOrder")
public class Order {

    /**
     * 订单号
     */
    @Id
    @Column(name = "orderNo")
    private String orderNo;

    /**
     * 订单时间
     */
    @Column(name = "orderTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderTime;

    /**
     * 开发票名称
     */
    @Column(name = "invoiceName")
    private String invoiceName;

    /**
     * 身份，1：个人，2：公司，3：单位
     */
    @Column(name = "identity")
    private Integer identity;

    /**
     * 单位
     */
    @Column(name = "department")
    private String department;

    /**
     * 身份证号
     */
    @Column(name = "idCardNo")
    private String idCardNo;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * tel
     */
    @Column(name = "tel")
    private String tel;

    /**
     * 生日
     */
    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别
     */
    @Column(name = "gender")
    private String gender;

    /**
     * 职业
     */
    @Column(name = "profession")
    private String profession;

    /**
     * e-mail
     */
    @Column(name = "eMail")
    private String eMail;

    /**
     * 车名
     */
    @Column(name = "vehicleName")
    private String vehicleName;

    /**
     * 型号
     */
    @Column(name = "version")
    private String version;

    /**
     * 颜色内
     */
    @Column(name = "inColor")
    private String inColor;

    /**
     * 颜色外
     */
    @Column(name = "outColor")
    private String outColor;

    /**
     * 是否同意更换颜色，1：同意，0：不同意
     */
    @Column(name = "canChangeColor")
    private Integer canChangeColor;

    /**
     * 数量（台）
     */
    @Column(name = "quantity")
    private Integer quantity;

    /**
     * 车价（元）
     */
    @Column(name = "price")
    private Double price;

    /**
     * 抵扣车价
     */
    @Column(name = "deductionPrice")
    private Integer deductionPrice;

    /**
     * 付款方式。1：现金，2：支票，3：转账
     */
    @Column(name = "paymentMethod")
    private Integer paymentMethod;

    /**
     * 现款或按揭，1：现款，0：按揭
     */
    @Column(name = "cashOrInstallment")
    private Integer cashOrInstallment;

    /**
     * 现款订金
     */
    @Column(name = "cashSubscription")
    private Double cashSubscription;

    /**
     * 现款余款
     */
    @Column(name = "cashSpareMoney")
    private Double cashSpareMoney;

    /**
     * 按揭首付
     */
    @Column(name = "installmentDownPayment")
    private Double installmentDownPayment;

    /**
     * 按揭贷款金额
     */
    @Column(name = "installmentLoanAmount")
    private Double installmentLoanAmount;

    /**
     * 预计交车日期
     */
    @Column(name = "giveVehicleDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date giveVehicleDate;

    /**
     * 车辆损失险
     */
    @Column(name = "vehicleDamageInsurance")
    private String vehicleDamageInsurance;

    /**
     * 第三者责任险
     */
    @Column(name = "thirdPartyLiabilityInsurance")
    private String thirdPartyLiabilityInsurance;

    /**
     * 全车盗抢险
     */
    @Column(name = "totalVehicleTheftInsurance")
    private String totalVehicleTheftInsurance;

    /**
     * 车上人员责任险
     */
    @Column(name = "vehiclePersonnelLiabilityInsurance")
    private String vehiclePersonnelLiabilityInsurance;

    /**
     * 无过失责任险
     */
    @Column(name = "noLiabilityInsurance")
    private String noLiabilityInsurance;

    /**
     * 玻璃单独破碎险
     */
    @Column(name = "riskOfGlassBreakageInsurance")
    private String riskOfGlassBreakageInsurance;

    /**
     * 自燃损失险
     */
    @Column(name = "spontaneousCombustionLossesInsurance")
    private String spontaneousCombustionLossesInsurance;

    /**
     * 不计免赔特约险
     */
    @Column(name = "nonDeductibleSpecialRiskInsurance")
    private String nonDeductibleSpecialRiskInsurance;

    /**
     * 其他险
     */
    @Column(name = "otherInsurance")
    private String otherInsurance;

    /**
     * 保险费预估
     */
    @Column(name = "insuranceEstimate")
    private String insuranceEstimate;

    /**
     * 保险公司
     */
    @Column(name = "insuranceCompanyName")
    private String insuranceCompanyName;

    /**
     * 精品明细
     */
    @Column(name = "fineDetail")
    private String fineDetail;

    /**
     * 评审意见
     */
    @Column(name = "reviewerComment")
    private String reviewerComment;

    /**
     * 订购金确认金额小写
     */
    @Column(name = "subscriptionConfirmationAmountLower")
    private String subscriptionConfirmationAmountLower;

    /**
     * 订购金确认金额大写
     */
    @Column(name = "subscriptionConfirmationAmountUpper")
    private String subscriptionConfirmationAmountUpper;

    /**
     * 订购金确认到账日
     */
    @Column(name = "subscriptionConfirmationArrivalDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date subscriptionConfirmationArrivalDate;

    /**
     * 销售顾问/tel
     */
    @Column(name = "salesConsultant")
    private String salesConsultant;

    /**
     * 销售总监
     */
    @Column(name = "salesDirector")
    private String salesDirector;

    /**
     * 销售id
     */
    @Column(name = "salesId")
    private String salesId;

    /**
     * 部门id
     */
    @Column(name = "departmentId")
    private Integer departmentId;

    /**
     * 车辆id
     */
    @Column(name = "vehicleId")
    private String vehicleId;

    /**
     * 订单状态，0：待审核，1：审核通过，2：审核未通过
     */
    @Column(name = "orderStatus")
    private Integer orderStatus;

    /**
     * 是否到期，0：未过期，1：即将过期，2：已过期
     */
    @Column(name = "orderExpire")
    private Integer orderExpire;

    /**
     * 订单图片地址
     */
    @Column(name = "orderImageUrl")
    private String orderImageUrl;

    /**
     * 订单审核不通过原因
     */
    @Column(name = "reasonOfCanNotPass")
    private String reasonOfCanNotPass;

    public Order() {
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInColor() {
        return inColor;
    }

    public void setInColor(String inColor) {
        this.inColor = inColor;
    }

    public String getOutColor() {
        return outColor;
    }

    public void setOutColor(String outColor) {
        this.outColor = outColor;
    }

    public Integer getCanChangeColor() {
        return canChangeColor;
    }

    public void setCanChangeColor(Integer canChangeColor) {
        this.canChangeColor = canChangeColor;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDeductionPrice() {
        return deductionPrice;
    }

    public void setDeductionPrice(Integer deductionPrice) {
        this.deductionPrice = deductionPrice;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getCashOrInstallment() {
        return cashOrInstallment;
    }

    public void setCashOrInstallment(Integer cashOrInstallment) {
        this.cashOrInstallment = cashOrInstallment;
    }

    public Double getCashSubscription() {
        return cashSubscription;
    }

    public void setCashSubscription(Double cashSubscription) {
        this.cashSubscription = cashSubscription;
    }

    public Double getCashSpareMoney() {
        return cashSpareMoney;
    }

    public void setCashSpareMoney(Double cashSpareMoney) {
        this.cashSpareMoney = cashSpareMoney;
    }

    public Double getInstallmentDownPayment() {
        return installmentDownPayment;
    }

    public void setInstallmentDownPayment(Double installmentDownPayment) {
        this.installmentDownPayment = installmentDownPayment;
    }

    public Double getInstallmentLoanAmount() {
        return installmentLoanAmount;
    }

    public void setInstallmentLoanAmount(Double installmentLoanAmount) {
        this.installmentLoanAmount = installmentLoanAmount;
    }

    public Date getGiveVehicleDate() {
        return giveVehicleDate;
    }

    public void setGiveVehicleDate(Date giveVehicleDate) {
        this.giveVehicleDate = giveVehicleDate;
    }

    public String getVehicleDamageInsurance() {
        return vehicleDamageInsurance;
    }

    public void setVehicleDamageInsurance(String vehicleDamageInsurance) {
        this.vehicleDamageInsurance = vehicleDamageInsurance;
    }

    public String getThirdPartyLiabilityInsurance() {
        return thirdPartyLiabilityInsurance;
    }

    public void setThirdPartyLiabilityInsurance(String thirdPartyLiabilityInsurance) {
        this.thirdPartyLiabilityInsurance = thirdPartyLiabilityInsurance;
    }

    public String getTotalVehicleTheftInsurance() {
        return totalVehicleTheftInsurance;
    }

    public void setTotalVehicleTheftInsurance(String totalVehicleTheftInsurance) {
        this.totalVehicleTheftInsurance = totalVehicleTheftInsurance;
    }

    public String getVehiclePersonnelLiabilityInsurance() {
        return vehiclePersonnelLiabilityInsurance;
    }

    public void setVehiclePersonnelLiabilityInsurance(String vehiclePersonnelLiabilityInsurance) {
        this.vehiclePersonnelLiabilityInsurance = vehiclePersonnelLiabilityInsurance;
    }

    public String getNoLiabilityInsurance() {
        return noLiabilityInsurance;
    }

    public void setNoLiabilityInsurance(String noLiabilityInsurance) {
        this.noLiabilityInsurance = noLiabilityInsurance;
    }

    public String getRiskOfGlassBreakageInsurance() {
        return riskOfGlassBreakageInsurance;
    }

    public void setRiskOfGlassBreakageInsurance(String riskOfGlassBreakageInsurance) {
        this.riskOfGlassBreakageInsurance = riskOfGlassBreakageInsurance;
    }

    public String getSpontaneousCombustionLossesInsurance() {
        return spontaneousCombustionLossesInsurance;
    }

    public void setSpontaneousCombustionLossesInsurance(String spontaneousCombustionLossesInsurance) {
        this.spontaneousCombustionLossesInsurance = spontaneousCombustionLossesInsurance;
    }

    public String getNonDeductibleSpecialRiskInsurance() {
        return nonDeductibleSpecialRiskInsurance;
    }

    public void setNonDeductibleSpecialRiskInsurance(String nonDeductibleSpecialRiskInsurance) {
        this.nonDeductibleSpecialRiskInsurance = nonDeductibleSpecialRiskInsurance;
    }

    public String getOtherInsurance() {
        return otherInsurance;
    }

    public void setOtherInsurance(String otherInsurance) {
        this.otherInsurance = otherInsurance;
    }

    public String getInsuranceEstimate() {
        return insuranceEstimate;
    }

    public void setInsuranceEstimate(String insuranceEstimate) {
        this.insuranceEstimate = insuranceEstimate;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getFineDetail() {
        return fineDetail;
    }

    public void setFineDetail(String fineDetail) {
        this.fineDetail = fineDetail;
    }

    public String getReviewerComment() {
        return reviewerComment;
    }

    public void setReviewerComment(String reviewerComment) {
        this.reviewerComment = reviewerComment;
    }

    public String getSubscriptionConfirmationAmountLower() {
        return subscriptionConfirmationAmountLower;
    }

    public void setSubscriptionConfirmationAmountLower(String subscriptionConfirmationAmountLower) {
        this.subscriptionConfirmationAmountLower = subscriptionConfirmationAmountLower;
    }

    public String getSubscriptionConfirmationAmountUpper() {
        return subscriptionConfirmationAmountUpper;
    }

    public void setSubscriptionConfirmationAmountUpper(String subscriptionConfirmationAmountUpper) {
        this.subscriptionConfirmationAmountUpper = subscriptionConfirmationAmountUpper;
    }

    public Date getSubscriptionConfirmationArrivalDate() {
        return subscriptionConfirmationArrivalDate;
    }

    public void setSubscriptionConfirmationArrivalDate(Date subscriptionConfirmationArrivalDate) {
        this.subscriptionConfirmationArrivalDate = subscriptionConfirmationArrivalDate;
    }

    public String getSalesConsultant() {
        return salesConsultant;
    }

    public void setSalesConsultant(String salesConsultant) {
        this.salesConsultant = salesConsultant;
    }

    public String getSalesDirector() {
        return salesDirector;
    }

    public void setSalesDirector(String salesDirector) {
        this.salesDirector = salesDirector;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderExpire() {
        return orderExpire;
    }

    public void setOrderExpire(Integer orderExpire) {
        this.orderExpire = orderExpire;
    }

    public String getOrderImageUrl() {
        return orderImageUrl;
    }

    public void setOrderImageUrl(String orderImageUrl) {
        this.orderImageUrl = orderImageUrl;
    }

    public String getReasonOfCanNotPass() {
        return reasonOfCanNotPass;
    }

    public void setReasonOfCanNotPass(String reasonOfCanNotPass) {
        this.reasonOfCanNotPass = reasonOfCanNotPass;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNo='" + orderNo + '\'' +
                ", orderTime=" + orderTime +
                ", invoiceName='" + invoiceName + '\'' +
                ", identity=" + identity +
                ", department='" + department + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", profession='" + profession + '\'' +
                ", eMail='" + eMail + '\'' +
                ", vehicleName='" + vehicleName + '\'' +
                ", version='" + version + '\'' +
                ", inColor='" + inColor + '\'' +
                ", outColor='" + outColor + '\'' +
                ", canChangeColor=" + canChangeColor +
                ", quantity=" + quantity +
                ", price=" + price +
                ", deductionPrice=" + deductionPrice +
                ", paymentMethod=" + paymentMethod +
                ", cashOrInstallment=" + cashOrInstallment +
                ", cashSubscription=" + cashSubscription +
                ", cashSpareMoney=" + cashSpareMoney +
                ", installmentDownPayment=" + installmentDownPayment +
                ", installmentLoanAmount=" + installmentLoanAmount +
                ", giveVehicleDate=" + giveVehicleDate +
                ", vehicleDamageInsurance='" + vehicleDamageInsurance + '\'' +
                ", thirdPartyLiabilityInsurance='" + thirdPartyLiabilityInsurance + '\'' +
                ", totalVehicleTheftInsurance='" + totalVehicleTheftInsurance + '\'' +
                ", vehiclePersonnelLiabilityInsurance='" + vehiclePersonnelLiabilityInsurance + '\'' +
                ", noLiabilityInsurance='" + noLiabilityInsurance + '\'' +
                ", riskOfGlassBreakageInsurance='" + riskOfGlassBreakageInsurance + '\'' +
                ", spontaneousCombustionLossesInsurance='" + spontaneousCombustionLossesInsurance + '\'' +
                ", nonDeductibleSpecialRiskInsurance='" + nonDeductibleSpecialRiskInsurance + '\'' +
                ", otherInsurance='" + otherInsurance + '\'' +
                ", insuranceEstimate='" + insuranceEstimate + '\'' +
                ", insuranceCompanyName='" + insuranceCompanyName + '\'' +
                ", fineDetail='" + fineDetail + '\'' +
                ", reviewerComment='" + reviewerComment + '\'' +
                ", subscriptionConfirmationAmountLower='" + subscriptionConfirmationAmountLower + '\'' +
                ", subscriptionConfirmationAmountUpper='" + subscriptionConfirmationAmountUpper + '\'' +
                ", subscriptionConfirmationArrivalDate=" + subscriptionConfirmationArrivalDate +
                ", salesConsultant='" + salesConsultant + '\'' +
                ", salesDirector='" + salesDirector + '\'' +
                ", salesId='" + salesId + '\'' +
                ", departmentId=" + departmentId +
                ", vehicleId='" + vehicleId + '\'' +
                ", orderStatus=" + orderStatus +
                ", orderExpire=" + orderExpire +
                ", orderImageUrl='" + orderImageUrl + '\'' +
                ", reasonOfCanNotPass='" + reasonOfCanNotPass + '\'' +
                '}';
    }
}
