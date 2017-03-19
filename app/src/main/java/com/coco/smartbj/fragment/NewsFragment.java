package com.coco.smartbj.fragment;

import android.util.Log;

import com.coco.smartbj.R;
import com.coco.smartbj.commen.BaseFragment;
import com.coco.smartbj.utils.Constant;
import com.coco.smartbj.utils.HttpCallbackListener;
import com.coco.smartbj.utils.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 新闻中心的fragment.
 */

public class NewsFragment extends BaseFragment {
    private static final String TAG = "NewsFragment";
    @Override
    protected void initData() {
        HttpUtil.sendHttpRequest(Constant.NEWSCENTERURL, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                String s = response.toString();
                Log.i(TAG, "onFinish: " + s);
            }

            @Override
            public void onError(Exception e) {

            }
        });

       HttpUtil.sendOkHttpRequest(Constant.NEWSCENTERURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, "onResponse: " + s);
            }
        });
    }

    @Override
    protected void initEvecnt() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }
}
