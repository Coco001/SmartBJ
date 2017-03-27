package com.coco.smartbj.activity;

import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.coco.smartbj.R;
import com.coco.smartbj.commen.BaseActivity;
import com.coco.smartbj.utils.ActivityManager;
import com.coco.smartbj.utils.Constant;
import com.coco.smartbj.utils.Sptools;

/**
 * 初试加载的界面
 */

public class SplashActivity extends BaseActivity {
    private ImageView sp;
    private AnimationSet mAs;

    @Override
    protected void initData() {
        sp = (ImageView) findViewById(R.id.sp);
        startAnimation();
        initEvent();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        mAs.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (Sptools.getBoolean(getApplicationContext(), Constant.ISSETUP, false)){
                    //true，设置过 ，直接进入主界面
                    //进入主界面
                    Intent main = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(main);//启动主界面
                } else {
                    //false 没设置过，进入设置向导界面
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);//启动设置向导界面
                }
                //关闭自己
                ActivityManager.getActivityManager().removeCurrent();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    /**
     * 开启动画
     */
    private void startAnimation() {
        mAs = new AnimationSet(false);

        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(1000);
        ra.setFillAfter(true);
        mAs.addAnimation(ra);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(1000);
        sa.setFillAfter(true);
        mAs.addAnimation(sa);

        AlphaAnimation aa = new AlphaAnimation(0, 1);// 由完全透明到不透明
        aa.setDuration(2000);
        aa.setFillAfter(true);// 动画播放完之后，停留在当前状态
        mAs.addAnimation(aa);

        sp.startAnimation(mAs);
    }
}
