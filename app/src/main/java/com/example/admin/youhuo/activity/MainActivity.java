package com.example.admin.youhuo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.youhuo.R;
import com.example.admin.youhuo.fragment.CartFragment;
import com.example.admin.youhuo.fragment.CateFragment;
import com.example.admin.youhuo.fragment.HomeFragment;
import com.example.admin.youhuo.fragment.MineFragment;
import com.example.admin.youhuo.fragment.SeeFragment;
import com.example.admin.youhuo.view.LeftSlidingMenu;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private boolean isExit;
    private ImageView mImg_home, mImg_cart, mImg_mine, mImg_see, mImg_cate;
    private TextView mTv_home, mTv_cart, mTv_mine, mTv_see, mTv_cate;
    private LinearLayout mLinear_home, mLinear_cart, mLinear_mine, mLinear_see, mLinear_cate;
    private CartFragment mFrag_cart;
    private CateFragment mFrag_cate;
    private HomeFragment mFrag_home;
    private SeeFragment mFrag_see;
    private MineFragment mFrag_mine;
    private FragmentManager manager;
    private LeftSlidingMenu mSliding;
    private String tag = "cn.bgs.youhoo";
    private SlidingReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_top);
        manager = getSupportFragmentManager();
        receiver=new SlidingReceiver();
        initView();
        initDate();
        FragmentTransaction ft = manager.beginTransaction();
        mFrag_home = new HomeFragment();
        ft.add(R.id.mFrag_group, mFrag_home);
        ft.commit();
    }

    private void initView() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(tag);
        registerReceiver(receiver, filter);
        mSliding = (LeftSlidingMenu) findViewById(R.id.mSlid);
        View top_view = View.inflate(this, R.layout.activity_main, null);
        mSliding.setTop(top_view);
        View bottom_view = View.inflate(this, R.layout.base_bottom, null);
        mSliding.setBottom(bottom_view);
        mImg_cart = (ImageView) top_view.findViewById(R.id.mImg_cart);
        mImg_home = (ImageView) top_view.findViewById(R.id.mImg_home);
        mImg_mine = (ImageView) top_view.findViewById(R.id.mImg_mine);
        mImg_see = (ImageView) top_view.findViewById(R.id.mImg_see);
        mImg_cate = (ImageView) top_view.findViewById(R.id.mImg_cate);

        mTv_cart = (TextView) top_view.findViewById(R.id.mTv_cart);
        mTv_home = (TextView) top_view.findViewById(R.id.mTv_home);
        mTv_mine = (TextView) top_view.findViewById(R.id.mTv_mine);
        mTv_see = (TextView) top_view.findViewById(R.id.mTv_see);
        mTv_cate = (TextView) top_view.findViewById(R.id.mTv_cate);

        mLinear_home = (LinearLayout) top_view.findViewById(R.id.mLine_home);
        mLinear_cart = (LinearLayout) top_view.findViewById(R.id.mLine_cart);
        mLinear_mine = (LinearLayout) top_view.findViewById(R.id.mLine_mine);
        mLinear_see = (LinearLayout) top_view.findViewById(R.id.mLine_see);
        mLinear_cate = (LinearLayout) top_view.findViewById(R.id.mLine_cate);
    }

    private void initDate() {
        mLinear_home.setOnClickListener(this);
        mLinear_cart.setOnClickListener(this);
        mLinear_mine.setOnClickListener(this);
        mLinear_see.setOnClickListener(this);
        mLinear_cate.setOnClickListener(this);
    }

    public void onClick(View view) {
        ClearBottom();
        hintFragment();
        FragmentTransaction ft = manager.beginTransaction();
        switch (view.getId()) {
            case R.id.mLine_home:
                mImg_home.setImageResource(R.mipmap.tabbar_homepage_highlighted_gray);
                mTv_home.setTextColor(Color.BLACK);
                ft.show(mFrag_home);
                break;
            case R.id.mLine_cart:
                mImg_cart.setImageResource(R.mipmap.tabbar_cart_highlighted_gray);
                mTv_cart.setTextColor(Color.BLACK);
                if (mFrag_cart == null) {
                    mFrag_cart = new CartFragment();
                    ft.add(R.id.mFrag_group, mFrag_cart);
                } else {
                    ft.show(mFrag_cart);
                }
                break;
            case R.id.mLine_mine:
                mImg_mine.setImageResource(R.mipmap.tabbar_mine_highlighted_gray);
                mTv_mine.setTextColor(Color.BLACK);
                if (mFrag_mine == null) {
                    mFrag_mine = new MineFragment();
                    ft.add(R.id.mFrag_group, mFrag_mine);
                } else {
                    ft.show(mFrag_mine);
                }
                break;
            case R.id.mLine_see:
                mImg_see.setImageResource(R.mipmap.tabbar_see_highlighted_gray);
                mTv_see.setTextColor(Color.BLACK);
                if (mFrag_see == null) {
                    mFrag_see = new SeeFragment();
                    ft.add(R.id.mFrag_group, mFrag_see);
                } else {
                    ft.show(mFrag_see);
                }
                break;
            case R.id.mLine_cate:
                mImg_cate.setImageResource(R.mipmap.tabbar_category_highlighted_gray);
                mTv_cate.setTextColor(Color.BLACK);
                if (mFrag_cate == null) {
                    mFrag_cate = new CateFragment();
                    ft.add(R.id.mFrag_group, mFrag_cate);
                } else {
                    ft.show(mFrag_cate);
                }
                break;
        }
        ft.commit();
    }
    /*
     *  返回键的常用的写法：
	 * if(isExit){
			finish();//退出
			System.exit(0);//结束掉进程
		}else{
			isExit=true;//将这个设置为true
			ToastUtils.showShort(this, "再次点击退出");
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					isExit=false;

				}
			}, 2000);
		}
	 * */

    @Override
    public void onBackPressed() {

        isExit = true;
        //带动画的跳转
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_from1, R.anim.in_frombottom1);
    }

    /**
     * 手机按返回键的监听
     */


    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    /**
     * 将所有的颜色清空
     */
    private void ClearBottom() {
        mImg_home.setImageResource(R.mipmap.tabbar_homepage_normal);
        mImg_cart.setImageResource(R.mipmap.tabbar_cart_normal);
        mImg_mine.setImageResource(R.mipmap.tabbar_mine_normal);
        mImg_see.setImageResource(R.mipmap.tabbar_see_normal);
        mImg_cate.setImageResource(R.mipmap.tabbar_category_normal);

        mTv_home.setTextColor(Color.GRAY);
        mTv_cart.setTextColor(Color.GRAY);
        mTv_mine.setTextColor(Color.GRAY);
        mTv_see.setTextColor(Color.GRAY);
        mTv_cate.setTextColor(Color.GRAY);
    }

    /**
     * 隐藏所有fragment的方法
     */
    private void hintFragment() {
        FragmentTransaction ft = manager.beginTransaction();
        if (mFrag_home != null) {
            ft.hide(mFrag_home);
        }
        if (mFrag_cart != null) {
            ft.hide(mFrag_cart);
        }
        if (mFrag_mine != null) {
            ft.hide(mFrag_mine);
        }
        if (mFrag_see != null) {
            ft.hide(mFrag_see);
        }
        if (mFrag_cate != null) {
            ft.hide(mFrag_cate);
        }
        ft.commit();
    }

    private class SlidingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSliding.Sliding();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
