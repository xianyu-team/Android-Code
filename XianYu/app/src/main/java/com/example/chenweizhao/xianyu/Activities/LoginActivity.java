package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class LoginActivity extends AppCompatActivity {
    Button mLoginByPassword;
    Button mLoginByMessage;
    EditText mLoginByMessagePhone;
    EditText mLoginByMessageMessage;
    Button mLoginGetMessage;

    EditText mLoginByPasswordPhone;
    EditText mLoginByPasswordPassword;

    Button mLoginOK;
    Button mLoginClear;

    TextView mGotoRegister;
    TextView mFindPassword;

    ConstraintLayout mLoginByPasswordInfo;
    ConstraintLayout mLoginbyMessageInfo;

    boolean isLoginByPasssword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setClick();
    }

    void init(){
        isLoginByPasssword = true;
        mLoginByPassword = findViewById(R.id.login_by_password);
        mLoginByMessage = findViewById(R.id.login_by_message);
        mLoginByMessagePhone = findViewById(R.id.login_by_message_phone);
        mLoginByMessageMessage = findViewById(R.id.login_by_message_message);
        mLoginGetMessage = findViewById(R.id.login_by_message_get_message);
        mLoginByPasswordPhone = findViewById(R.id.login_by_password_phone);
        mLoginByPasswordPassword = findViewById(R.id.login_by_password_password);
        mLoginOK = findViewById(R.id.Login_OK);
        mLoginClear = findViewById(R.id.Login_clear);
        mGotoRegister = findViewById(R.id.Goto_register);
        mFindPassword = findViewById(R.id.find_password);
        mLoginByPasswordInfo = findViewById(R.id.login_by_password_info);
        mLoginbyMessageInfo = findViewById(R.id.login_by_message_info);
    }

    void setClick(){
        mLoginByPassword.setOnClickListener(new ClickListen());
        mLoginByMessage.setOnClickListener(new ClickListen());
        mLoginGetMessage.setOnClickListener(new ClickListen());
        mLoginOK.setOnClickListener(new ClickListen());
        mLoginClear.setOnClickListener(new ClickListen());
        mGotoRegister.setOnClickListener(new ClickListen());
        mFindPassword.setOnClickListener(new ClickListen());
    }

    private class ClickListen implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_by_password:
                    if (!isLoginByPasssword) {
                        mLoginByMessage.setBackgroundColor(Color.parseColor("#888888"));
                        mLoginByPassword.setBackgroundColor(Color.parseColor("#ffffff"));
                        isLoginByPasssword = !isLoginByPasssword;
                        mLoginByPasswordInfo.setVisibility(View.VISIBLE);
                        mLoginbyMessageInfo.setVisibility(View.INVISIBLE);
                        mFindPassword.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.login_by_message:
                    if (isLoginByPasssword) {
                        mLoginByMessage.setBackgroundColor(Color.parseColor("#ffffff"));
                        mLoginByPassword.setBackgroundColor(Color.parseColor("#888888"));
                        isLoginByPasssword = !isLoginByPasssword;
                        mLoginByPasswordInfo.setVisibility(View.INVISIBLE);
                        mLoginbyMessageInfo.setVisibility(View.VISIBLE);
                        mFindPassword.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.login_by_message_get_message:
                        String phone_get_message = mLoginByMessagePhone.getText().toString();
                        if (phone_get_message.equals("") || phone_get_message.length() != 11) {
                            Toast.makeText(LoginActivity.this, "手机号码不规范", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        getMessage(phone_get_message);
                    break;
                case R.id.Login_OK:
                    if (isLoginByPasssword) {
                        String phone = mLoginByPasswordPhone.getText().toString();
                        String password = mLoginByPasswordPassword.getText().toString();
                        if (phone.equals("") || password.equals("")) {
                            Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LoginByPassword(phone, password);
                    }
                    else {
                        String phone = mLoginByMessagePhone.getText().toString();
                        String message = mLoginByMessageMessage.getText().toString();
                        if (phone.equals("") || message.equals("")) {
                            Toast.makeText(LoginActivity.this, "手机或验证码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LoginByMessage(phone, message);
                    }

                    break;
                case R.id.Login_clear:
                    if (isLoginByPasssword) {
                        mLoginByPasswordPhone.setText("");
                        mLoginByPasswordPassword.setText("");
                    }
                    else {
                        mLoginByMessagePhone.setText("");
                        mLoginByMessageMessage.setText("");
                    }
                    break;
                case R.id.Goto_register:
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.find_password:
                    Intent intent1 = new Intent(LoginActivity.this, FindPasswordActivity1.class);
                    startActivity(intent1);
                    break;
                default:
                    break;
            }
        }
    }

    void LoginByPassword(String phone, String password){
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        //Login_packet login_packet = new Login_packet(phone, password);
        Login_packet login_packet = new Login_packet("15016561536", "15016561536");
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

            LoginInterface service = retrofit.create(LoginInterface.class);
            service.postRegister(login_packet)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Confirm_packet_login>() {
                        @Override
                        public void onNext(Confirm_packet_login confirm_packet_login) {
                            if(confirm_packet_login.getCode() == 200){
                                if (confirm_packet_login.getData().getUser_fillln() == 0){
                                    Intent intent = new Intent(LoginActivity.this, PerfectInformationActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, confirm_packet_login.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(LoginActivity.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(LoginActivity.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }

    void getMessage(String phone){
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
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

            GetMessage service = retrofit.create(GetMessage.class);
            service.getMessage(phone)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Confirm_packet_login>() {
                        @Override
                        public void onNext(Confirm_packet_login confirm_packet_login) {
                            Toast.makeText(LoginActivity.this, "短信验证码为：" + confirm_packet_login.getData().getVerification_code(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(LoginActivity.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(LoginActivity.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }

    void LoginByMessage(String phone, String message){
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        Login_packet_by_message login_packet = new Login_packet_by_message(phone, message);
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

            LoginInterfaceByMessage service = retrofit.create(LoginInterfaceByMessage.class);
            service.postRegister(login_packet)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Confirm_packet_login>() {
                        @Override
                        public void onNext(Confirm_packet_login confirm_packet_login) {
                            if(confirm_packet_login.getCode() == 200){
                                if (confirm_packet_login.getData().getUser_fillln() == 0){
                                    Intent intent = new Intent(LoginActivity.this, PerfectInformationActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, confirm_packet_login.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(LoginActivity.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(LoginActivity.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }

    public interface LoginInterfaceByMessage{
        @POST("/user/sms/session")
        Observable<Confirm_packet_login> postRegister(@Body Login_packet_by_message login_packet_by_message);
    }


    public interface LoginInterface{
        @POST("/user/password/session")
        Observable<Confirm_packet_login> postRegister(@Body Login_packet login_packet);
    }


    public interface GetMessage{
        @GET("/sms/verification_code/{user_phone}")
        Observable<Confirm_packet_login> getMessage(@Path("user_phone") String user_phone);
    }
}

