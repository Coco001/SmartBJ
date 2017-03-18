package com.coco.smartbj.commen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.coco.smartbj.utils.ActivityManager;

/**
 * Activity 的基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ActivityManager.getActivityManager().add(this);
        initTitle();
        initData();
        initEvent();
    }

    protected abstract void initEvent();

    protected abstract void initData();

    protected abstract void initTitle();

    protected abstract int getLayoutId();

    //启动新的activity
    public void setupActivity(Class Activity, Bundle bundle) {
        Intent intent = new Intent(this, Activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    //销毁当前的activity
    public void removeCurrentActivity() {
        ActivityManager.getActivityManager().removeCurrent();
    }

    //销毁对所有
    public void removeAllActivity() {
        ActivityManager.getActivityManager().removeAll();
    }
}
