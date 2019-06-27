package com.example.chenweizhao.xianyu.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;


public class SendTaskActivity extends AppCompatActivity {
    private ImageView mBack;
    private Button mSend;
    private EditText mDescribe;
    private EditText mDetail;
    private EditText mValue;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_task);
        init();
        setClick();
    }

    void init() {
        mBack = findViewById(R.id.back);
        mSend = findViewById(R.id.send);
        mDescribe = findViewById(R.id.describe);
        mDetail = findViewById(R.id.detail);
        mValue = findViewById(R.id.value);
    }

    void setClick() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String describe = mDescribe.getText().toString();
                String detail = mDetail.getText().toString();
                String value = mValue.getText().toString();
                if (describe.equals("") || detail.equals("") || value.equals("")) {
                    Toast.makeText(getBaseContext(), "请保证信息已完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                int task_value = Integer.parseInt(value);
                if (task_value <= 0) {
                    Toast.makeText(getBaseContext(), "金额不能少于等于0", Toast.LENGTH_SHORT).show();
                    return;
                }

                Senddeliverypacket senddeliverypacket = new Senddeliverypacket(describe, task_value, detail);
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

                    SendTaskInterface service = retrofit.create(SendTaskInterface.class);
                    service.postRegister(senddeliverypacket)
                            .subscribeOn(Schedulers.io())//请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                            .subscribe(new DisposableObserver<Confirm_packet_login>() {
                                @Override
                                public void onNext(Confirm_packet_login confirm_packet_login) {
                                    if(confirm_packet_login.getCode() == 200){
                                        Toast.makeText(SendTaskActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(SendTaskActivity.this, confirm_packet_login.getMessage(), Toast.LENGTH_SHORT).show();
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

    public interface SendTaskInterface{
        @POST("/task/delivery")
        Observable<Confirm_packet_login> postRegister(@Body Senddeliverypacket senddeliverypacket);
    }

}
