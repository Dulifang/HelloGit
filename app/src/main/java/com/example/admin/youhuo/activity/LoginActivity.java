package com.example.admin.youhuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.admin.youhuo.R;
import com.example.admin.youhuo.utils.SharedUtils;

/**
 * Created by admin on 2016/11/18.
 */

public class LoginActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1*3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String str=new SharedUtils().getShared("tag",LoginActivity.this);
                if(str.equals("")){
                    Intent intent=new Intent(LoginActivity.this,FirstStartActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(intent);
                }

            }
        }).start();
        finish();
    }
}
