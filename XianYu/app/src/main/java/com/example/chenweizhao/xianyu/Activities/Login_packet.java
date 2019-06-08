package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class Login_packet implements Serializable {

    /**
     * user_phone : 15989061915
     * user_password : 123456
     */

    private String user_phone;
    private String user_password;

    Login_packet(String user_phone, String user_password) {
        setUser_phone(user_phone);
        setUser_password(user_password);
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
