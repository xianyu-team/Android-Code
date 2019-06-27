package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import retrofit2.http.GET;

public class MoneyHistoryActivity extends AppCompatActivity {
    ImageView back;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_history);

        back = findViewById(R.id.back);
        listView = findViewById(R.id.money_history);
        initData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

            getHistory service = retrofit.create(getHistory.class);
            service.getMessage()
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<Confirm_packet_login>() {
                        @Override
                        public void onNext(Confirm_packet_login confirm_packet_login) {
                            MoneyHistoryItemAdapter adapter = new MoneyHistoryItemAdapter(confirm_packet_login.getData().getBills(), getBaseContext());
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MoneyHistoryActivity.this, "访问失败："+ e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: "+e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception o){
            Toast.makeText(MoneyHistoryActivity.this, "抛出异常："+ o.toString(), Toast.LENGTH_SHORT).show();
            Log.i("test", "onError: "+o.toString());
        }
    }

    public class MoneyHistoryItemAdapter extends BaseAdapter {
        ArrayList<Confirm_packet_login.Bills> data;
        Context context;
        TextView time;
        TextView bill_description;
        TextView value;



        MoneyHistoryItemAdapter(ArrayList<Confirm_packet_login.Bills> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Confirm_packet_login.Bills getItem(int position) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.moneyhistoryitem, null);
            }
            time = convertView.findViewById(R.id.time);
            bill_description = convertView.findViewById(R.id.bill_description);
            value = convertView.findViewById(R.id.value);

            time.setText(data.get(position).getBill_time());
            bill_description.setText(data.get(position).getBill_description());
            if (data.get(position).getBill_type() == 0) {
                value.setText("+ " + data.get(position).getBill_number());
            }

            else {
                value.setText("- " + data.get(position).getBill_number());
            }


            return convertView;
        }
    }

    public interface getHistory{
        @GET("/bill")
        Observable<Confirm_packet_login> getMessage();
    }
}
