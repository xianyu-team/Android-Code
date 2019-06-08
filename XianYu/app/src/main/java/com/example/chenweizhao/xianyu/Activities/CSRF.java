package com.example.chenweizhao.xianyu.Activities;

public class CSRF {

    public CSRF(){

    }

    public static String getCsrf() {
        return CSRF.csrf;
    }

    public static void setCsrf(String csrf) {
        CSRF.csrf = csrf;
    }

    public static String csrf = "";
}
