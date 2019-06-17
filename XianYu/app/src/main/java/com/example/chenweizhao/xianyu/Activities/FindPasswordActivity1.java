package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class FindPasswordActivity1 extends AppCompatActivity {
    ImageView mBack;
    EditText mPhone;
    EditText mMessage;
    Button mGetMessage;
    Button mOk;
    Button mclear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_by_message);
        init();
        setClick();
    }

    void init(){
        mBack = findViewById(R.id.back);
        mPhone = findViewById(R.id.find_password_phone);
        mMessage = findViewById(R.id.find_password_message);
        mGetMessage = findViewById(R.id.login_by_message_get_message);
        mOk = findViewById(R.id.find_password_confirm);
        mclear = findViewById(R.id.Login_clear);
    }

    void setClick(){
        mBack.setOnClickListener(new ClickListen());
        mGetMessage.setOnClickListener(new ClickListen());
        mOk.setOnClickListener(new ClickListen());
        mclear.setOnClickListener(new ClickListen());
    }

    private class ClickListen implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.login_by_message_get_message:
                    String phone_get_message = mPhone.getText().toString();
                    if (phone_get_message.equals("") || phone_get_message.length() != 11) {
                        Toast.makeText(FindPasswordActivity1.this, "手机号码不规范", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getMessage(phone_get_message);
                    break;
                case R.id.find_password_confirm:
                    String phone = mPhone.getText().toString();
                    String message = mMessage.getText().toString();
                    if (phone.equals("") || message.equals("")) {
                        Toast.makeText(FindPasswordActivity1.this, "手机或验证码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ChangePasswordByMessage(phone, message);
                    break;
                case R.id.Login_clear:
                    mPhone.setText("");
                    mMessage.setText("");
                    break;
                default:
                    break;
            }
        }
    }



    void getMessage(String phone){
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(FindPasswordActivity1.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
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

            GetMessage service = retrofit.create(GetMessage.class);
            service.getMessage(phone)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Confirm_packet_login>() {
                        @Override
                        public void onNext(Confirm_packet_login confirm_packet_login) {
                            Toast.makeText(FindPasswordActivity1.this, confirm_packet_login.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(FindPasswordActivity1.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(FindPasswordActivity1.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }

    void ChangePasswordByMessage(final String phone, final String message) {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(FindPasswordActivity1.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        Login_packet_by_message login_packet = new Login_packet_by_message(phone, message);
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

            FindPasswordInterfaceByMessage service = retrofit.create(FindPasswordInterfaceByMessage.class);
            service.postRegister(login_packet)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Confirm_packet_login>() {
                        @Override
                        public void onNext(Confirm_packet_login confirm_packet_login) {
                            if(confirm_packet_login.getCode() == 200){
                                Intent intent = new Intent(FindPasswordActivity1.this, FindPasswordActivity2_ChangePassword.class);
                                intent.putExtra("phone", phone);
                                intent.putExtra("message", message);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(FindPasswordActivity1.this, confirm_packet_login.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(FindPasswordActivity1.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(FindPasswordActivity1.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }
    public interface GetMessage{
        @GET("/sms/verification_code/{user_phone}")
        Observable<Confirm_packet_login> getMessage(@Path("user_phone") String user_phone);
    }


    public interface FindPasswordInterfaceByMessage{
        @POST("/sms/verification")
        Observable<Confirm_packet_login> postRegister(@Body Login_packet_by_message login_packet_by_message);
    }

}
