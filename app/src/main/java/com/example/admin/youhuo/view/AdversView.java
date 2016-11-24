package com.example.admin.youhuo.view;

/**
 * Created by admin on 2016/11/23.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.admin.youhuo.R;
import com.example.admin.youhuo.activity.MyApplication;
import com.example.admin.youhuo.info.AdverInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的广告轮播类
 **/
public class AdversView implements ViewPager.OnPageChangeListener {
    private Context context;
    private RelativeLayout relat;//放置vp和点的容器
    private ViewPager mVp;
    private LinearLayout mLinear;//存放底部显示点的容器
    private boolean touchFlag = false;//触摸锁
    private Bitmap pointS;//选中的点
    private Bitmap pointN;//没有选中的点
    private List<View> Vplist = new ArrayList<View>();
    private List<AdverInfo> list;
    private VpAdapter adapter;
    private int index = 0;//当前vp的索引页
    private boolean ThreadFlag = true;
    private View[] views;
    private TimeThread timeThread;

    public AdversView(Context context, List<AdverInfo> list) {
        this.context = context;
        this.list = list;
        views = new View[list.size()];
        initList();
        initView();
        initPoint();
        timeThread = new TimeThread();
        timeThread.start();
    }

    /**
     * 获取当前父容器的方法
     */
    public RelativeLayout getView() {
        return relat;
    }

    /**
     * 关闭线程的方法
     */
    public void stopTime() {
        ThreadFlag = false;
    }

    //初始化数据
    private void initList() {
        for (int i = 0; i < list.size(); i++) {
            ImageView mImg = new ImageView(context);
            mImg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImg.setScaleType(ImageView.ScaleType.FIT_XY);
            mImg.setImageResource(R.mipmap.homepage_background);
            //图片的异步加载
            ((MyApplication) context.getApplicationContext()).loader.Load(list.get(i).getImgpath(), mImg, context);
            //Log.e("", "22222"+list.get(i).getImgpath());
            Vplist.add(mImg);
            //Log.e("", "33333333"+Vplist.size());
            views[i] = mImg;
        }
    }

    /**
     * 初始化底部圆点的方法
     */
    private void initPoint() {
        for (int i = 0; i < list.size(); i++) {
            ImageView img = new ImageView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 10;
            lp.bottomMargin = 30;
            img.setLayoutParams(lp);
            if (i == 0) {
                img.setImageBitmap(pointS);
            } else {
                img.setImageBitmap(pointN);
            }
            mLinear.addView(img, i);
        }
    }

    private void initView() {
        relat = new RelativeLayout(context);
        mVp = new ViewPager(context);
        mVp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mLinear = new LinearLayout(context);
        mLinear.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mLinear.setLayoutParams(lp);
        //设置adapter
        adapter = new VpAdapter();
        //设置vp页面切换速度
        ViewPagerScroller scrol = new ViewPagerScroller(context);
        scrol.initViewPagerScroll(mVp);
        mVp.setAdapter(adapter);

        relat.addView(mVp);
        relat.addView(mLinear);
        //读取圆点图片
        pointN = BitmapFactory.decodeResource(context.getResources(), R.mipmap.community_ad_banner_point_nor);
        pointS = BitmapFactory.decodeResource(context.getResources(), R.mipmap.community_ad_banner_point_sel);
        //设置vp滑动监听
        mVp.setOnPageChangeListener(this);
    }

    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (touchFlag) {
                    return;
                }
                if (!ThreadFlag) {
                    return;
                }
                index++;
                mVp.setCurrentItem(index);
            }
        }
    };

    private class VpAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(Vplist.get(position % Vplist.size()));

            return Vplist.get(position % Vplist.size());
        }
    }

    /**
     * vp的滑动监听
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == ViewPager.SCROLL_STATE_IDLE) {
            touchFlag = false;
        } else {
            touchFlag = true;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        index = state;
        //根据vp页面切换底部对应点
        for (int i = 0; i < Vplist.size(); i++) {
            ImageView mImg = (ImageView) mLinear.getChildAt(i);
            if (i == state % Vplist.size()) {
                mImg.setImageBitmap(pointS);
            } else {
                mImg.setImageBitmap(pointN);
            }
        }
    }

    /**
     * 子线程内部类
     */
    public class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (touchFlag) {
                try {
                    Thread.sleep(6000);
                    hand.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
