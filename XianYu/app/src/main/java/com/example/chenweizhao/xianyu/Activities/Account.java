package com.example.chenweizhao.xianyu.Activities;

public class Account {
    int userId;
    String icon;
    String userName;
    String schoolName;

    Account(int userId, String icon, String userName, String schoolName){
        setUserId(userId);
        setIcon(icon);
        setUserName(userName);
        setSchoolName(schoolName);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getUserId() {
        return userId;
    }

    public String getIcon() {
        return icon;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getUserName() {
        return userName;
    }
}
