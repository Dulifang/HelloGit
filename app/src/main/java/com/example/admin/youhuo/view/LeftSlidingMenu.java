package com.example.admin.youhuo.view;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by admin on 2016/11/21.
 */

public class LeftSlidingMenu extends FrameLayout {
    private LinearLayout mTopLinear, mBottomLinear;
    private boolean SlidingFlag = false;
    private PointF startPF = new PointF();
    private int Max_width = 0;
    private int topheight = 150;

    public LeftSlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager manager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        Max_width = manager.getDefaultDisplay().getWidth() * 4 / 5;
        initView();
    }

    private void initView() {
        mBottomLinear = new LinearLayout(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Max_width, FrameLayout.LayoutParams.MATCH_PARENT);
        mBottomLinear.setLayoutParams(lp);
        mBottomLinear.setOrientation(LinearLayout.VERTICAL);

        mTopLinear = new LinearLayout(getContext());
        mTopLinear.setBackgroundColor(Color.WHITE);
        mTopLinear.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mTopLinear.setOrientation(LinearLayout.VERTICAL);
        addView(mBottomLinear);
        addView(mTopLinear);
    }

    public void setBottom(View v) {
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mBottomLinear.addView(v);
    }

    public void setTop(View v) {
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mTopLinear.addView(v);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            startPF.x=ev.getX();
            startPF.y=ev.getY();
            if(!SlidingFlag){
                return super.dispatchTouchEvent(ev);
            }
            if(SlidingFlag && startPF.y>topheight && startPF.x>Max_width){
                return true;
            }
        }else if(ev.getAction()==MotionEvent.ACTION_MOVE){
            PointF pf=new PointF();
            pf.x=ev.getX();
            pf.y=ev.getY();
            float disX=pf.x-startPF.x;
            float disY=pf.y-startPF.y;
            if(Math.abs(disX)/2-Math.abs(disY)<0){
                return super.dispatchTouchEvent(ev);
            }else{
                if(SlidingFlag && pf.y>topheight){
                    FrameLayout.LayoutParams lp= (LayoutParams) mTopLinear.getLayoutParams();
                    if(disX>0){
                        lp.leftMargin= (int) (lp.leftMargin+Math.abs(disX));
                        lp.rightMargin=-lp.leftMargin;
                        if(lp.leftMargin>=Max_width && disX>0){
                            lp.leftMargin=Max_width;
                            lp.rightMargin=-Max_width;
                            SlidingFlag=true;
                        }
                    }else if(disX<0){
                        lp.leftMargin= (int) (lp.leftMargin-Math.abs(disX));
                        lp.rightMargin=-lp.leftMargin;
                        if(lp.leftMargin<=0 && disX<0){
                            lp.leftMargin=0;
                            lp.rightMargin=0;
                            SlidingFlag=false;
                        }
                    }
                    mTopLinear.setLayoutParams(lp);
                    requestLayout();
                    startPF=pf;
                    return true;
                }
            }
        }else if(ev.getAction()==MotionEvent.ACTION_UP){
            if(SlidingFlag){
                FrameLayout.LayoutParams lp= (LayoutParams) mTopLinear.getLayoutParams();
                if(lp.leftMargin>=Max_width/2){
                    SlidingFlag=true;
                    lp.leftMargin=Max_width;
                    lp.rightMargin=-Max_width;
                }else{
                    SlidingFlag=false;
                    lp.leftMargin=0;
                    lp.rightMargin=0;
                }
                mTopLinear.setLayoutParams(lp);
                requestLayout();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public void Sliding(){
        FrameLayout.LayoutParams lp= (LayoutParams) mTopLinear.getLayoutParams();
        if(SlidingFlag){
            lp.leftMargin=0;
            lp.rightMargin=0;
        }else{
            lp.leftMargin=Max_width;
            lp.rightMargin=-Max_width;
        }
        mTopLinear.setLayoutParams(lp);
        SlidingFlag=!SlidingFlag;
    }
}
