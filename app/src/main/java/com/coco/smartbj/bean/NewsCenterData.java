package com.coco.smartbj.bean;

import java.util.List;

/**
 * 新闻中心的bean
 */
public class NewsCenterData {
    public int retcode;
    public List<NewsData> data;//新闻的数据

    public class NewsData {
        public List<ViewTagData> children;

        public class ViewTagData {
            public String id;
            public String title;
            public int type;
            public String url;
        }

        public String id;
        public String title;
        public int type;
        public String url;
        public String url1;
        public String dayurl;
        public String excurl;
        public String weekurl;
    }

    public List<String> extend;

}
