package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class ModifyInfoPacket implements Serializable {

    /**
     * user_icon : dxba1at...
     * student_name : 陈xx
     * student_university : 中山大学
     * student_academy : 数据科学与计算机学院
     * student_number : 16340034
     * student_gender : 1
     */

    private String user_icon;
    private String student_name;
    private String student_university;
    private String student_academy;
    private String student_number;
    private int student_gender;

    ModifyInfoPacket (String user_icon, String student_name, String student_university, String student_academy, String student_number, int student_gender) {
        setUser_icon(user_icon);
        setStudent_university(student_university);
        setStudent_name(student_name);
        setStudent_academy(student_academy);
        setStudent_number(student_number);
        setStudent_gender(student_gender);
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_university() {
        return student_university;
    }

    public void setStudent_university(String student_university) {
        this.student_university = student_university;
    }

    public String getStudent_academy() {
        return student_academy;
    }

    public void setStudent_academy(String student_academy) {
        this.student_academy = student_academy;
    }

    public String getStudent_number() {
        return student_number;
    }

    public void setStudent_number(String student_number) {
        this.student_number = student_number;
    }

    public int getStudent_gender() {
        return student_gender;
    }

    public void setStudent_gender(int student_gender) {
        this.student_gender = student_gender;
    }
}
