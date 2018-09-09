package com.nevs.web.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author YETA
 * 日志实体类
 * @date 2018/09/02/14:37
 */
@Entity
public class Log {

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 操作，1：登录，2：注销，3：创建部门，4：修改部门，5：修改奖励数目，6：修改用户资料，7：下载用户信息
     */
    @Column(name = "action")
    private Integer action;

    /**
     * 细节
     */
    @Column(name = "detail")
    private String detail;

    /**
     * 时间
     */
    @Column(name = "time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    /**
     * 人物
     */
    @Column(name = "creator")
    private String creator;

    public Log() {
    }

    public Log(String id, Integer action, String detail, Date time, String creator) {
        this.id = id;
        this.action = action;
        this.detail = detail;
        this.time = time;
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id='" + id + '\'' +
                ", action=" + action +
                ", detail='" + detail + '\'' +
                ", time=" + time +
                ", creator='" + creator + '\'' +
                '}';
    }
}
