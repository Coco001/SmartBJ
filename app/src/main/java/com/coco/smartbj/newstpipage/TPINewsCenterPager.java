package com.coco.smartbj.newstpipage;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.coco.smartbj.R;
import com.coco.smartbj.bean.NewsCenterData;
import com.coco.smartbj.bean.TPINewsData;
import com.coco.smartbj.utils.Constant;
import com.coco.smartbj.utils.HttpUtil;
import com.coco.smartbj.utils.Sptools;
import com.coco.smartbj.utils.Uiutils;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.ScaleInOutTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 新闻中心页签对应的页面
 */

public class TPINewsCenterPager {
    private Context mContext;
    private NewsCenterData.NewsData.ViewTagData mViewTagData;
    private View mRoot;
    private Banner news_banner;
    private ListView list;
    private TPINewsData mTpiNewsData;
    private TPINewsData mData = new TPINewsData();
    private List<TPINewsData.TPINewsData_Data.TPINewsData_Data_ListNewsData> mNews;
    private ListNewsAdapter mListNewsAdapter;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mData = (TPINewsData) msg.obj;
                    initEvent(mData);
                    break;
            }
            return true;
        }
    });

    public TPINewsCenterPager(Context context, NewsCenterData.NewsData.ViewTagData data) {
        mContext = context;
        mViewTagData = data;
        initView();
        initData();
    }

    private void initEvent(TPINewsData data) {
        //处理数据
        processData(data);

    }

    private void processData(TPINewsData data) {
        //设置轮播图的数据
        setBanner(data);
        //设置listview
        setListView(data);
    }

    private void setListView(TPINewsData data) {
        mNews = data.data.news;
        //创建新闻list的适配器
        mListNewsAdapter = new ListNewsAdapter();
        list.setAdapter(mListNewsAdapter);

        mListNewsAdapter.notifyDataSetChanged();
    }

    private void setBanner(TPINewsData data) {
        List<TPINewsData.TPINewsData_Data.TPINewsData_Data_LunBoData> topnews = data.data.topnews;
        List<String> imagesUrl = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < topnews.size(); i++) {
            imagesUrl.add(topnews.get(i).topimage);
            titles.add(topnews.get(i).title);
        }
        //设置banner样式
        news_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        news_banner.setImageLoader(new GlideImageLoader());
        news_banner.setImages(imagesUrl);
        //设置banner动画效果
        news_banner.setBannerAnimation(Transformer.Accordion);
        //设置标题集合（当banner样式有显示title时）
        news_banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        news_banner.isAutoPlay(true);
        //设置轮播时间
        news_banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        news_banner.setIndicatorGravity(BannerConfig.RIGHT);
        news_banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
        //banner设置方法全部调用完毕时最后调用
        news_banner.start();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片简单用法
            Picasso.with(context).load((String) path).into(imageView);
        }
    }

    private void initData() {
        //首先检查本地是否存在数据缓存
        String localString = Sptools.getString(mContext, mViewTagData.url, "");
        if (!TextUtils.isEmpty(localString)) {
            parseDataWithFastJson(localString);
        } else {
            //从网络获取数据
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        HttpUtil.sendOkHttpRequest(Constant.SERVERURL + mViewTagData.url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                //保存数据到本地
                Sptools.setString(mContext, mViewTagData.url, string);
                parseDataWithFastJson(string);
            }
        });
    }

    //使用fastjson解析数据
    private void parseDataWithFastJson(String s) {
        mTpiNewsData = new TPINewsData();
        mTpiNewsData = JSON.parseObject(s, TPINewsData.class);
        Message message = new Message();
        message.what = 0;
        message.obj = mTpiNewsData;
        mHandler.sendMessage(message);
    }

    private void initView() {
        //获取页签对应页面的根布局
        mRoot = View.inflate(mContext, R.layout.fragment_news_banner, null);
        View mBanner = View.inflate(mContext, R.layout.banner_item, null);
        list = (ListView) mRoot.findViewById(R.id.list);
        news_banner = (Banner) mBanner.findViewById(R.id.news_banner);
        //获取屏幕的尺寸
        DisplayMetrics size = Uiutils.getScreenSize(mContext);
        //设置banner的尺寸
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(size.widthPixels, size.heightPixels / 2);
        news_banner.setLayoutParams(params);
        list.addHeaderView(mBanner);
    }

    public View getRootView() {
        return mRoot;
    }

    //listview的适配器
    private class ListNewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNews.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.tpi_news_listview_item, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_tpi_news_listview_item_icon);
                holder.iv_newspic = (ImageView) convertView.findViewById(R.id.iv_tpi_news_listview_item_pic);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_tpi_news_listview_item_title);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_tpi_news_listview_item_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TPINewsData.TPINewsData_Data.TPINewsData_Data_ListNewsData data = mNews.get(position);
            String id = data.id;
            String readnewsIds = Sptools.getString(mContext, Constant.READNEWSIDS, "");
            if (TextUtils.isEmpty(readnewsIds) || !readnewsIds.contains(id)) {
                //空 没有保存过id
                holder.tv_title.setTextColor(Color.BLACK);
                holder.tv_time.setTextColor(Color.BLACK);
            } else {
                holder.tv_title.setTextColor(Color.GRAY);
                holder.tv_time.setTextColor(Color.GRAY);
            }
            //设置标题
            holder.tv_title.setText(data.title);
            //设置时间
            holder.tv_time.setText(data.pubdate);
            //设置图片
            Picasso.with(mContext).load(data.listimage).into(holder.iv_newspic);
            return convertView;
        }
    }

    private class ViewHolder{
        ImageView iv_newspic;
        TextView tv_title;
        TextView tv_time;
        ImageView iv_icon;
    }

}
