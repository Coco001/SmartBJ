package com.coco.smartbj.newstpipage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.CharacterPickerDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.coco.smartbj.R;
import com.coco.smartbj.activity.NewsDetailActivity;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xiao.free.refreshlayoutlib.SwipeRefreshLayout;
import xiao.free.refreshlayoutlib.base.OnLoadMoreListener;
import xiao.free.refreshlayoutlib.base.OnRefreshListener;

/**
 * 新闻中心页签对应的页面
 */

public class TPINewsCenterPager implements OnRefreshListener, OnLoadMoreListener {
    private Context mContext;
    private NewsCenterData.NewsData.ViewTagData mViewTagData;

    private SwipeRefreshLayout mRefreshLayout;//刷新的根组件
    private ListView mListView;//刷新的list，在该组件中添加banner
    private Banner mBanner;//被添加的banner

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
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        initData();
    }

    private void initEvent(TPINewsData data) {
        //处理数据
        processData(data);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前新闻的连接
                TPINewsData.TPINewsData_Data.TPINewsData_Data_ListNewsData tpiNewsData_Data_ListNewsData = mNews.get(position);
                String newsurl = tpiNewsData_Data_ListNewsData.url;
                String newsid = tpiNewsData_Data_ListNewsData.id;//获取新闻的id

                //获取新闻的标记 id
                String readIDs = Sptools.getString(mContext, Constant.READNEWSIDS, null);
                if (TextUtils.isEmpty(readIDs)) {
                    readIDs = newsid;//保存当前新闻的id
                } else {
                    readIDs += "," + (Integer.parseInt(newsid)-1);//添加保存新闻id
                }
                //重新保存读过的新闻的id
                Sptools.setString(mContext,Constant.READNEWSIDS, readIDs);
                // 修改读过的新闻字体颜色
                //告诉界面更新
                mListNewsAdapter.notifyDataSetChanged();
                //跳转到新闻页面显示新闻
                Intent newsActivity = new Intent(mContext, NewsDetailActivity.class);
                newsActivity.putExtra("newsurl", newsurl);
                mContext.startActivity(newsActivity);
            }
        });
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
        mListView.setAdapter(mListNewsAdapter);
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
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(imagesUrl);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Accordion);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    @Override
    public void onLoadMore() {
        List<TPINewsData.TPINewsData_Data.TPINewsData_Data_ListNewsData> newTitles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newTitles.add(mNews.get(i));
        }
        mNews.addAll(newTitles);
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setLoadingMore(false);
                mListNewsAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onRefresh() {
        List<TPINewsData.TPINewsData_Data.TPINewsData_Data_ListNewsData> newTitles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            newTitles.add(mNews.get(i));
        }
        newTitles.addAll(mNews);
        mNews.removeAll(mNews);
        mNews.addAll(newTitles);

        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                mListNewsAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片
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
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_news_list, null);
        mListView = (ListView) mRefreshLayout.findViewById(R.id.swipe_target);
        //获取banner组件的view
        View inflate = inflater.inflate(R.layout.banner_item, null);
        //获取屏幕的尺寸
        DisplayMetrics size = Uiutils.getScreenSize(mContext);
        //设置banner的尺寸
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(size.widthPixels, size.heightPixels / 3);
        inflate.setLayoutParams(params);

        mBanner = (Banner) inflate.findViewById(R.id.news_banner);
        //将banner添加到mSwipeRefreshLayout的头部
        mListView.addHeaderView(mBanner);
    }

    public View getRootView() {
        return mRefreshLayout;
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
            ViewHolder holder;
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
