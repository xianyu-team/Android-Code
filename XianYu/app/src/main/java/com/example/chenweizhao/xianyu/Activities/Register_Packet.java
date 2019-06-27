package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class Register_Packet implements Serializable {
    private String user_phone;
    private String user_password;
    private String verification_code;

    public Register_Packet(String user_phone, String user_password, String verification_code) {
        this.user_phone = user_phone;
        this.user_password = user_password;
        this.verification_code = verification_code;
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

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }
}
