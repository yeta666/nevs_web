package com.nevs.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author YETA
 * 系统实体
 * @date 2018/08/26/16:00
 */
@Entity
@Table(name = "system")
public class System {

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 公司简介
     */
    @Column(name = "companyProfile")
    private String companyProfile;

    /**
     * 首页图片路径
     */
    @Column(name = "homePageImageUrl")
    private String homePageImageUrl;

    /**
     * 公司地址
     */
    @Column(name = "companyAddress")
    private String companyAddress;

    /**
     * 联系电话
     */
    @Column(name = "contactNumber")
    private String contactNumber;

    public System() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }

    public String getHomePageImageUrl() {
        return homePageImageUrl;
    }

    public void setHomePageImageUrl(String homePageImageUrl) {
        this.homePageImageUrl = homePageImageUrl;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "System{" +
                "id=" + id +
                ", companyProfile='" + companyProfile + '\'' +
                ", homePageImageUrl='" + homePageImageUrl + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
