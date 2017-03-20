package com.coco.smartbj.fragment;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.coco.smartbj.R;
import com.coco.smartbj.bean.NewsCenterData;
import com.coco.smartbj.commen.BaseFragment;
import com.coco.smartbj.utils.Constant;
import com.coco.smartbj.utils.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 组图的fragment.
 */

public class PicsFragment extends BaseFragment {

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvecnt() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pics;
    }
}
