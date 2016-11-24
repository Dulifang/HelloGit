package com.example.admin.youhuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.admin.youhuo.R;

/**
 * Created by admin on 2016/11/18.
 */

/**
 * 带动画的页面跳转 ：overridePendingTransition：处理activity和activity之间的动画的
 * overridePendingTransition：方法的调用必须再stactivity之后调用
 * 两个参数：1.即将进入的activity的动画     2.即将退出的activity的动画
 */

public class MenuActivity extends Activity implements View.OnClickListener {
    private Button mMenu_boy, mMenu_girl, mMenu_child, mMenu_style;
    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initView();
        initData();
    }

    private void initView() {
        mMenu_boy = (Button) findViewById(R.id.mMenu_boy);
        mMenu_girl = (Button) findViewById(R.id.mMenu_girl);
        mMenu_child = (Button) findViewById(R.id.mMenu_child);
        mMenu_style = (Button) findViewById(R.id.mMenu_style);
    }

    private void initData() {
        mMenu_boy.setOnClickListener(this);
        mMenu_girl.setOnClickListener(this);
        mMenu_child.setOnClickListener(this);
        mMenu_style.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();
        switch (ID) {
            case R.id.mMenu_boy:
                Intent intent1 = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
                break;
            case R.id.mMenu_girl:
                Intent intent2 = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
                break;
            case R.id.mMenu_child:
                Intent intent3 = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
                break;
            case R.id.mMenu_style:
                Intent intent4 = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent4);
                overridePendingTransition(R.anim.in_frombottom, R.anim.out_from);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();
            System.exit(0);
        } else {
            isExit = true;
            Toast.makeText(MenuActivity.this, "再次点击退出", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

}
