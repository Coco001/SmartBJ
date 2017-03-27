package com.coco.smartbj.fragment;

import android.content.pm.ProviderInfo;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.coco.smartbj.R;
import com.coco.smartbj.bean.PhotosData;
import com.coco.smartbj.commen.BaseFragment;
import com.coco.smartbj.utils.Constant;
import com.coco.smartbj.utils.HttpUtil;
import com.coco.smartbj.utils.MyApplication;
import com.coco.smartbj.utils.Sptools;
import com.coco.smartbj.utils.Uiutils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 组图的fragment.
 */
public class PicsFragment extends BaseFragment {

    private static final int COLUMMNONE = 1;//显示的列数
    private static final int COLUMMNTWO = 2;//显示的列数
    private static boolean flag = false;
    GridLayoutManager manager;
    private RecyclerView pic_recyclerview;
    private FloatingActionButton pic_fab;
    private PhotosData mPhotosData;
    List<PhotosData.PhotosData_Data.PhotosNews> picList;
    private static final String TAG = "PicsFragment";

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    picList = (List<PhotosData.PhotosData_Data.PhotosNews>) msg.obj;
                    initEvecnt();
                    break;
                case 1:
                    boolean isTwo = (boolean) msg.obj;
                    showOneOrTwo(isTwo);
                    break;
            }
            return true;
        }
    });
    private int mWidth;
    private int mHeight;

    @Override
    protected void initData() {
        picList = new ArrayList<>();
        DisplayMetrics size = Uiutils.getScreenSize(MyApplication.context);
        mWidth = size.widthPixels;
        mHeight = size.heightPixels;
        initView();
        //检查本地是否有缓存数据
        String string = Sptools.getString(MyApplication.context, Constant.PHOTOSURL, "");
        if (!TextUtils.isEmpty(string)) {
            parseDataWithFastJson(string);
        } else {//从网络获取数据
            HttpUtil.sendOkHttpRequest(Constant.PHOTOSURL, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    //将数据缓存到本地
                    Sptools.setString(MyApplication.context, Constant.PHOTOSURL, s);
                    parseDataWithFastJson(s);
                }
            });
        }
    }

    private void parseDataWithFastJson(String s) {
        if (mPhotosData == null) {
            mPhotosData = JSON.parseObject(s, PhotosData.class);
        }
        List<PhotosData.PhotosData_Data.PhotosNews> newsList = mPhotosData.data.news;
        Message message = new Message();
        message.what = 0;
        message.obj = newsList;
        mHandler.sendMessage(message);
    }

    private void initView() {
        pic_recyclerview = (RecyclerView) getActivity().findViewById(R.id.pic_recyclerview);
        pic_fab = (FloatingActionButton) getActivity().findViewById(R.id.pic_fab);
        showOneOrTwo(false);
    }

    private void showOneOrTwo(boolean flag) {
        if (flag) {
            manager = new GridLayoutManager(MyApplication.context, COLUMMNTWO);

        } else {
            manager = new GridLayoutManager(MyApplication.context, COLUMMNONE);
        }
        pic_recyclerview.setLayoutManager(manager);

    }

    @Override
    protected void initEvecnt() {
        //设置adapter
        pic_recyclerview.setAdapter(new PicAdapter());
        pic_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"更改显示的列数",Snackbar.LENGTH_LONG)
                        .setAction("确认更改", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = !flag;
                        Message message = new Message();
                        message.what = 1;
                        message.obj = flag;
                        mHandler.sendMessage(message);
                    }
                }).show();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pics;
    }

    class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MyApplication.context).inflate(R.layout.pic_cardview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PhotosData.PhotosData_Data.PhotosNews item = picList.get(position);
            holder.title.setText(item.title);
            if (flag) {
                Glide.with(MyApplication.context)
                        .load(item.listimage)
                        .override(mWidth / 2, mHeight / 3)
                        .into(holder.pic);
            } else {
                Glide.with(MyApplication.context)
                        .load(item.listimage)
                        .override(mWidth, mHeight / 2)
                        .into(holder.pic);
            }
        }

        @Override
        public int getItemCount() {
            return picList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CardView mCardView;
            ImageView pic;
            TextView title;
            public ViewHolder(View itemView) {
                super(itemView);
                mCardView = (CardView) itemView;
                pic = (ImageView) itemView.findViewById(R.id.card_iv);
                title = (TextView) itemView.findViewById(R.id.card_tv);
            }
        }
    }


}
