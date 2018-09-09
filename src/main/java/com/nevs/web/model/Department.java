package com.nevs.web.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author YETA
 * 部门实体
 * @date 2018/08/26/16:00
 */
@Entity
@Table(name = "department")
public class Department {

    /**
     * 部门id
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    /**
     * 部门名
     */
    @Column(name = "name")
    private String name;

    /**
     * 部门管理员id
     */
    @Column(name = "managerId")
    private String managerId;

    /**
     * 部门管理员姓名
     */
    @Column(name = "managerName")
    private String managerName;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 总销售量
     */
    @Column(name = "totalSales")
    private Integer totalSales;

    /**
     * 季度销售量
     */
    @Column(name = "quarterlySales")
    private Integer quarterlySales;

    /**
     * 上一次清算时间
     */
    @Column(name = "lastLiquidationTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastLiquidationTime;

    /**
     * 卖的多标志位
     */
    @Column(name = "flag")
    private Integer flag;

    public Department() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public Integer getQuarterlySales() {
        return quarterlySales;
    }

    public void setQuarterlySales(Integer quarterlySales) {
        this.quarterlySales = quarterlySales;
    }

    public Date getLastLiquidationTime() {
        return lastLiquidationTime;
    }

    public void setLastLiquidationTime(Date lastLiquidationTime) {
        this.lastLiquidationTime = lastLiquidationTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", managerId='" + managerId + '\'' +
                ", managerName='" + managerName + '\'' +
                ", createTime=" + createTime +
                ", totalSales=" + totalSales +
                ", quarterlySales=" + quarterlySales +
                ", lastLiquidationTime=" + lastLiquidationTime +
                ", flag=" + flag +
                '}';
    }
}
