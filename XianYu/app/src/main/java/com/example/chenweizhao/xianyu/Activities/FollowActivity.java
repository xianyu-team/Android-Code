package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
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

public class FollowActivity extends AppCompatActivity {
    ImageView back;
    TextView title;
    ListView follow;
    Boolean isFollow;
    private List<Account> accounts = new ArrayList<>();
    private AccountAdapter accountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        init();
        set_click();
        getFollowId();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        isFollow = bundle.getBoolean("isFollow");
        back = findViewById(R.id.follow_back);
        title = findViewById(R.id.follow_title);
        follow = findViewById(R.id.follow_list);
        if(isFollow) {
            //Toast.makeText(AssignmentActivity.this, "Assignment put", Toast.LENGTH_SHORT).show();
            title.setText("关注的人");
        }
        else {
            //Toast.makeText(AssignmentActivity.this, "Assignment get", Toast.LENGTH_SHORT).show();
            title.setText("我的粉丝");
        }
        accounts.add(new Account(1, null, "白点", "中山大学"));
        accounts.add(new Account(2, null, "余霜", "中山大学"));
        accountAdapter = new AccountAdapter(FollowActivity.this, R.layout.item_account, accounts);
        follow.setAdapter(accountAdapter);
    }

    private void set_click() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public interface getFollow {
        @GET("/user/followings")
        Observable<Following_packets> getFollowingsId();

        @GET("/user/fans")
        Observable<Fan_packets> getFansId();

        @POST("/user/batch/information")
        Observable<Batch_user> getUsers(@Body Batch_Id batch_id);

    }

    private void getFollowId() {
        check_internet();
        try {
            OkHttpClient build = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .writeTimeout(2, TimeUnit.SECONDS)
                    .addInterceptor(new FollowActivity.ReceivedCookiesInterceptor())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://120.77.146.251:8000")

                    .addConverterFactory(GsonConverterFactory.create())

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置

                    // build 即为okhttp声明的变量，下文会讲
                    .client(build)

                    .build();

            final FollowActivity.getFollow service = retrofit.create(FollowActivity.getFollow.class);
            if (isFollow) {
                service.getFollowingsId()
                        .subscribeOn(Schedulers.io())//请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                        .subscribe(new DisposableObserver<Following_packets>() {
                            @Override
                            public void onNext(Following_packets following_packets) {
                                if (following_packets.getCode() == 200) {
                                    Toast.makeText(FollowActivity.this, "查询成功，关注者的个数为： " + following_packets.getData().getFollowings().size(), Toast.LENGTH_SHORT).show();
                                    Batch_Id batch_id = new Batch_Id();
                                    List<Batch_Id.UserIdsBean> temp = new ArrayList<>();
                                    for (int i = 0; i < following_packets.getData().getFollowings().size(); i++) {
                                        Batch_Id.UserIdsBean userIdsBean = new Batch_Id.UserIdsBean();
                                        userIdsBean.setUser_id(following_packets.getData().getFollowings().get(i).getFollowing_id());
                                        temp.add(userIdsBean);
                                    }
                                    batch_id.setUser_ids(temp);
                                    service.getUsers(batch_id)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DisposableObserver<Batch_user>() {
                                                @Override
                                                public void onNext(Batch_user batch_user) {
                                                    Toast.makeText(FollowActivity.this, "查询成功2，关注者的个数为： " + batch_user.getData().getUsers().size(), Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onComplete() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }
                                            });

                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
            else {
                service.getFansId()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<Fan_packets>() {
                            @Override
                            public void onNext(Fan_packets fan_packets) {
                                if (fan_packets.getCode() == 200) {
                                    Toast.makeText(FollowActivity.this, "查询成功，粉丝的个数为： " + fan_packets.getData().getFans().size(), Toast.LENGTH_SHORT).show();
                                    Batch_Id batch_id = new Batch_Id();
                                    List<Batch_Id.UserIdsBean> temp = new ArrayList<>();
                                    for (int i = 0; i < fan_packets.getData().getFans().size(); i++) {
                                        Batch_Id.UserIdsBean userIdsBean = new Batch_Id.UserIdsBean();
                                        userIdsBean.setUser_id(fan_packets.getData().getFans().get(i).getFan_id());
                                        temp.add(userIdsBean);
                                    }
                                    batch_id.setUser_ids(temp);
                                    service.getUsers(batch_id)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DisposableObserver<Batch_user>() {
                                                @Override
                                                public void onNext(Batch_user batch_user) {
                                                    Toast.makeText(FollowActivity.this, "查询成功2，粉丝的个数为： " + batch_user.getData().getUsers().size(), Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onComplete() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Log.v("error", e.toString());
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }catch (Exception o) {
            Toast.makeText(FollowActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void check_internet() {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(FollowActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
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
