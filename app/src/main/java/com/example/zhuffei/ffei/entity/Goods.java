package com.example.zhuffei.ffei.entity;


/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/9 14:52
 */


public class Goods {
    private int id;

    private Integer uId;

    private String name;

    private String des;

    private Double price;

    private Integer type;

    private Integer state;

    private String location;

    private String img1;

    private String img2;

    private String img3;

    private String img4;

    private String img5;

    private String img6;

    private Integer browse;

    private String createTime;

    private String updateTime;

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", uId=" + uId +
                ", name='" + name + '\'' +
                ", des='" + des + '\'' +
                ", price=" + price +
                ", type=" + type +
                ", state=" + state +
                ", location='" + location + '\'' +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", img3='" + img3 + '\'' +
                ", img4='" + img4 + '\'' +
                ", img5='" + img5 + '\'' +
                ", img6='" + img6 + '\'' +
                ", browse=" + browse +
                ", createTime=" + createTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    public String getImg6() {
        return img6;
    }

    public void setImg6(String img6) {
        this.img6 = img6;
    }

    public Integer getBrowse() {
        return browse;
    }

    public void setBrowse(Integer browse) {
        this.browse = browse;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
