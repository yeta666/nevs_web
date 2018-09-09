package com.nevs.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author YETA
 * 车辆实体类
 * @date 2018/08/26/16:44
 */
@Entity
@Table(name = "vehicle")
public class Vehicle {

    /**
     * 车辆id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 名字
     */
    @Column(name = "name")
    private String name;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 价格
     */
    @Column(name = "price")
    private Double price;

    /**
     * 订金
     */
    @Column(name = "subscription")
    private Double subscription;

    /**
     * 销售量
     */
    @Column(name = "quantityOfSale")
    private Integer quantityOfSale;

    /**
     * 图片地址
     */
    @Column(name = "imageUrl")
    private String imageUrl;

    /**
     * 是否有库存，1：有，0：没有
     */
    @Column(name = "store")
    private Integer store;

    public Vehicle() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubscription() {
        return subscription;
    }

    public void setSubscription(Double subscription) {
        this.subscription = subscription;
    }

    public Integer getQuantityOfSale() {
        return quantityOfSale;
    }

    public void setQuantityOfSale(Integer quantityOfSale) {
        this.quantityOfSale = quantityOfSale;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", subscription=" + subscription +
                ", quantityOfSale=" + quantityOfSale +
                ", imageUrl='" + imageUrl + '\'' +
                ", store=" + store +
                '}';
    }
}
