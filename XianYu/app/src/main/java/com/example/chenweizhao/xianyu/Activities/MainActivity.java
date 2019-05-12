package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
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
    }
}
