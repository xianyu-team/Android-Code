package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
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

public class FindPasswordActivity2_ChangePassword extends AppCompatActivity {
    ImageView mBack;
    EditText mPassword1;
    EditText mPassword2;
    ImageView mOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_change_password);
        init();
    }

    void init(){
        mBack = findViewById(R.id.back);
        mPassword1 = findViewById(R.id.new_password);
        mPassword2 = findViewById(R.id.confirm_password);
        mOk = findViewById(R.id.change_password);
    }


    void setClick(){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = mPassword1.getText().toString();
                String password2 = mPassword2.getText().toString();
                if (password1.equals("") || password2.equals("")){
                    Toast.makeText(FindPasswordActivity2_ChangePassword.this, "密码不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password1.equals(password2)) {
                    Toast.makeText(FindPasswordActivity2_ChangePassword.this, "前后密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cn.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    Toast.makeText(FindPasswordActivity2_ChangePassword.this, "网络未连接", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone = getIntent().getStringExtra("phone");
                String message = getIntent().getStringExtra("message");
                ChangePasswordPacket login_packet = new ChangePasswordPacket(phone, password1, message);
                try {
                    OkHttpClient build = new OkHttpClient.Builder()
                            .connectTimeout(2, TimeUnit.SECONDS)
                            .readTimeout(2, TimeUnit.SECONDS)
                            .writeTimeout(2, TimeUnit.SECONDS)
                            .addInterceptor(new ReceivedCookiesInterceptor())
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://120.77.146.251:8000")

                            .addConverterFactory(GsonConverterFactory.create())

                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置

                            // build 即为okhttp声明的变量，下文会讲
                            .client(build)

                            .build();

                    ChangePassword service = retrofit.create(ChangePassword.class);
                    service.postRegister(login_packet)
                            .subscribeOn(Schedulers.io())//请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                            .subscribe(new DisposableObserver<Confirm_packet_login>() {
                                @Override
                                public void onNext(Confirm_packet_login confirm_packet_login) {
                                    if(confirm_packet_login.getCode() == 200){
                                        Intent intent = new Intent(FindPasswordActivity2_ChangePassword.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(FindPasswordActivity2_ChangePassword.this, confirm_packet_login.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(FindPasswordActivity2_ChangePassword.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("test", "onError: "+e.toString());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }catch (Exception o){
                    Toast.makeText(FindPasswordActivity2_ChangePassword.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("test", "onError: "+o.toString());
                }
            }
        });
    }

    public interface ChangePassword{
        @POST("/user/password")
        Observable<Confirm_packet_login> postRegister(@Body ChangePasswordPacket ChangePasswordPacket);
    }

}
