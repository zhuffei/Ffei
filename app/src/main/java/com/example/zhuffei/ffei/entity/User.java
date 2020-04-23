package com.example.zhuffei.ffei.entity;


/**
 * @author zhuffei
 * @date 2020.3.4
 */

public class User {

    private int id;

    private String name;

    private String phone;

    private String pwd;

//    private int role;

    private int state;

//    private int schoolId;

    private String avator;

    private Integer isFocused;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

//    public int getRole() {
//        return role;
//    }
//
//    public void setRole(int role) {
//        this.role = role;
//    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
//
//    public int getSchoolId() {
//        return schoolId;
//    }
//
//    public void setSchoolId(int schoolId) {
//        this.schoolId = schoolId;
//    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public Integer getIsFocused() {
        return isFocused;
    }

    public void setIsFocused(Integer isFocused) {
        this.isFocused = isFocused;
    }

    public User() {
    }
}
