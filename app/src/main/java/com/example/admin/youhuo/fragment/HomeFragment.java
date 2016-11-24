package com.example.admin.youhuo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.youhuo.R;
import com.example.admin.youhuo.adapter.MyAdapter1;
import com.example.admin.youhuo.view.MeasuredListView;
import com.example.admin.youhuo.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/21.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, PullToRefreshView.Pull_To_Load {
    private ImageView mFrag_home_log1, mFrag_home_log2;
    private ScaleAnimation scal1, scal2;//两个伸缩的动画 1.从两侧往中间缩  2.从中间往两边伸展
    private boolean flag = true;
    private int index = 0;//只让旋转两次的标志
    private int time = 4500;//第一次翻转前的时间间隔------>后期时间缩短
    private ImageView mImg_menu, mImg_scan;
    private PullToRefreshView mSc;
    private View centerView;
    private MyAdapter1 adapter1;
    private MeasuredListView mLv;
    private int max;
    private List<String> list1 = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragment_home, null);
        initList();
        initView(v);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        Thread.sleep(time);
                        hand.sendEmptyMessage(0);
                        index++;
                        time = 1000;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return v;
    }

    //初始化数据源
    private void initList() {
        for (int i = 0; i < 30; i++) {
            list1.add("第" + i + "条数据");
        }
    }

    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (index < 3) {
                if (msg.what == 0) {
                    // if:如果当前显示的是第一张图片，第一张图片开启动画（在动画结束的时候执行第二张图片的动画开启）
                    //else :如果当前显示的是第二张图片，第二张图片开启动画（动画结束行第一张执行）
                    //因为是在while循环中，所以动画在不停的反复执行，所以需要我们用index进行打断，执行两次即可
                    if (mFrag_home_log1.getVisibility() == View.VISIBLE) {
                        mFrag_home_log1.startAnimation(scal1);
                    } else {
                        mFrag_home_log2.startAnimation(scal1);
                    }
                }

            }
            if (msg.what == 1) {
                mSc.complate();
                loadMore();
                adapter1.notifyDataSetChanged();
                //mSc.MesureListHeight(mLv);
            }
            if (msg.what == 2) {
                mSc.complate();
                Toast.makeText(getActivity(), "联网失败，稍后刷新！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initView(View v) {
        mFrag_home_log1 = (ImageView) v.findViewById(R.id.mFrag_home_log1);
        mFrag_home_log2 = (ImageView) v.findViewById(R.id.mFrag_home_log2);
        mImg_menu = (ImageView) v.findViewById(R.id.mMenu_btn);
        mImg_scan = (ImageView) v.findViewById(R.id.mFrag_home_scan);
        mSc = (PullToRefreshView) v.findViewById(R.id.mSc);
        mSc.setCall(this);
        centerView = View.inflate(getActivity(), R.layout.view_center, null);
        mSc.setCenterView(centerView);

        //home中listView的操作
        mLv = (MeasuredListView) centerView.findViewById(R.id.mLv);
        adapter1 = new MyAdapter1(getActivity(), list1);
        mLv.setAdapter(adapter1);
       // mLv.setSelection(ListView.FOCUS_DOWN);
        mLv.setFocusable(false);
        /*
         * 参数：8个   1：x轴的起点（最大为1，即本身）   2：x轴的终点   3：y 轴的启点   4：y轴的终点
		 *         5：动画在x轴的应用范围   6: 一半  7：动画在y轴的应用范围   8：一半
		 *         总之，5,6,7,8四个参数的意思就是 变化点在中心
		 *
		 * */
        scal1 = new ScaleAnimation(1, 1, 1, 0, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        scal2 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        //默认显示的是第一张
        showLog1();
        //设置时间
        scal1.setDuration(100);
        scal2.setDuration(100);
        //给动画1设置监听----->动画1结束后，执行动画2
        scal1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mFrag_home_log1.getVisibility() == View.VISIBLE) {
                    mFrag_home_log1.setAnimation(null);
                    showLog2();
                    mFrag_home_log2.startAnimation(scal2);
                } else {
                    mFrag_home_log2.setAnimation(null);
                    showLog1();
                    mFrag_home_log1.startAnimation(scal2);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImg_menu.setOnClickListener(this);
        mImg_scan.setOnClickListener(this);
    }

    /**
     * log1显示，log2消失的方法
     */
    private void showLog1() {
        mFrag_home_log1.setVisibility(View.VISIBLE);
        mFrag_home_log2.setVisibility(View.GONE);
    }

    /**
     * log2显示，log1消失的方法
     */
    private void showLog2() {
        mFrag_home_log1.setVisibility(View.GONE);
        mFrag_home_log2.setVisibility(View.VISIBLE);
    }

    /**
     * 在生命周期将while循环停止
     */
    @Override
    public void onPause() {
        super.onPause();
        flag = false;
    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();
        switch (ID) {
            case R.id.mMenu_btn:
                Intent intent = new Intent();
                intent.setAction("cn.bgs.youhoo");
                getActivity().sendBroadcast(intent);
                break;
            case R.id.mFrag_home_scan:
                //TODO 二维码扫描后期自己添加

                break;
        }
    }

    @Override
    public void Load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hand.sendEmptyMessage(1);
            }
        }).start();
    }

    @Override
    public void Refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hand.sendEmptyMessage(2);
            }
        }).start();
    }

    private void loadMore() {
        int count = adapter1.getCount();
        for (int i = count; i < count + 30; i++) {
            list1.add("第" + i + "条数据");
        }
    }
}
