package com.coco.smartbj.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.coco.smartbj.R;
import com.coco.smartbj.bean.NewsCenterData;
import com.coco.smartbj.commen.BaseFragment;
import com.coco.smartbj.newstpipage.TPINewsCenterPager;
import com.coco.smartbj.utils.Constant;
import com.coco.smartbj.utils.HttpUtil;
import com.coco.smartbj.utils.MyApplication;
import com.coco.smartbj.utils.Sptools;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 新闻中心的fragment.
 */

public class NewsFragment extends BaseFragment {
    private TabLayout mTabLayout;
    private List<String> mTitleText;
    private ViewPager mViewPager;
    private List<NewsCenterData.NewsData.ViewTagData> mDatas = new ArrayList<>();
    private NewsCenterData mNewsCenterData;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mDatas = (List<NewsCenterData.NewsData.ViewTagData>) msg.obj;
                    initEvecnt();
                    break;
            }
            return true;
        }
    });

    @Override
    protected void initData() {
        mTitleText = new ArrayList<>();
        mTabLayout = (TabLayout) getActivity().findViewById(R.id.indicator_title);
        mViewPager = (ViewPager) getActivity().findViewById(R.id.indicator_pager);
        //首先检查本地是否有数据
        String jsonData = Sptools.getString(MyApplication.context, Constant.NEWSCENTERURL, "");
        if (!TextUtils.isEmpty(jsonData)) {
            parseDataWithFastJson(jsonData);
        } else {
            //然后再通过网络获取数据
            HttpUtil.sendOkHttpRequest(Constant.NEWSCENTERURL, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    //将数据缓存到本地
                    Sptools.setString(MyApplication.context, Constant.NEWSCENTERURL, s);
                    parseDataWithFastJson(s);
                }
            });
        }
    }

    //使用fastjson解析数据
    private void parseDataWithFastJson(String s) {
        if (mNewsCenterData == null) {
            mNewsCenterData = JSON.parseObject(s, NewsCenterData.class);
        }
        List<NewsCenterData.NewsData.ViewTagData> children = mNewsCenterData.data.get(0).children;
        Message message = new Message();
        message.what = 0;
        message.obj = children;
        mHandler.sendMessage(message);
    }

    @Override
    protected void initEvecnt() {
        int size = mDatas.size();
        for (int i = 0; i < size; i++) {
            mTitleText.add(mDatas.get(i).title);
        }
        mPagerAdapter adapter = new mPagerAdapter();
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class mPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleText.get(position);
        }

        @Override
        public int getCount() {
            return mTitleText.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TPINewsCenterPager newsCenterPager = new TPINewsCenterPager(MyApplication.context, mDatas.get(position));
            View view = newsCenterPager.getRootView();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    //使用Gson解析数据
    private void parseData(String s) {
        Gson gson = new Gson();
        NewsCenterData newsCenterData = gson.fromJson(s, NewsCenterData.class);
    }
}
