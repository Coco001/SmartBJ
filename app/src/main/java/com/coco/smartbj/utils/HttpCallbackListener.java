package com.coco.smartbj.utils;

/**
 * 网络请求回调的接口.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
