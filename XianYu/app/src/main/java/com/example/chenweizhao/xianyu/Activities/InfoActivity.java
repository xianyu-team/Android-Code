package com.example.chenweizhao.xianyu.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
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
import retrofit2.http.PUT;

public class InfoActivity extends AppCompatActivity {

    ImageView back;
    Button modify;
    ImageView profile;
    ImageView profileChoose;
    TextView accountNumber;
    EditText name;
    EditText schoolName;
    EditText departmentName;
    EditText idNumber;
    RadioGroup gender;
    RadioButton genderMale;
    RadioButton genderFemale;
    Bitmap pro = null;
    String pro1 = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        requestWritePermission();
        init();
        get_info();
        set_click();
    }

    private void init() {
        back = findViewById(R.id.information_back);
        modify = findViewById(R.id.information_modify);
        profile = findViewById(R.id.information_profile);
        profileChoose = findViewById(R.id.information_profile_choose);
        accountNumber = findViewById(R.id.information_account_number);
        name = findViewById(R.id.information_name);
        schoolName = findViewById(R.id.information_school_name);
        departmentName = findViewById(R.id.information_department_name);
        idNumber = findViewById(R.id.information_id_number);
        gender = findViewById(R.id.information_gender);
        genderMale = findViewById(R.id.information_gender_male);
        genderFemale = findViewById(R.id.information_gender_female);
    }

    private void set_click() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在这里跳转到手机系统相册里面
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_internet();
                String user_icon;
                if (pro != null) {
                    user_icon = bitmapToString(pro);
                } else {
                    user_icon = pro1;
                }
                String student_name = name.getText().toString();
                String student_university = schoolName.getText().toString();
                String student_academy = departmentName.getText().toString();
                String student_number = idNumber.getText().toString();
                int student_gender = genderMale.isChecked() ? 1 : 0;
                ModifyInfoPacket modifyInfoPacket = new ModifyInfoPacket(user_icon, student_name, student_university, student_academy, student_number, student_gender);

                try {
                    OkHttpClient build = new OkHttpClient.Builder()
                            .connectTimeout(2, TimeUnit.SECONDS)
                            .readTimeout(2, TimeUnit.SECONDS)
                            .writeTimeout(2, TimeUnit.SECONDS)
                            .addInterceptor(new InfoActivity.ReceivedCookiesInterceptor())
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://120.77.146.251:8000")

                            .addConverterFactory(GsonConverterFactory.create())

                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置

                            // build 即为okhttp声明的变量，下文会讲
                            .client(build)

                            .build();

                    final InfoActivity.ModifyInfoInterface service = retrofit.create(InfoActivity.ModifyInfoInterface.class);

                    service.postModify(modifyInfoPacket)
                            .subscribeOn(Schedulers.io())//请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                            .subscribe(new DisposableObserver<InfoActivity.ConfirmPacket>() {
                                @Override
                                public void onNext(InfoActivity.ConfirmPacket confirmPacket) {
                                    if (confirmPacket.getCode() == 200) {
                                        Toast.makeText(InfoActivity.this, "修改成功：" + confirmPacket.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(InfoActivity.this, "修改失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i("test", "onError: " + e.toString());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                } catch (Exception o) {
                    Toast.makeText(InfoActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();
                    Log.v("test", "onError: " + o.toString());
                }
            }
        });
    }


    private void check_internet() {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(InfoActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void get_info() {
        check_internet();
        try {
            OkHttpClient build = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .writeTimeout(2, TimeUnit.SECONDS)
                    .addInterceptor(new InfoActivity.ReceivedCookiesInterceptor())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://120.77.146.251:8000")

                    .addConverterFactory(GsonConverterFactory.create())

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//新的配置

                    // build 即为okhttp声明的变量，下文会讲
                    .client(build)

                    .build();

            final InfoActivity.ModifyInfoInterface service = retrofit.create(InfoActivity.ModifyInfoInterface.class);

            service.getUserProfile()
                    .subscribeOn(Schedulers.io())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())         //请求完成后在主线程中执行
                    .subscribe(new DisposableObserver<User_profile_packet>() {
                        @Override
                        public void onNext(User_profile_packet user_profile_packet) {
                            if (user_profile_packet.getCode() == 200) {
                                pro1 = user_profile_packet.getData().getUser().getUser_icon();
                                profile.setImageBitmap(stringToBitmap(user_profile_packet.getData().getUser().getUser_icon()));
                                accountNumber.setText(user_profile_packet.getData().getUser().getUser_phone());
                                name.setText(user_profile_packet.getData().getStudent().getStudent_name());
                                schoolName.setText(user_profile_packet.getData().getStudent().getStudent_university());
                                departmentName.setText(user_profile_packet.getData().getStudent().getStudent_academy());
                                idNumber.setText(user_profile_packet.getData().getStudent().getStudent_number());
                                if (user_profile_packet.getData().getStudent().getStudent_gender() == 1) {
                                    genderMale.setChecked(true);
                                    genderFemale.setChecked(false);
                                }
                                else {
                                    genderMale.setChecked(false);
                                    genderFemale.setChecked(true);
                                }
                            } else {
                                Toast.makeText(InfoActivity.this, "查询失败: " + user_profile_packet.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(InfoActivity.this, "访问失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("test", "onError: " + e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } catch (Exception o) {
            Toast.makeText(InfoActivity.this, "抛出异常：" + o.toString(), Toast.LENGTH_SHORT).show();
            Log.v("test", "onError: " + o.toString());
        }
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        pro = bitmap;
                        profile.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
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

        private String bitmapToString (Bitmap src){
            String string = "";
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] bytes = bStream.toByteArray();
            string = Base64.encodeToString(bytes, Base64.DEFAULT);
            return string;
        }

        private void requestWritePermission () {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        public interface ModifyInfoInterface {
            @PUT("/user/profile")
            Observable<InfoActivity.ConfirmPacket> postModify(@Body ModifyInfoPacket modifyInfoPacket);

            @GET("/user/profile")
            Observable<User_profile_packet> getUserProfile();
        }

        public class ConfirmPacket implements Serializable {

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
