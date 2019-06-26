package com.example.chenweizhao.xianyu.Activities;

public class Header {
    public Header(){

    }

    public static String getToken() {
        return Header.token;
    }

    public static void setToken(String token) {
        Header.token = token;
    }

    public static String getSessionID() {return Header.sessionID;}

    public static void setSessionID(String sessionID) {Header.sessionID = sessionID;}

    public static String token = "";

    public static String sessionID = "";
}
