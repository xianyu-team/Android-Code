package com.example.chenweizhao.xianyu.Activities;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class ReceivedCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder()
                .removeHeader("Content-Type")//移除旧的
                .addHeader("Content-Type", "application/json")//添加真正的头部
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")//添加真正的头部,可以写死，也可以动态获取
                .addHeader("X-CSRFtoken", Header.getToken())
                .removeHeader("Cookie")
                .addHeader("Cookie", "sessionid=" + Header.getSessionID() + ";" + "csrftoken=" + Header.getToken());

        Response originalResponse = chain.proceed(builder.build());
        if (!originalResponse.headers("set-cookie").isEmpty()) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < originalResponse.headers("set-cookie").size(); i++) {
                sb.append(originalResponse.headers("set-cookie").get(i));
                sb.append("; ");
            }
            String cookie = sb.toString();
            cookie = cookie.replace(';', ' ');
            cookie = cookie.replace('=', ' ');
            String[] data = cookie.split(" ");
            for (int i = 0; i < data.length; i++) {
                if(data[i].equals("sessionid")) {
                    Header.setSessionID(data[i + 1]);
                    i++;
                }
                if(data[i].equals("csrftoken")) {
                    Header.setToken(data[i + 1]);
                    i++;
                }
            }
        }
        return originalResponse;
    }
}
