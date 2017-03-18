package com.coco.smartbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.coco.smartbj.R;
import com.coco.smartbj.commen.BaseActivity;
import com.coco.smartbj.utils.Constant;
import com.coco.smartbj.utils.MyApplication;
import com.coco.smartbj.utils.Sptools;
import com.coco.smartbj.utils.Uiutils;

import java.util.ArrayList;
import java.util.List;

/**
 * 向导界面
 */

public class GuideActivity extends BaseActivity {
    private ViewPager guide_pager;
    private List<ImageView> mImageViews;
    private MyAdapter mAdapter;
    private Button bt_guide_startexp;
    private LinearLayout ll_guide_points;
    private View v_guide_redpoint;
    private float offset;

    @Override
    protected void initEvent() {
        //设置监听布局完成的事件
        v_guide_redpoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                v_guide_redpoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //计算点与点之间的距离
                offset = ll_guide_points.getChildAt(1).getLeft() - ll_guide_points.getChildAt(0).getLeft();
            }
        });

        //设置按钮的点击事件并记录
        bt_guide_startexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sptools.setBoolean(getApplicationContext(), Constant.ISSETUP, true);
                Intent startMain = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(startMain);
            }
        });

        guide_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float leftMargin = offset * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v_guide_redpoint.getLayoutParams();
                params.leftMargin = Math.round(leftMargin);
                v_guide_redpoint.setLayoutParams(params);
            }
            //viewpager滑动到最后一个页面的时候显示按钮
            @Override
            public void onPageSelected(int position) {
                if (position == mImageViews.size() - 1) {
                    bt_guide_startexp.setVisibility(View.VISIBLE);
                } else {
                    bt_guide_startexp.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        guide_pager = (ViewPager) findViewById(R.id.guide_pager);
        bt_guide_startexp = (Button) findViewById(R.id.bt_guide_startexp);
        ll_guide_points = (LinearLayout) findViewById(R.id.ll_guide_points);
        v_guide_redpoint = findViewById(R.id.v_guide_redpoint);
        int[] pics = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
        mImageViews = new ArrayList<>();
        for (int i = 0; i < pics.length; i++) {
            ImageView imageView = new ImageView(MyApplication.context);
            imageView.setBackgroundResource(pics[i]);
            mImageViews.add(imageView);
            //添加灰色的点
            View v_point = new View(MyApplication.context);
            v_point.setBackgroundResource(R.drawable.gray_point);
            int dp = Uiutils.px2dp(50);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp, dp);
            v_point.setLayoutParams(params);
            //设置点之间的间隔
            if (i != 0) {
                params.leftMargin = dp;
            }
            ll_guide_points.addView(v_point);
        }
        mAdapter = new MyAdapter();
        guide_pager.setAdapter(mAdapter);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViews.get(position);
            container.addView(imageView);
            return imageView;
        }
    }

}
