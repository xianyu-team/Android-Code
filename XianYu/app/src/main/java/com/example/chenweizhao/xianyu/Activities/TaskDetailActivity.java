package com.example.chenweizhao.xianyu.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.Fragments.TaskFragment;
import com.example.chenweizhao.xianyu.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class TaskDetailActivity extends AppCompatActivity {
    ImageView back;
    ImageView mUserImage;
    TextView mSchool;
    TextView mCollege;
    TextView mName;
    TextView mNumber;
    TextView task_punish_time;
    TextView task_end_time;
    TextView task_sketch;
    TextView task_detail;
    TextView task_value;

    Button like;
    Button get;
    private int task_id;
    private int user_id;
    private String time;
    private int value;
    private String sketch;

    private int picked = 0;

    private Boolean hasFollow = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        init();
        initData();
        setClick();
    }

    void init() {
        Intent intent = getIntent();
        task_id = intent.getIntExtra("task_id", -1);
        user_id = intent.getIntExtra("user_id", -1);
        sketch = intent.getStringExtra("sketch");
        time = intent.getStringExtra("time");
        value = intent.getIntExtra("value",-1);
        mUserImage = findViewById(R.id.user_image);
        mSchool = findViewById(R.id.school);
        mCollege = findViewById(R.id.college);
        mName = findViewById(R.id.user_name);
        mNumber = findViewById(R.id.school_number);
        task_punish_time = findViewById(R.id.begin_time);
        task_end_time = findViewById(R.id.end_time);
        task_sketch = findViewById(R.id.detail_info);
        task_detail = findViewById(R.id.secret_info);
        task_value = findViewById(R.id.value);
        back = findViewById(R.id.back);
        like = findViewById(R.id.like);
        get = findViewById(R.id.accept_task);
    }

    void initData() {



        //任务信息
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

            TaskDetailInterface service = retrofit.create(TaskDetailInterface.class);
            service.setTaskId(task_id)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<ConfirmTaskDeatil>() {
                        @Override
                        public void onNext(ConfirmTaskDeatil confirm_packet) {
                            if(confirm_packet.getCode() == 200){
                                task_sketch.setText(sketch);
                                task_detail.setText(confirm_packet.getData().getDelivery().delivery_detail);
                                task_punish_time.setText(time);
                                task_value.setText(value + " ");
                                if (confirm_packet.getData().getDelivery().getDelivery_complished() == 1) {
                                    task_end_time.setText(confirm_packet.getData().getDelivery().getDelivery_complishDate());
                                }
                                else {
                                    task_end_time.setText("任务还未完成");
                                }
                                picked = confirm_packet.getData().getDelivery().getDelivery_picked();
                                if (picked == 1) {
                                    get.setText("已被接受");
                                }
                            }
                            else {
                                Toast.makeText(TaskDetailActivity.this, confirm_packet.getMessage()+"111", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(TaskDetailActivity.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(TaskDetailActivity.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }

        //个人信息
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

            Batch_Id.UserIdsBean data = new Batch_Id.UserIdsBean();
            data.setUser_id(user_id);
            ArrayList<Batch_Id.UserIdsBean> user_ids = new ArrayList<Batch_Id.UserIdsBean>();
            user_ids.add(data);
            Batch_Id batch_id = new Batch_Id();
            batch_id.setUser_ids(user_ids);
            UserDetailInterface service = retrofit.create(UserDetailInterface.class);
            service.getUsers(batch_id)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Batch_user>() {
                        @Override
                        public void onNext(Batch_user confirm_packet) {
                            if(confirm_packet.getCode() == 200){
                                mUserImage.setImageBitmap(stringToBitmap(confirm_packet.getData().getUsers().get(0).getUser().getUser_icon()));
                                mName.setText(confirm_packet.getData().getUsers().get(0).getStudent().getStudent_name());
                                mSchool.setText(confirm_packet.getData().getUsers().get(0).getStudent().getStudent_university());
                                mCollege.setText(confirm_packet.getData().getUsers().get(0).getStudent().getStudent_academy());
                                mNumber.setText(confirm_packet.getData().getUsers().get(0).getStudent().getStudent_number());
                            }
                            else {
                                //Toast.makeText(TaskDetailActivity.this, confirm_packet.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(TaskDetailActivity.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(TaskDetailActivity.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }

        //判断是否关注
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

            getFollow service = retrofit.create(getFollow.class);
            service.getFollowingsId()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Following_packets>() {
                        @Override
                        public void onNext(Following_packets following_packets) {
                            for (int i = 0; i < following_packets.getData().getFollowings().size(); i++) {
                                if (user_id != following_packets.getData().getFollowings().get(i).getFollowing_id()) {
                                    hasFollow = false;
                                }
                                else {
                                    hasFollow = true;
                                    break;
                                }
                            }
                            if (hasFollow) {
                                like.setText("取消关注");
                            }
                            else {
                                like.setText("关注");
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
        catch (Exception o){
            Toast.makeText(TaskDetailActivity.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
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

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picked == 1) {

                }
                else {

                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
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

                    getFollow service = retrofit.create(getFollow.class);
                    if (hasFollow) {
                        service.unFollow(new user(user_id))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<FollowDetailActivity.confirmPacket>() {
                                    @Override
                                    public void onNext(FollowDetailActivity.confirmPacket confirmPacket) {
                                        if (confirmPacket.getCode() == 200) {
                                            like.setText("关注");
                                            hasFollow = !hasFollow;
                                        } else {
                                            Toast.makeText(TaskDetailActivity.this, "取关失败：" , Toast.LENGTH_SHORT).show();
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
                        service.follow(new user(user_id))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<FollowDetailActivity.confirmPacket>() {
                                    @Override
                                    public void onNext(FollowDetailActivity.confirmPacket confirmPacket) {
                                        if (confirmPacket.getCode() == 200) {
                                            like.setText("取消关注");
                                            hasFollow = !hasFollow;
                                        } else {
                                            Toast.makeText(TaskDetailActivity.this, "关注失败：" , Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TaskDetailActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();
                }
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


    public interface TaskDetailInterface{
        @GET("/task/delivery/detail/{task_id}")
        Observable<ConfirmTaskDeatil> setTaskId(@Path("task_id") int task_id);
    }

    public interface UserDetailInterface{
        @POST("/user/batch/information")
        Observable<Batch_user> getUsers(@Body Batch_Id batch_id);

    }

    public interface getFollow {
        @GET("/user/followings")
        Observable<Following_packets> getFollowingsId();

        @POST ("/user/following")
        Observable<FollowDetailActivity.confirmPacket> follow(@Body user user);

        @HTTP(method = "DELETE", path = "/user/following", hasBody = true)
        Observable<FollowDetailActivity.confirmPacket> unFollow(@Body user user);


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
}
