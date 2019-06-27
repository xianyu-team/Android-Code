package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class Login_packet_by_message implements Serializable {
    private String user_phone;
    private String verification_code;

    public Login_packet_by_message(String user_phone, String verification_code) {
        setUser_phone(user_phone);
        setVerification_code(verification_code);
    }
    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getVerification_code() {
        return verification_code;
    }
}
