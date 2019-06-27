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

public class AssignmentActivity extends AppCompatActivity {
    boolean isPut;
    int userID;
    ImageView assignmentBack;
    TextView assignmentTitle;
    ListView assignmentItems;
    List<AssignmentCommon> assignmentCommons = new ArrayList<>();
    AssignmentAdapter assignmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        init();
        get_info();
        set_click();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        isPut = bundle.getBoolean("isPut");
        userID = bundle.getInt("userID");
        assignmentBack = findViewById(R.id.assignment_back);
        assignmentTitle = findViewById(R.id.assignment_title);
        assignmentItems = findViewById(R.id.assignment_items);
        if(isPut) {
            assignmentTitle.setText("发布的任务");

        }
        else {
            assignmentTitle.setText("领取的任务");
        }
        assignmentCommons.add(new AssignmentCommon(1, 1, 0, "测试1", 20, "2019-6-18" ));
        assignmentCommons.add(new AssignmentCommon(1, 2, 0, "测试2", 20, "2019-6-19" ));
        assignmentAdapter = new AssignmentAdapter(AssignmentActivity.this, R.layout.item_assignment, assignmentCommons);
        assignmentItems.setAdapter(assignmentAdapter);
    }

    private void get_info() {
        check_internet();
        try {
            OkHttpClient build = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .writeTimeout(2, TimeUnit.SECONDS)
                    .addInterceptor(new AssignmentActivity.ReceivedCookiesInterceptor())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://120.77.146.251:8000")

                    .addConverterFactory(GsonConverterFactory.create())

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置

                    // build 即为okhttp声明的变量，下文会讲
                    .client(build)

                    .build();

            final AssignmentActivity.taskInterface service = retrofit.create(AssignmentActivity.taskInterface.class);

            int task_type;
            if (isPut) {
                task_type = 0;
            }
            else {
                task_type = 1;
            }
            if(userID == -1) {
                service.getTask(task_type)
                        .subscribeOn(Schedulers.io())//请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                        .subscribe(new DisposableObserver<Assignment>() {
                            @Override
                            public void onNext(Assignment assignment) {
                                if (assignment.getCode() == 200) {
                                    Toast.makeText(AssignmentActivity.this, "查询成功,任务数为：" + assignment.getData().getTasks().size(), Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < assignment.getData().getTasks().size(); i++) {
                                        assignmentCommons.add(new AssignmentCommon(assignment.getData().getTasks().get(i).getUser_id(),
                                                assignment.getData().getTasks().get(i).getTask_id(), assignment.getData().getTasks().get(i).getTask_type(),
                                                assignment.getData().getTasks().get(i).getTask_sketch(), assignment.getData().getTasks().get(i).getTask_bonus(),
                                                assignment.getData().getTasks().get(i).getTask_publishDate()
                                        ));
                                        assignmentAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(AssignmentActivity.this, "查询失败: " + assignment.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(AssignmentActivity.this, "访问失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.i("test", "onError: " + e.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
            else {
                service.getTaskByID(userID, task_type)
                        .subscribeOn(Schedulers.io())//请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                        .subscribe(new DisposableObserver<Assignment>() {
                            @Override
                            public void onNext(Assignment assignment) {
                                if (assignment.getCode() == 200) {
                                    Toast.makeText(AssignmentActivity.this, "查询成功,任务数为：" + assignment.getData().getTasks().size(), Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < assignment.getData().getTasks().size(); i++) {
                                        assignmentCommons.add(new AssignmentCommon(assignment.getData().getTasks().get(i).getUser_id(),
                                                assignment.getData().getTasks().get(i).getTask_id(), assignment.getData().getTasks().get(i).getTask_type(),
                                                assignment.getData().getTasks().get(i).getTask_sketch(), assignment.getData().getTasks().get(i).getTask_bonus(),
                                                assignment.getData().getTasks().get(i).getTask_publishDate()
                                        ));
                                        assignmentAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(AssignmentActivity.this, "查询失败: " + assignment.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(AssignmentActivity.this, "访问失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.i("test", "onError: " + e.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

        }
        catch (Exception o) {
            Toast.makeText(AssignmentActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void set_click() {
        assignmentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void check_internet() {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(AssignmentActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public interface taskInterface {
        @GET ("/user/tasks/{t_type}")
        Observable<Assignment> getTask(@Path("t_type") int type);

        @GET ("/user/{u_id}/tasks/{t_type}")
        Observable<Assignment> getTaskByID(@Path("u_id")int u_id, @Path("t_type") int type);
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


}
