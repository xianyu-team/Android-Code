package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    Button clearBtn;
    Button mesBtn;
    EditText phoneNumView;
    EditText phoneMesView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.register_OK) ;
        clearBtn = findViewById(R.id.register_clear);
        mesBtn = findViewById(R.id.register_by_message_get_message);
        phoneNumView = findViewById(R.id.register_by_message_phone);
        phoneMesView = findViewById(R.id.register_by_message_message);
        addListeners();
    }

    private void addListeners() {
        mesBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String phoneNum = phoneNumView.getText().toString();
                if (phoneNum.length() != 11) return;
                check_internet();

                try {
                    OkHttpClient build = new OkHttpClient.Builder()
                            .connectTimeout(2, TimeUnit.SECONDS)
                            .readTimeout(2, TimeUnit.SECONDS)
                            .writeTimeout(2, TimeUnit.SECONDS)
                            .addInterceptor(new RegisterActivity.ReceivedCookiesInterceptor())
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://120.77.146.251:8000")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置
                            .client(build)
                            .build();

                    final RegisterActivity.RegisterInterface service = retrofit.create(RegisterActivity.RegisterInterface.class);

                    service.getVeriCode(phoneNumView.getText().toString())
                            .subscribeOn(Schedulers.io())//请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                            .subscribe(new DisposableObserver<Confirm_packet_verification>() {
                                @Override
                                public void onNext(Confirm_packet_verification confirm_packet) {
                                    if (confirm_packet.getCode() == 200) {
                                        Toast.makeText(RegisterActivity.this, "验证码: " + confirm_packet.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "验证失败: " + confirm_packet.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(RegisterActivity.this, "验证失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("test", "onError: " + e.toString());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                } catch (Exception o) {
                    Toast.makeText(RegisterActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verification_packet veri = new Verification_packet(phoneNumView.getText().toString(), phoneMesView.getText().toString());
                check_internet();
                try {
                    OkHttpClient build = new OkHttpClient.Builder()
                            .connectTimeout(2, TimeUnit.SECONDS)
                            .readTimeout(2, TimeUnit.SECONDS)
                            .writeTimeout(2, TimeUnit.SECONDS)
                            .addInterceptor(new RegisterActivity.ReceivedCookiesInterceptor())
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://120.77.146.251:8000")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置
                            .client(build)
                            .build();

                    final RegisterActivity.RegisterInterface service = retrofit.create(RegisterActivity.RegisterInterface.class);

                    service.postVeri(veri)
                            .subscribeOn(Schedulers.io())//请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                            .subscribe(new DisposableObserver<Confirm_packet_verification>() {
                                @Override
                                public void onNext(Confirm_packet_verification confirm_packet) {
                                    if (confirm_packet.getCode() == 200) {
                                        Intent intent = new Intent(RegisterActivity.this, SetPasswordActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phoneNum", phoneNumView.getText().toString());
                                        bundle.putString("veriCode", phoneMesView.getText().toString());
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "验证失败: " + confirm_packet.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(RegisterActivity.this, "验证失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("test", "onError: " + e.toString());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                } catch (Exception o) {
                    Toast.makeText(RegisterActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneMesView.setText("");
                phoneNumView.setText("");
            }
        });

        ((TextView)findViewById(R.id.goto_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public interface RegisterInterface {
        @POST("/sms/verification")
        Observable<Confirm_packet_verification> postVeri(@Body Verification_packet veri_packet);

        @GET("/sms/verification_code/{user_phone}")
        Observable<Confirm_packet_verification> getVeriCode(@Path("user_phone") String user_phone);
    }

    private void check_internet() {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(RegisterActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
    }

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
                Header.setToken(data[1]);
                Header.setSessionID(data[21]);

            }
            return originalResponse;
        }
    }
}
