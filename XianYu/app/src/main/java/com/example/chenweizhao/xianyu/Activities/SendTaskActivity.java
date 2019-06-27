package com.example.chenweizhao.xianyu.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;


public class SendTaskActivity extends AppCompatActivity {
    private int taskId;
    private int taskType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_task);
    }

}
