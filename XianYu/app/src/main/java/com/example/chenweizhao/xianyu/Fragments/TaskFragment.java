package com.example.chenweizhao.xianyu.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.Activities.Confirm_packet_login;
import com.example.chenweizhao.xianyu.Activities.FindPasswordActivity1;
import com.example.chenweizhao.xianyu.Activities.FindPasswordActivity2_ChangePassword;
import com.example.chenweizhao.xianyu.Activities.MainActivity;
import com.example.chenweizhao.xianyu.Activities.ReceivedCookiesInterceptor;
import com.example.chenweizhao.xianyu.Activities.Senddeliverypacket;
import com.example.chenweizhao.xianyu.Activities.TaskDetailActivity;
import com.example.chenweizhao.xianyu.Activities.TaskListPacket;
import com.example.chenweizhao.xianyu.Activities.TaskSketch;
import com.example.chenweizhao.xianyu.R;

import java.util.ArrayList;
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

public class TaskFragment extends Fragment {
    private RadioGroup mRadioGroup;
    private RadioButton mFollowButton, mNewButton, mValueButton;
    private ListView listView;
    private TaskItemAdapter taskItemAdapter;
    private Boolean isFirst = true;
    private int mButtonType = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task, container, false);
        mRadioGroup = view.findViewById(R.id.radioGroup_task);
        mFollowButton = view.findViewById(R.id.like_task);
        mNewButton = view.findViewById(R.id.new_task);
        mValueButton = view.findViewById(R.id.money_task);
        listView = view.findViewById(R.id.all_task);
        setOnclick();
        mNewButton.performClick();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            return;
        }
        if (mButtonType == 0) {
            NewButtonClick();
        }
        else if (mButtonType == 1) {
            FollowButtonClick();
        }
        else if (mButtonType == 2) {
            ValueButtonClick();
        }
        else;

    }

    void setOnclick() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mFollowButton.getId()) {
                    mFollowButton.setTextColor(Color.parseColor("#000000"));
                    mNewButton.setTextColor(Color.parseColor("#8A000000"));
                    mValueButton.setTextColor(Color.parseColor("#8A000000"));
                    mButtonType = 1;
                    FollowButtonClick();

                }
                else if (checkedId == mNewButton.getId()) {
                    mFollowButton.setTextColor(Color.parseColor("#8A000000"));
                    mNewButton.setTextColor(Color.parseColor("#000000"));
                    mValueButton.setTextColor(Color.parseColor("#8A000000"));
                    mButtonType = 0;
                    NewButtonClick();
                }
                else if (checkedId == mValueButton.getId()) {
                    mFollowButton.setTextColor(Color.parseColor("#8A000000"));
                    mNewButton.setTextColor(Color.parseColor("#8A000000"));
                    mValueButton.setTextColor(Color.parseColor("#000000"));
                    mButtonType = 2;
                    ValueButtonClick();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (taskItemAdapter.data.get(position).getTask_type() == 1) {
                    Toast.makeText(getContext(), "问卷跳转找闲鱼", Toast.LENGTH_SHORT).show();
                }
                else{
                    int task_id = taskItemAdapter.data.get(position).getTask_id();
                    int user_id = taskItemAdapter.data.get(position).getUser_id();
                    String sketch = taskItemAdapter.data.get(position).getTask_sketch();
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra("task_id", task_id);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("sketch", sketch);
                    startActivity(intent);
                }
            }
        });
    }

    public class TaskItemAdapter extends BaseAdapter {
        ArrayList<TaskSketch> data;
        Context context;
        TextView task_type;
        TextView task_punish_time;
        TextView task_sketch;
        TextView task_value;

        void UpdateData(ArrayList<TaskSketch> newdata) {
            if (newdata == null) {
                data.clear();
                return;
            }
            data.clear();
            data.addAll(newdata);
        }

        TaskItemAdapter(ArrayList<TaskSketch> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public TaskSketch getItem(int position) {
            if (data.get(position) == null) {
                return null;
            }
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.tasklistitem, null);
            }
            task_type = convertView.findViewById(R.id.task_type);
            task_punish_time = convertView.findViewById(R.id.task_punish_time);
            task_sketch = convertView.findViewById(R.id.task_sketch);
            task_value = convertView.findViewById(R.id.task_value);

            task_punish_time.setText(data.get(position).getTask_publishDate());
            task_sketch.setText(data.get(position).getTask_sketch());
            task_value.setText(data.get(position).getTask_bonus() + " 闲余币");
            if (data.get(position).getTask_type() == 0) {
                task_type.setText("跑腿任务");
            }else if (data.get(position).getTask_type() == 1){
                task_type.setText("问卷任务");
            }
            else{
                task_type.setText("未知任务");
            }
            return convertView;
        }
    }

    public interface GetTaskListInterface{
        @GET("/task/{t_type}")
        Observable<TaskListPacket> getTaskList(@Path("t_type") int type);
    }

    void FollowButtonClick(){
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

            GetTaskListInterface service = retrofit.create(GetTaskListInterface.class);
            service.getTaskList(1)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<TaskListPacket>() {
                        @Override
                        public void onNext(TaskListPacket confirm_packet) {
                            if(confirm_packet.getCode() == 200){
                                taskItemAdapter.UpdateData(confirm_packet.getData().getTasks());
                                taskItemAdapter.notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(getContext(), confirm_packet.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getContext(), "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(getContext(), "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }

    void NewButtonClick() {
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

            GetTaskListInterface service = retrofit.create(GetTaskListInterface.class);
            service.getTaskList(0)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<TaskListPacket>() {
                        @Override
                        public void onNext(TaskListPacket confirm_packet) {
                            if(confirm_packet.getCode() == 200){
                                if (isFirst) {
                                    taskItemAdapter = new TaskItemAdapter(confirm_packet.getData().getTasks(), getContext());
                                    listView.setAdapter(taskItemAdapter);
                                    isFirst = false;
                                }
                                else {
                                    taskItemAdapter.UpdateData(confirm_packet.getData().getTasks());
                                    taskItemAdapter.notifyDataSetChanged();
                                }
                            }
                            else {
                                Toast.makeText(getContext(), confirm_packet.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getContext(), "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(getContext(), "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }
    void ValueButtonClick() {
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

            GetTaskListInterface service = retrofit.create(GetTaskListInterface.class);
            service.getTaskList(2)
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<TaskListPacket>() {
                        @Override
                        public void onNext(TaskListPacket confirm_packet) {
                            if(confirm_packet.getCode() == 200){
                                taskItemAdapter.UpdateData(confirm_packet.getData().getTasks());
                                taskItemAdapter.notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(getContext(), confirm_packet.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getContext(), "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(getContext(), "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }

}
