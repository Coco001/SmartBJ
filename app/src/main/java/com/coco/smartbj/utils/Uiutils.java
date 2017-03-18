package com.coco.smartbj.utils;

import android.content.Context;

/**
 * 操作ui界面的工具类
 */

public class Uiutils {

    public static Context getContext(){
        return MyApplication.context;
    }
    //将dp转化为px
    public static int dp2px(int dp){
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);//实现四舍五入
    }

    public static int px2dp(int px){
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);//实现四舍五入
    }
}
