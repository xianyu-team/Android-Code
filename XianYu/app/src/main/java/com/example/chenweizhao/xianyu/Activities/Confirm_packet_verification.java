package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class Confirm_packet_verification implements Serializable {
    /**
     * code : 200
     * message : OK
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
