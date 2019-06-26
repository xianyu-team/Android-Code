package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
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

public class AccountActivity extends AppCompatActivity {
    CircleImageView profile;
    TextView nickName;
    TextView university;
    ImageView editInfo;
    TextView follow;
    TextView follower;
    ImageView assignmentPut;
    ImageView assignmentGet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account);
        init();
        getInfo();
        set_click();
    }

    private void init() {
        profile = findViewById(R.id.account_profile);
        nickName = findViewById(R.id.account_nickname);
        university = findViewById(R.id.account_university);
        editInfo = findViewById(R.id.account_right);
        follow = findViewById(R.id.account_follow_number);
        follower = findViewById(R.id.account_follower_number);
        assignmentPut = findViewById(R.id.account_assignment_put_right);
        assignmentGet = findViewById(R.id.account_assignment_get_right);
    }

    private void getInfo() {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(AccountActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }

        Login_packet login_packet = new Login_packet("15989061915", "123456");
//        Login_packet login_packet = new Login_packet("15016561536", "15016561536");

        try {
            OkHttpClient build = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .writeTimeout(2, TimeUnit.SECONDS)
                    .addInterceptor(new AccountActivity.ReceivedCookiesInterceptor())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://120.77.146.251:8000")

                    .addConverterFactory(GsonConverterFactory.create())

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置

                    // build 即为okhttp声明的变量，下文会讲
                    .client(build)

                    .build();


            final AccountActivity.LoginInterface service = retrofit.create(AccountActivity.LoginInterface.class);

            service.postRegister(login_packet)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Confirm_packet_login>() {
                        @Override
                        public void onNext(Confirm_packet_login confirm_packet_login) {
                            if (confirm_packet_login.getCode() == 200) {
                                service.getUserProfile()
                                        .subscribeOn(Schedulers.io())//请求在新的线程中执行
                                        .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                                        .subscribe(new DisposableObserver<User_profile_packet>() {
                                            @Override
                                            public void onNext(User_profile_packet user_profile_packet) {
                                                if (user_profile_packet.getCode() == 200) {
                                                    profile.setImageBitmap(stringToBitmap(user_profile_packet.getData().getUser().getUser_icon()));
                                                    nickName.setText(user_profile_packet.getData().getStudent().getStudent_name());
                                                    university.setText(user_profile_packet.getData().getStudent().getStudent_university());
                                                } else {
                                                    Toast.makeText(AccountActivity.this, "查询失败: " + user_profile_packet.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Toast.makeText(AccountActivity.this, "访问失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                                Log.i("test", "onError: " + e.toString());
                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });

                                service.getFollowings()
                                        .subscribeOn(Schedulers.io())//请求在新的线程中执行
                                        .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                                        .subscribe(new DisposableObserver<Following_packets>() {
                                            @Override
                                            public void onNext(Following_packets following_packets) {
                                                if (following_packets.getCode() == 200 ) {
                                                    String s = "关注了" + following_packets.getData().getFollowings().size() + "人";
                                                    follow.setText(s);
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });

                            } else {
                                Toast.makeText(AccountActivity.this, "失败: " + confirm_packet_login.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(AccountActivity.this, "访问失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: " + e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });


        } catch (Exception o) {
            Toast.makeText(AccountActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void set_click() {
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        assignmentPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPut", true);
                Intent intent = new Intent(AccountActivity.this, AssignmentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        assignmentGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPut", false);
                Intent intent = new Intent(AccountActivity.this, AssignmentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFollow", true);
                Intent intent = new Intent(AccountActivity.this, FollowActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFollow", false);
                Intent intent = new Intent(AccountActivity.this, FollowActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private Bitmap stringToBitmap (String src){
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(src, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public interface LoginInterface {
        @POST("/user/password/session")
        Observable<Confirm_packet_login> postRegister(@Body Login_packet login_packet);

        @GET("/user/profile")
        Observable<User_profile_packet> getUserProfile();

        @GET("/user/followings")
        Observable<Following_packets> getFollowings();
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
