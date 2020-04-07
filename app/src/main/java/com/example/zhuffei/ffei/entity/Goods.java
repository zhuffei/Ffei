package com.example.zhuffei.ffei.entity;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/3/22 18:34
 */
public class Goods {
    private int id;

    private String name;

    private Double price;

    private int img;

    private String state;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Goods(String name, Double price, int img, String state) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.state = state;
    }

    public Goods(String name, Double price, int img){
        this.name = name;
        this.price = price;
        this.img = img;
    }
}