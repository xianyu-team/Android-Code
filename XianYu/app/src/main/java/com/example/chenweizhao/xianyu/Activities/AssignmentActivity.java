package com.example.chenweizhao.xianyu.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.R;

import java.util.ArrayList;
import java.util.List;

public class AssignmentActivity extends AppCompatActivity {
    boolean isPut;
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
        set_click();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        isPut = bundle.getBoolean("isPut");
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

    private void set_click() {
        assignmentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
