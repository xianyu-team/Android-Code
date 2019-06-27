package com.example.chenweizhao.xianyu.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.Fragments.TaskFragment;
import com.example.chenweizhao.xianyu.R;

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
import retrofit2.http.POST;
import retrofit2.http.Path;

public class TaskDetailActivity extends AppCompatActivity {
    ImageView mUserImage;
    TextView mSchool;
    TextView mCollege;
    TextView mName;
    TextView mNumber;
    TextView task_punish_time;
    TextView task_sketch;
    TextView task_detail;
    TextView task_value;

    Button like;
    Button get;
    private int task_id;
    private int user_id;
    private String sketch;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        init();
        initData();

    }

    void init() {
        Intent intent = getIntent();
        task_id = intent.getIntExtra("task_id", -1);
        user_id = intent.getIntExtra("user_id", -1);
        sketch = intent.getStringExtra("sketch");
        mUserImage = findViewById(R.id.user_image);
        mSchool = findViewById(R.id.school);
        mCollege = findViewById(R.id.college);
        mName = findViewById(R.id.user_name);
        mNumber = findViewById(R.id.school_number);
        task_punish_time = findViewById(R.id.begin_time);
        task_sketch = findViewById(R.id.detail_info);
        task_detail = findViewById(R.id.secret_info);
        task_value = findViewById(R.id.value);

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
                                task_punish_time.setText(confirm_packet.getData().getDelivery().delivery_complishDate);
                                task_value.setText(confirm_packet.getData().getDelivery().delivery_complished + "");
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


}
