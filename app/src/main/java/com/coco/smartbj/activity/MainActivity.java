package com.coco.smartbj.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coco.smartbj.R;
import com.coco.smartbj.commen.BaseActivity;
import com.coco.smartbj.fragment.GovaffairsFragment;
import com.coco.smartbj.fragment.HomeFragment;
import com.coco.smartbj.fragment.InteractFragment;
import com.coco.smartbj.fragment.NewsFragment;
import com.coco.smartbj.fragment.PicsFragment;
import com.coco.smartbj.fragment.SettingFragment;
import com.coco.smartbj.fragment.SmartFragment;
import com.coco.smartbj.fragment.ThemeFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private PicsFragment mPicsFragment;
    private ThemeFragment mThemeFragment;
    private InteractFragment mInteractFragment;

    private Toolbar main_toolbar;
    private NavigationView nav_view;
    private DrawerLayout main_drawlayout;
    private TextView tv_main_home;
    private ImageView iv_main_home;
    private ImageView iv_main_news;
    private TextView tv_main_news;
    private ImageView iv_main_smart;
    private TextView tv_main_smart;
    private ImageView iv_main_gov;
    private TextView tv_main_gov;
    private ImageView iv_main_setting;
    private TextView tv_main_setting;
    private HomeFragment mHomeFragment;
    private NewsFragment mNewsFragment;
    private SmartFragment mSmartFragment;
    private GovaffairsFragment mGovaffairsFragment;
    private SettingFragment mSettingFragment;
    private FragmentTransaction mTransaction;
    private ActionBar mBar;
    private TextView toolbar_title;

    private static boolean flag = true;
    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    flag = true;
                    break;
            }
        }
    };

    @Override
    protected void initEvent() {
        nav_view.setCheckedItem(R.id.nav_news);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_news:
                        select(1);
                        break;
                    case R.id.nav_theme:
                        select(5);
                        break;
                    case R.id.nav_pics:
                        select(6);
                        break;
                    case R.id.nav_chat:
                        select(7);
                        break;
                }
                main_drawlayout.closeDrawers();//关闭导航栏
                return true;
            }
        });

        iv_main_home.setOnClickListener(this);
        iv_main_news.setOnClickListener(this);
        iv_main_smart.setOnClickListener(this);
        iv_main_gov.setOnClickListener(this);
        iv_main_setting.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        main_drawlayout = (DrawerLayout) findViewById(R.id.main_drawlayout);
        main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        //
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.item_selector);
        nav_view.setItemTextColor(csl);
        //
        //nav_view.setItemIconTintList(null);
        iv_main_home = (ImageView) findViewById(R.id.iv_main_home);
        tv_main_home = (TextView) findViewById(R.id.tv_main_home);
        iv_main_news = (ImageView) findViewById(R.id.iv_main_news);
        tv_main_news = (TextView) findViewById(R.id.tv_main_news);
        iv_main_smart = (ImageView) findViewById(R.id.iv_main_smart);
        tv_main_smart = (TextView) findViewById(R.id.tv_main_smart);
        iv_main_gov = (ImageView) findViewById(R.id.iv_main_gov);
        tv_main_gov = (TextView) findViewById(R.id.tv_main_gov);
        iv_main_setting = (ImageView) findViewById(R.id.iv_main_setting);
        tv_main_setting = (TextView) findViewById(R.id.tv_main_setting);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        mBar = getSupportActionBar();
        if (mBar != null) {
            mBar.setDisplayHomeAsUpEnabled(false);
            mBar.setHomeAsUpIndicator(R.mipmap.img_menu);
            mBar.setDisplayShowTitleEnabled(false);
            initTitle();

        }
        select(0);
    }

    @Override
    protected void initTitle() {
        toolbar_title.setText("首页");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                main_drawlayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    /**
     * 重写返回键点击事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flag) {
                Toast.makeText(MainActivity.this, "再点击一次，退出应用", Toast.LENGTH_SHORT).show();
                flag = false;
                //发送一个延迟消息
                handler.sendEmptyMessageDelayed(1, 2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void select(int i) {
        FragmentManager manager = this.getFragmentManager();
        mTransaction = manager.beginTransaction();
        hintOther();
        switch (i) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    mTransaction.add(R.id.main_framelayout, mHomeFragment);
                }
                mBar.setDisplayHomeAsUpEnabled(false);
                mTransaction.show(mHomeFragment);
                iv_main_home.setBackgroundResource(R.mipmap.home_press);
                tv_main_home.setTextColor(Color.RED);
                toolbar_title.setText("首页");
                break;
            case 1:
                if (mNewsFragment == null) {
                    mNewsFragment = new NewsFragment();
                    mTransaction.add(R.id.main_framelayout, mNewsFragment);
                }
                mBar.setDisplayHomeAsUpEnabled(true);
                mTransaction.show(mNewsFragment);
                iv_main_news.setBackgroundResource(R.mipmap.newscenter_press);
                tv_main_news.setTextColor(Color.RED);
                toolbar_title.setText("新闻中心");
                break;
            case 2:
                if (mSmartFragment == null) {
                    mSmartFragment = new SmartFragment();
                    mTransaction.add(R.id.main_framelayout, mSmartFragment);
                }
                mBar.setDisplayHomeAsUpEnabled(true);
                mTransaction.show(mSmartFragment);
                iv_main_smart.setBackgroundResource(R.mipmap.smartservice_press);
                tv_main_smart.setTextColor(Color.RED);
                toolbar_title.setText("智慧服务");
                break;
            case 3:
                if (mGovaffairsFragment == null) {
                    mGovaffairsFragment = new GovaffairsFragment();
                    mTransaction.add(R.id.main_framelayout, mGovaffairsFragment);
                }
                mBar.setDisplayHomeAsUpEnabled(true);
                mTransaction.show(mGovaffairsFragment);
                iv_main_gov.setBackgroundResource(R.mipmap.govaffairs_press);
                tv_main_gov.setTextColor(Color.RED);
                toolbar_title.setText("政务");
                break;
            case 4:
                if (mSettingFragment == null) {
                    mSettingFragment = new SettingFragment();
                    mTransaction.add(R.id.main_framelayout, mSettingFragment);
                }
                mBar.setDisplayHomeAsUpEnabled(false);
                mTransaction.show(mSettingFragment);
                iv_main_setting.setBackgroundResource(R.mipmap.setting_press);
                tv_main_setting.setTextColor(Color.RED);
                toolbar_title.setText("设置中心");
                break;
            case 5:
                if (mThemeFragment == null) {
                    mThemeFragment = new ThemeFragment();
                    mTransaction.add(R.id.main_framelayout, mThemeFragment);
                }
                mTransaction.show(mThemeFragment);
                iv_main_news.setBackgroundResource(R.mipmap.newscenter_press);
                tv_main_news.setTextColor(Color.RED);
                toolbar_title.setText("主题");
                break;
            case 6:
                if (mPicsFragment == null) {
                    mPicsFragment = new PicsFragment();
                    mTransaction.add(R.id.main_framelayout, mPicsFragment);
                }
                mTransaction.show(mPicsFragment);
                iv_main_news.setBackgroundResource(R.mipmap.newscenter_press);
                tv_main_news.setTextColor(Color.RED);
                toolbar_title.setText("组图");
                break;
            case 7:
                if (mInteractFragment == null) {
                    mInteractFragment = new InteractFragment();
                    mTransaction.add(R.id.main_framelayout, mInteractFragment);
                }
                mTransaction.show(mInteractFragment);
                iv_main_news.setBackgroundResource(R.mipmap.newscenter_press);
                tv_main_news.setTextColor(Color.RED);
                toolbar_title.setText("互动");
                break;
        }
        mTransaction.commit();
    }

    private void hintOther() {
        if (mHomeFragment != null) {
            mTransaction.hide(mHomeFragment);
            iv_main_home.setBackgroundResource(R.mipmap.home);
            tv_main_home.setTextColor(Color.GRAY);
        }
        if (mNewsFragment != null) {
            mTransaction.hide(mNewsFragment);
            iv_main_news.setBackgroundResource(R.mipmap.newscenter);
            tv_main_news.setTextColor(Color.GRAY);
        }
        if (mSmartFragment != null) {
            mTransaction.hide(mSmartFragment);
            iv_main_smart.setBackgroundResource(R.mipmap.smartservice);
            tv_main_smart.setTextColor(Color.GRAY);
        }
        if (mGovaffairsFragment != null) {
            mTransaction.hide(mGovaffairsFragment);
            iv_main_gov.setBackgroundResource(R.mipmap.govaffairs);
            tv_main_gov.setTextColor(Color.GRAY);
        }
        if (mSettingFragment != null) {
            mTransaction.hide(mSettingFragment);
            iv_main_setting.setBackgroundResource(R.mipmap.setting);
            tv_main_setting.setTextColor(Color.GRAY);
        }
        if (mThemeFragment != null) {
            mTransaction.hide(mThemeFragment);
        }
        if (mPicsFragment != null) {
            mTransaction.hide(mPicsFragment);
        }
        if (mInteractFragment != null) {
            mTransaction.hide(mInteractFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_main_home:
                select(0);
                break;
            case R.id.iv_main_news:
                select(1);
                break;
            case R.id.iv_main_smart:
                select(2);
                break;
            case R.id.iv_main_gov:
                select(3);
                break;
            case R.id.iv_main_setting:
                select(4);
                break;
        }
    }

}
