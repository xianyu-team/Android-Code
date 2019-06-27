package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;
import java.util.ArrayList;

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
        private int user_balance;
        private ArrayList<Bills> bills;

        public void setUser_balance(int user_balance) {
            this.user_balance = user_balance;
        }

        public int getUser_balance() {
            return user_balance;
        }

        public void setBills(ArrayList<Bills> bills) {
            this.bills = bills;
        }

        public ArrayList<Bills> getBills() {
            return bills;
        }

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

    public static class Bills {
        int bill_id;
        int user_id;
        int bill_type;
        int bill_number;
        String bill_description;
        String bill_time;


        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setBill_description(String bill_description) {
            this.bill_description = bill_description;
        }

        public void setBill_id(int bill_id) {
            this.bill_id = bill_id;
        }

        public void setBill_number(int bill_number) {
            this.bill_number = bill_number;
        }

        public void setBill_time(String bill_time) {
            this.bill_time = bill_time;
        }

        public void setBill_type(int bill_type) {
            this.bill_type = bill_type;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getBill_id() {
            return bill_id;
        }

        public int getBill_number() {
            return bill_number;
        }

        public int getBill_type() {
            return bill_type;
        }

        public String getBill_description() {
            return bill_description;
        }

        public String getBill_time() {
            return bill_time;
        }
    }
}
