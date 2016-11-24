package com.example.admin.youhuo.view;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.admin.youhuo.R;

/**
 * Created by admin on 2016/11/22.
 */

/*
 * 带上下拉刷新功能的scrollview:---》一般加载在fragment中，首页
 *
 * */
public class PullToRefreshView extends ScrollView {
    private LinearLayout mLinear;//scrollview中添加的linear
    private final static int DONE = 0;//完成状态
    private final static int RELASE_REFRESH = 2;//释放刷新状态
    private final static int REGRESHING = 3;//刷新状态
    private final static int RELASE_LOAD = 5;//释放加载状态
    private final static int LOADING = 6;//加载状态
    private int mode = DONE;//当前所处的状态
    private View headView;//头部的布局
    private View footView;//底部的布局
    private ImageView headImg;//头部的图片
    private ImageView footImg;//底部的图片
    private AnimationDrawable draw_p;
    private AnimationDrawable draw_U;
    private int height = 0;//头部的高度
    private PointF startPF = new PointF();
    private int scal = 3;
    private Pull_To_Load call;
    private LinearLayout centerView;//scrollview里显示的布局，其中包括广告轮播，listView及其他
    private int[] drawables = {R.mipmap.icon_loaing_frame_1, R.mipmap.icon_loaing_frame_2, R.mipmap.icon_loaing_frame_3,
            R.mipmap.icon_loaing_frame_4, R.mipmap.icon_loaing_frame_5, R.mipmap.icon_loaing_frame_6, R.mipmap.icon_loaing_frame_7,
            R.mipmap.icon_loaing_frame_8, R.mipmap.icon_loaing_frame_9, R.mipmap.icon_loaing_frame_10, R.mipmap.icon_loaing_frame_11,
            R.mipmap.icon_loaing_frame_12, R.mipmap.icon_loaing_frame_13,};

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnim();
        initView();
    }

    private void initView() {
//设置scrollview的参数
        mLinear = new LinearLayout(getContext());
        mLinear.setOrientation(LinearLayout.VERTICAL);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mLinear.setLayoutParams(lp);
        //设置scrollview中显示的布局的参数
        centerView = new LinearLayout(getContext());
        centerView.setOrientation(LinearLayout.VERTICAL);
        centerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //设置头尾布局的参数及动画
        headView = View.inflate(getContext(), R.layout.view_head, null);
        footView = View.inflate(getContext(), R.layout.view_head, null);
        headImg = (ImageView) headView.findViewById(R.id.mSCImg);
        footImg = (ImageView) footView.findViewById(R.id.mSCImg);
        headView.measure(0, 0);
        height = headView.getMeasuredHeight();

        headView.setPadding(0, -height, 0, 0);
        footView.setPadding(0, 0, 0, -height);
        headImg.setImageDrawable(draw_p);
        footImg.setImageDrawable(draw_U);
        //将子布局依次添加到scrollview的linear中，scrollview是单布局，想要用多布局必须用linear
        mLinear.addView(headView);
        mLinear.addView(centerView);
        mLinear.addView(footView);
        //将linear添加到自定义中
        addView(mLinear);
    }

    //设置centerView的方法
    public void setCenterView(View v) {
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        centerView.addView(v);
    }

    //初始化动画的方法
    private void initAnim() {
        draw_p = new AnimationDrawable();
        draw_U = new AnimationDrawable();
        for (int i : drawables) {
            draw_p.addFrame(getResources().getDrawable(i), 20);
            draw_U.addFrame(getResources().getDrawable(i), 20);
        }
        draw_p.setOneShot(false);
        draw_U.setOneShot(false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            startPF.y = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            PointF pf = new PointF();
            pf.y = ev.getY();
            float disY = (pf.y - startPF.y) / scal;
            //进行判断：上拉或下拉
            if (getScrollY() <= 0 && disY > 0) {//下拉
                headView.setPadding(0, (int) (disY - height), 0, 0);
                if (disY > height) {
                    mode = RELASE_REFRESH;
                } else {
                    mode = DONE;
                }
                return true;
            }
            //
            if ((getScrollY() + getHeight() )>= PullToRefreshView.this.computeVerticalScrollRange() && disY < 0) {//上滑  判断是否滑到scrollview的底部
                footView.setPadding(0, 0, 0, (int) (Math.abs(disY) - height));
                if (Math.abs(disY) > height) {
                    mode = RELASE_LOAD;
                } else {
                    mode = DONE;
                }
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            select();
        }
        return super.dispatchTouchEvent(ev);
    }

    //根据当前mode的状态进行判断是刷新还是加载
    private void select() {
        switch (mode) {
            case DONE:
                draw_p.stop();
                draw_U.stop();
                headView.setPadding(0, -height, 0, 0);
                footView.setPadding(0, 0, 0, -height);
                break;
            case RELASE_REFRESH:
                draw_p.start();
                headView.setPadding(0, 0, 0, 0);
                //刷新
                call.Refresh();
                break;
            case RELASE_LOAD:
                draw_U.start();
                footView.setPadding(0, 0, 0, 0);
                //加载
                call.Load();
                break;
        }
    }

    //结束的方法
    public void complate() {
        mode = DONE;
        select();
    }

    //回调接口
    public interface Pull_To_Load {
        public void Load();

        public void Refresh();
    }

    public void setCall(Pull_To_Load call) {
        this.call = call;
    }

    //测量listView高度的方法
    public void MesureListHeight(ListView lv) {
        //1.获取到listView的适配器
        ListAdapter adapter = lv.getAdapter();
        int height = 0;//当前listView的高度
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, null);
            view.measure(0, 0);
            height = height + view.getMeasuredHeight();
        }
        height = height + lv.getDividerHeight() * (adapter.getCount() - 1) +
                lv.getPaddingBottom() + lv.getPaddingTop();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        lv.setLayoutParams(lp);
        PullToRefreshView.this.invalidate();//刷新高度
    }
}
