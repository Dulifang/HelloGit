package com.example.admin.youhuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.admin.youhuo.R;
import com.example.admin.youhuo.utils.SharedUtils;

/**
 * Created by admin on 2016/11/18.
 */

public class FirstStartActivity extends Activity implements View.OnClickListener {
    private TextView mFirst_time, mFirst_tiao;
    private int num = 5;
    private SharedUtils utils;
    private String tag = "tag";
    private boolean flag = true;
    private boolean clik = false;//点击立即跳转之后的标志
    private TimeThread time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstatart);
        utils = new SharedUtils();
        utils.saveShared("tag", tag, this);
        initView();
        time = new TimeThread();
        time.start();
    }

    private void initView() {
        mFirst_time = (TextView) findViewById(R.id.mFirst_time);
        mFirst_tiao = (TextView) findViewById(R.id.mFirst_tiao);

        mFirst_tiao.setOnClickListener(this);
    }

    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (num > 0) {
                    num--;
                    mFirst_time.setText("" + num);
                }
                if (num == 1) {
                    if (!clik) {
                        Intent intent = new Intent(FirstStartActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(FirstStartActivity.this, MenuActivity.class);
        startActivity(intent);
        time.interrupt();
        clik = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        flag = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    private class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (flag) {
                try {
                    Thread.sleep(1 * 1000);
                    hand.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
