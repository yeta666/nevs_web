package com.nevs.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author YETA
 * 异常实体
 * @date 2018/09/05/14:11
 */
@Entity
@Table(name = "exception")
public class ExceptionLog {

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 具体信息
     */
    @Column(name = "message")
    private String message;

    /**
     * 时间
     */
    @Column(name = "time")
    private Date time;

    public ExceptionLog() {
    }

    public ExceptionLog(String id, String type, String message, Date time) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ExceptionLog{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
