package com.coco.smartbj.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理activity的类
 * 实现添加、删除当前、删除所有、删除指定等功能
 */

public class ActivityManager {
    //单例模式
    private ActivityManager(){}

    private static ActivityManager activityManager = new ActivityManager();

    public static ActivityManager getActivityManager(){
        return activityManager;
    }

    private List<Activity> mActivities = new ArrayList<>();

    //添加
    public void add(Activity activity){
        if(activity != null){
            mActivities.add(activity);
        }
    }

    //删除指定
    public void remove(Activity activity){
        if(activity != null){
            for(int i = mActivities.size() - 1;i >= 0;i--){
                Activity currentActivity = mActivities.get(i);
                if(currentActivity.getClass().equals(activity.getClass())){
                    currentActivity.finish();//销毁当前的activity
                    mActivities.remove(i);//从栈空间移除
                }
            }
        }
    }

    //删除当前
    public void removeCurrent(){
        Activity activity = mActivities.get(mActivities.size()-1);
        activity.finish();
        mActivities.remove(activity);
    }

    //删除所有
    public void removeAll(){
        for (int i = mActivities.size() - 1;i >= 0;i--){
            Activity activity = mActivities.get(i);
            activity.finish();
            mActivities.remove(activity);
        }
    }

    //返回栈大小
    public int size(){
        return mActivities.size();
    }

    //返回指定的activity
    public Activity getActivity(Activity activity) {
        for (int i = mActivities.size() - 1;i >= 0;i--){
            Activity a = mActivities.get(i);
            if (a.equals(activity)) {
                return a;
            }
        }
        return null;
    }
}
