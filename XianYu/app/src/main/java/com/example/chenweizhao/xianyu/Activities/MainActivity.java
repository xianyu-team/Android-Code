package com.example.chenweizhao.xianyu.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.chenweizhao.xianyu.Fragments.AccountFragment;
import com.example.chenweizhao.xianyu.Fragments.TaskFragment;
import com.example.chenweizhao.xianyu.R;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment taskFragment;
    Fragment accountFragment;
    ImageView write;
    PopupWindow popupWindow;
    View popwindow_view;
    ConstraintLayout mDelivery;
    ConstraintLayout mOther;
    ConstraintLayout mQuestionnaire;

    private RadioGroup mRadioGroup;
    private RadioButton mTaskButton, mAccountButton, mMyButton;

    Drawable taskWhite;
    Drawable taskGray;
    Drawable accountWhite;
    Drawable accountGray;
    Drawable myWhite;
    Drawable myGray;


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
        accountFragment = new AccountFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, taskFragment);
        fragmentTransaction.commit();


        //
        popwindow_view = getLayoutInflater().inflate(R.layout.popwindows_write, null, true);
        mDelivery = popwindow_view.findViewById(R.id.take_expressage);
        mQuestionnaire = popwindow_view.findViewById(R.id.quesionnaire);
        mOther = popwindow_view.findViewById(R.id.other);
        mRadioGroup = findViewById(R.id.bottom_tab);
        mTaskButton = findViewById(R.id.Task);
        mAccountButton = findViewById(R.id.money);
        mMyButton = findViewById(R.id.my);

        taskWhite = getResources().getDrawable(R.drawable.icon_task_white);
        taskGray = getResources().getDrawable(R.drawable.icon_task_gray);
        accountWhite = getResources().getDrawable(R.drawable.icon_money_white);
        accountGray = getResources().getDrawable(R.drawable.icon_money_gray);
        myWhite = getResources().getDrawable(R.drawable.icon_my_white);
        myGray = getResources().getDrawable(R.drawable.icon_my_gray);
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

        //底部tab点击
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mTaskButton.getId()) {
                    mTaskButton.setCompoundDrawablesWithIntrinsicBounds(null, taskWhite, null, null);
                    mTaskButton.setTextColor(Color.parseColor("#ffdfdf"));
                    mAccountButton.setCompoundDrawablesWithIntrinsicBounds(null, accountGray, null, null);
                    mAccountButton.setTextColor(Color.parseColor("#2c4853"));
                    mMyButton.setCompoundDrawablesWithIntrinsicBounds(null, myGray, null, null);
                    mMyButton.setTextColor(Color.parseColor("#2c4853"));
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new TaskFragment());
                    fragmentTransaction.commit();

                } else if (checkedId == mAccountButton.getId()) {
                    Intent intent = new Intent(MainActivity.this, MoneyActivity.class);
                    startActivity(intent);
                }
                else if (checkedId == mMyButton.getId()) {
                    mMyButton.setCompoundDrawablesWithIntrinsicBounds(null, myWhite, null, null);
                    mMyButton.setTextColor(Color.parseColor("#ffdfdf"));
                    mTaskButton.setCompoundDrawablesWithIntrinsicBounds(null, taskGray, null, null);
                    mTaskButton.setTextColor(Color.parseColor("#2c4853"));
                    mAccountButton.setCompoundDrawablesWithIntrinsicBounds(null, accountGray, null, null);
                    mAccountButton.setTextColor(Color.parseColor("#2c4853"));
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new AccountFragment());
                    fragmentTransaction.commit();
                }
                else ;
            }
        });
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
