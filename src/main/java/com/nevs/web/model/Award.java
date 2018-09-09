package com.nevs.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author YETA
 * 奖励实体
 * @date 2018/09/08/19:08
 */
@Entity
@Table(name = "award")
public class Award {

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 一级奖励
     */
    @Column(name = "levelOfReward")
    private Integer levelOfReward;

    /**
     * 二级奖励
     */
    @Column(name = "secondaryReward")
    private Integer secondaryReward;

    /**
     * 部门管理员奖励
     */
    @Column(name = "managerReward")
    private Integer managerReward;

    public Award() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLevelOfReward() {
        return levelOfReward;
    }

    public void setLevelOfReward(Integer levelOfReward) {
        this.levelOfReward = levelOfReward;
    }

    public Integer getSecondaryReward() {
        return secondaryReward;
    }

    public void setSecondaryReward(Integer secondaryReward) {
        this.secondaryReward = secondaryReward;
    }

    public Integer getManagerReward() {
        return managerReward;
    }

    public void setManagerReward(Integer managerReward) {
        this.managerReward = managerReward;
    }

    @Override
    public String toString() {
        return "Award{" +
                "id='" + id + '\'' +
                ", levelOfReward=" + levelOfReward +
                ", secondaryReward=" + secondaryReward +
                ", managerReward=" + managerReward +
                '}';
    }
}
