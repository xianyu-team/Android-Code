package com.example.chenweizhao.xianyu.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;

public class FollowDetailActivity extends AppCompatActivity {
    private int userId;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_detail);
        init();
        set_click();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");
        Toast.makeText(FollowDetailActivity.this, "userId" + userId, Toast.LENGTH_SHORT ).show();
        back = findViewById(R.id.follow_detail_back);
    }

    private void set_click() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
