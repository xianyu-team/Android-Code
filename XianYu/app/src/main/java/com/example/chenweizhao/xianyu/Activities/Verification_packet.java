package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class Verification_packet implements Serializable {
    private String user_phone;
    private String verification_code;

    public Verification_packet(String phoneNum, String code) {
        user_phone = phoneNum;
        verification_code = code;
    }
    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }
}
