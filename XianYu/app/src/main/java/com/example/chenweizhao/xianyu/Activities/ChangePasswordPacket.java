package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class ChangePasswordPacket implements Serializable {
    private String user_phone;
    private String user_password;
    private String verification_code;

    public ChangePasswordPacket(String user_phone, String user_password, String verification_code) {
        setUser_phone(user_phone);
        setUser_password(user_password);
        setVerification_code(verification_code);
    }
    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getVerification_code() {
        return verification_code;
    }
}
