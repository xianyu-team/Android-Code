package com.example.chenweizhao.xianyu.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MoneyActivity extends AppCompatActivity {
    ImageView back;
    TextView history;
    TextView value;
    Button buy;
    Button sell;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        init();

        initData();

        setClick();
    }

    void init() {
        back = findViewById(R.id.back);
        history = findViewById(R.id.money_history);
        value = findViewById(R.id.my_money);
        buy = findViewById(R.id.buy);
        sell = findViewById(R.id.sell);
    }

    void initData() {
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

            getValue service = retrofit.create(getValue.class);
            service.getMessage()
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Confirm_packet_login>() {
                        @Override
                        public void onNext(Confirm_packet_login confirm_packet_login) {
                            value.setText(confirm_packet_login.getData().getUser_balance() + ".00");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MoneyActivity.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(MoneyActivity.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }

    void setClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoneyActivity.this, MoneyHistoryActivity.class);
                startActivity(intent);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "充值功能暂未开放", Toast.LENGTH_SHORT).show();
            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "提现功能暂未开放", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface getValue{
        @GET("/user/balance")
        Observable<Confirm_packet_login> getMessage();
    }

    public interface getHistory{
        @GET("/bill")
        Observable<Confirm_packet_login> getMessage();
    }
}

