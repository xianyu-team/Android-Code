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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class FollowDetailActivity extends AppCompatActivity {
    private int userId;
    boolean hasFollow = false;
    CircleImageView profile;
    TextView nickname;
    TextView school;
    TextView department;
    TextView id;
    ImageView right;
    ImageView back;
    Button follow_btn;
    TextView follow_num;
    TextView fan_num;
    /**
     * code : 200
     * message : OK
     * data : {}
     */

    private int code;
    private String message;
    private DataBean data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_detail);
        init();
        getInfo();
        set_click();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");
        //Toast.makeText(FollowDetailActivity.this, "userId" + userId, Toast.LENGTH_SHORT ).show();
        profile = findViewById(R.id.follow_detail_profile);
        nickname = findViewById(R.id.follow_detail_nickname);
        school = findViewById(R.id.follow_detail_school);
        department = findViewById(R.id.follow_detail_department);
        id = findViewById(R.id.follow_detail_id);
        right = findViewById(R.id.detail_follow_right);
        back = findViewById(R.id.follow_detail_back);
        follow_btn = findViewById(R.id.follow_detail_follow_btn);
        follow_num = findViewById(R.id.detail_follow_number);
        fan_num = findViewById(R.id.detail_follower_number);
    }

    private void set_click() {
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        follow_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFollow", true);
                bundle.putInt("userID", userId);
                Intent intent = new Intent(FollowDetailActivity.this, FollowActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        fan_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFollow", false);
                bundle.putInt("userID", userId);
                Intent intent = new Intent(FollowDetailActivity.this, FollowActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    OkHttpClient build = new OkHttpClient.Builder()
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .writeTimeout(5, TimeUnit.SECONDS)
                            .addInterceptor(new ReceivedCookiesInterceptor())
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://120.77.146.251:8000")

                            .addConverterFactory(GsonConverterFactory.create())

                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置

                            // build 即为okhttp声明的变量，下文会讲
                            .client(build)

                            .build();

                    final getFollow service = retrofit.create(getFollow.class);
                    if (hasFollow) {
                        service.unFollow(new user(userId))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<confirmPacket>() {
                                    @Override
                                    public void onNext(confirmPacket confirmPacket) {
                                        if (confirmPacket.getCode() == 200) {
                                            follow_btn.setText("关注");
                                        } else {
                                            Toast.makeText(FollowDetailActivity.this, "取关失败：" , Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                    } else {
                        service.follow(new user(userId))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<confirmPacket>() {
                                    @Override
                                    public void onNext(confirmPacket confirmPacket) {
                                        if (confirmPacket.getCode() == 200) {
                                            follow_btn.setText("取消关注");
                                        } else {
                                            Toast.makeText(FollowDetailActivity.this, "关注失败：" , Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                    }
                } catch (Exception o) {
                    Toast.makeText(FollowDetailActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userID", userId);
                bundle.putBoolean("isPut", true);
                Intent intent = new Intent(FollowDetailActivity.this, AssignmentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getInfo() {
        check_internet();
        try {
            OkHttpClient build = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(new ReceivedCookiesInterceptor())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://120.77.146.251:8000")

                    .addConverterFactory(GsonConverterFactory.create())

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置

                    // build 即为okhttp声明的变量，下文会讲
                    .client(build)

                    .build();

            final getFollow service = retrofit.create(getFollow.class);

            List<Batch_Id.UserIdsBean> temp = new ArrayList<>();
            Batch_Id.UserIdsBean userIdsBean = new Batch_Id.UserIdsBean();
            userIdsBean.setUser_id(2);
            temp.add(userIdsBean);
            Batch_Id batch_id = new Batch_Id();
            batch_id.setUser_ids(temp);
            service.getUsers(batch_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Batch_user>() {
                        @Override
                        public void onNext(Batch_user batch_user) {
                            profile.setImageBitmap(stringToBitmap(batch_user.getData().getUsers().get(0).getUser().getUser_icon()));
                            nickname.setText(batch_user.getData().getUsers().get(0).getStudent().getStudent_name());
                            school.setText(batch_user.getData().getUsers().get(0).getStudent().getStudent_university());
                            department.setText(batch_user.getData().getUsers().get(0).getStudent().getStudent_academy());
                            id.setText(batch_user.getData().getUsers().get(0).getStudent().getStudent_number());
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(FollowDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            Log.v("error", e.toString());
                        }
                    });

            service.getFollowingsId()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Following_packets>() {
                        @Override
                        public void onNext(Following_packets following_packets) {
                           for (int i = 0; i < following_packets.getData().getFollowings().size(); i++) {
                               if (userId != following_packets.getData().getFollowings().get(i).getFollowing_id()) {
                                   hasFollow = false;
                               }
                               else {
                                   hasFollow = true;
                                   break;
                               }
                           }
                           if (hasFollow) {
                               follow_btn.setText("取消关注");
                           }
                           else {
                               follow_btn.setText("关注");
                           }
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

            service.getFollowingsIdByID(userId)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Following_packets>() {
                        @Override
                        public void onNext(Following_packets following_packets) {
                            if (following_packets.getCode() == 200) {
                                String temp = "关注了" + following_packets.getData().getFollowings().size() + "人";
                                follow_num.setText(temp);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

            service.getFansIdByID(userId)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Fan_packets>() {
                        @Override
                        public void onNext(Fan_packets fan_packets) {
                            if (fan_packets.getCode() == 200) {
                                String temp = "关注者" + fan_packets.getData().getFans().size() + "人";
                                fan_num.setText(temp);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });


        }catch (Exception o) {
            Toast.makeText(FollowDetailActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();

        }


    }

    private void check_internet() {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(FollowDetailActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
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

    public interface getFollow {
        @GET("/user/followings")
        Observable<Following_packets> getFollowingsId();

        @GET("/user/fans")
        Observable<Fan_packets> getFansId();

        @GET("/user/{ID}/fans")
        Observable<Fan_packets> getFansIdByID(@Path("ID") int id);

        @GET("/user/{ID}/followings")
        Observable<Following_packets> getFollowingsIdByID(@Path("ID") int id);

        @POST("/user/batch/information")
        Observable<Batch_user> getUsers(@Body Batch_Id batch_id);

        @POST ("/user/following")
        Observable<confirmPacket> follow(@Body user user);

        @HTTP(method = "DELETE", path = "/user/following", hasBody = true)
        Observable<confirmPacket> unFollow(@Body user user);


    }

    public class confirmPacket implements Serializable{

        /**
         * code : 200
         * message : OK
         * data : {}
         */

        private int code;
        private String message;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public class DataBean {
        }
    }

    public class user implements Serializable {

        /**
         * user_id : 1
         */

        public user(int user_id) {
            setUser_id(user_id);
        }

        private int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
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

}
