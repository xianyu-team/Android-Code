package com.example.chenweizhao.xianyu.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.Fragments.TaskFragment;
import com.example.chenweizhao.xianyu.R;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment taskFragment;
    ImageView write;
    PopupWindow popupWindow;
    View popwindow_view;
    ConstraintLayout mDelivery;
    ConstraintLayout mOther;
    ConstraintLayout mQuestionnaire;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        set_click();
    }

    void init(){
        //
        write = findViewById(R.id.write);


        //
        taskFragment = new TaskFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, taskFragment);
        fragmentTransaction.commit();


        //
        popwindow_view = getLayoutInflater().inflate(R.layout.popwindows_write, null, true);
        mDelivery = popwindow_view.findViewById(R.id.take_expressage);
        mQuestionnaire = popwindow_view.findViewById(R.id.quesionnaire);
        mOther = popwindow_view.findViewById(R.id.other);
    }

    void set_click(){


        //
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new PopupWindow(popwindow_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setAnimationStyle(1);
                popupWindow.showAsDropDown(write, 0,0);
            }
        });
        mDelivery.setOnClickListener(new PopupWindowClick());
        mOther.setOnClickListener(new PopupWindowClick());
        mQuestionnaire.setOnClickListener(new PopupWindowClick());

    }

    private class PopupWindowClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.take_expressage:
                    Intent intent =  new Intent(MainActivity.this, SendTaskActivity.class);
                    startActivity(intent);
                    break;
                case R.id.quesionnaire:
                    Toast.makeText(getBaseContext(), "问卷功能找闲鱼", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.other:
                    Toast.makeText(getBaseContext(), "其他功能暂未开放", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
    }
}
