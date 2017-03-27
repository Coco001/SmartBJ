package com.coco.smartbj.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.coco.smartbj.R;
import com.coco.smartbj.commen.BaseActivity;

/**
 * 新闻详情页面的activity
 */

public class NewsDetailActivity extends BaseActivity {
    private ImageButton news_content_back;         //返回键
    private ImageButton ib_base_content_textsize;  //字体大小
    private ImageButton ib_base_content_share;     //分享
    private WebView news_content_content;          //新闻内容
    private ProgressBar news_content_pb;           //进度条
    private WebSettings wv_setting;               //webview的设置
    private static final String TAG = "NewsDetailActivity";
    @Override
    protected void initView() {
        //返回的按钮
        news_content_back = (ImageButton) findViewById(R.id.news_content_back);
        //修改新闻的字体
        ib_base_content_textsize = (ImageButton) findViewById(R.id.ib_base_content_textsize);
        //分享
        ib_base_content_share = (ImageButton) findViewById(R.id.ib_base_content_share);
        //显示新闻
        news_content_content = (WebView) findViewById(R.id.news_content_content);
        //加载新闻的进度
        news_content_pb = (ProgressBar) findViewById(R.id.news_content_pb);
        //news_content_pb.setVisibility(View.VISIBLE);

        //控制WebView的显示设置
        wv_setting = news_content_content.getSettings();
        //设置放大缩小
        wv_setting.setBuiltInZoomControls(true);
        wv_setting.setJavaScriptEnabled(true);//可以去编译javsscript脚本
        wv_setting.setUseWideViewPort(true);//双击放大或缩小
        //给WebView添加一个新闻加载完成的监听事件
        news_content_content.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                news_content_pb.setVisibility(View.GONE);//隐藏进度条
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished: " +"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        });
    }

    @Override
    protected void initEvent() {
        //创建三个按钮公用的监听器
        View.OnClickListener listener = new View.OnClickListener() {
            int textSizeIndex = 2;// 0. 超大号 1,大号  2 正常  3 小号  4 超小号
            private AlertDialog dialog;

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.news_content_back://返回键
                        finish();//关闭当前新闻页面
                        break;
                    case R.id.ib_base_content_textsize://修改字体大小
                        //通过对话框来修改字体大小 五种字体大小
                        showChangeTextSizeDialog();
                        //设置字体大小 wv_setting.setTextSize(TextSize.)
                    case R.id.ib_base_content_share://分享
                        //SharedAppUtils.showShare(getApplicationContext());
                        break;
                }
            }

            private void showChangeTextSizeDialog() {
                AlertDialog.Builder ab = new AlertDialog.Builder(NewsDetailActivity.this);
                ab.setTitle("改变字体大小");
                String[] textSize = new String[]{"超大号","大号","正常","小号","超小号"};
                ab.setSingleChoiceItems(textSize, textSizeIndex, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textSizeIndex = which;
                        setTextSize();

                    }
                });
                dialog = ab.create();
                dialog.show();
            }

            private void setTextSize() {
                switch (textSizeIndex) {
                    case 0://超大号
                        wv_setting.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1: //大号
                        wv_setting.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2: //正常
                        wv_setting.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3: //小号
                        wv_setting.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4: //最小号
                        wv_setting.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                dialog.dismiss();
            }
        };
        //给返回键添加事件
        news_content_back.setOnClickListener(listener);
        ib_base_content_textsize.setOnClickListener(listener);
        ib_base_content_share.setOnClickListener(listener);

        news_content_content.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                news_content_pb.setVisibility(View.GONE);//隐藏进度条
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished: " +"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        });
    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra("newsurl");
        if (TextUtils.isEmpty(url)){
            Toast.makeText(getApplicationContext(), "链接错误",Toast.LENGTH_SHORT).show();
        } else {
            news_content_content.loadUrl(url);
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.news_content;
    }

}
