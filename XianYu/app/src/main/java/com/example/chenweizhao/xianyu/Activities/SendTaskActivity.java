package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class SendTaskActivity extends AppCompatActivity {
    Button send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_task);
        init();
        set_click();
    }

    void init () {
        send = findViewById(R.id.send);
    }

    void set_click() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cn.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    Toast.makeText(SendTaskActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
                    return;
                }

                Login_packet login_packet = new Login_packet("15016561536", "15016561536");

                try {
                    OkHttpClient build = new OkHttpClient.Builder()
                            .connectTimeout(2, TimeUnit.SECONDS)
                            .readTimeout(2, TimeUnit.SECONDS)
                            .writeTimeout(2, TimeUnit.SECONDS)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request()
                                            .newBuilder()
                                            .removeHeader("Content-Type")//移除旧的
                                            .addHeader("Content-Type", "application/json")//添加真正的头部
                                            .build();
                                    Response originalResponse = chain.proceed(request);
                                    if (!originalResponse.headers("set-cookie").isEmpty()) {
                                        //Toast.makeText(SendTaskActivity.this, "cookie:", Toast.LENGTH_SHORT).show();
                                        StringBuffer sb = new StringBuffer();
                                        for (int i = 0; i < originalResponse.headers("set-cookie").size(); i++) {
                                            sb.append(originalResponse.headers("set-cookie").get(i));
                                            sb.append("; ");
                                        }
                                        String temp = sb.toString();
                                        temp = temp.replace(';', ' ');
                                        temp = temp.replace('=', ' ');
                                        String [] data = temp.split(" ");
                                        //String temp = sb.toString();
                                        Log.v("cookie", CSRF.getCsrf() + "a");
                                        Log.v("cookie", "test");
                                        CSRF.setCsrf(data[23]);
                                        Log.v("cookie", CSRF.getCsrf());
                                    }
                                    return  originalResponse;
                                }
                                })
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://120.77.146.251:8000")

                            .addConverterFactory(GsonConverterFactory.create())

                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置

                            // build 即为okhttp声明的变量，下文会讲
                            .client(build)

                            .build();

                    LoginInterface service = retrofit.create(LoginInterface.class);
                    service.postRegister(login_packet)
                            .subscribeOn(Schedulers.io())//请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                            .subscribe(new DisposableObserver<Confirm_packet_login>() {
                                @Override
                                public void onNext(Confirm_packet_login confirm_packet_login) {
                                    if(confirm_packet_login.getCode() == 200){
                                        Toast.makeText(SendTaskActivity.this, "成功: " + confirm_packet_login.getMessage() + "用户信息" + confirm_packet_login.getData().getUser_fillln(), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(SendTaskActivity.this, "失败: " + confirm_packet_login.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(SendTaskActivity.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("test", "onError: "+e.toString());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }catch (Exception o){
                    Toast.makeText(SendTaskActivity.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("test", "onError: "+o.toString());
                }
            }
        });
    }

    public interface LoginInterface{
        @POST("/user/password/session")
        Observable<Confirm_packet_login> postRegister(@Body Login_packet login_packet);
    }


}
