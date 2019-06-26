package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class Confirm_packet_login implements Serializable {

    /**
     * code : 200
     * message : OK
     * data : {"user_fillln":0}
     */

    private int code;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_fillln : 0
         */

        private int user_fillln;
        private String verification_code;

        public void setVerification_code(String verification_code) {
            this.verification_code = verification_code;
        }

        public String getVerification_code() {
            return verification_code;
        }

        public int getUser_fillln() {
            return user_fillln;
        }

        public void setUser_fillln(int user_fillln) {
            this.user_fillln = user_fillln;
        }
    }
}
