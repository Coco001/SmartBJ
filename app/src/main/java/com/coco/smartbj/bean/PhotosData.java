

package com.coco.smartbj.bean;

import java.util.List;

public class PhotosData {

    public int retcode;
    public PhotosData_Data data;

    public class PhotosData_Data {
        public String countcommenturl;
        public String more;
        public String title;

        public List<PhotosNews> news;

        public class PhotosNews {
            public boolean comment;
            public String commentlist;//	http://zhbj.qianlong.com/static/api/news/10003/14/71414/comment_1.json
            public String commenturl;//	http://zhbj.qianlong.com/client/user/newComment/71414
            public int id;    //71414
            public String largeimage;//	http://zhbj.qianlong.com/static/images/2014/09/20/47/16231787033RH7.jpg
            public String listimage;//	http://10.0.2.2:8080/zhbj/photos/images/115913765474ZV.jpg
            public String pubdate;//	2014-09-20 11:49
            public String smallimage;//	http://zhbj.qianlong.com/static/images/2014/09/20/56/1622255182UXNV.jpg
            public String title;//	逃脱大师成功挑战百米高空<极限逃生>
            public String type;//	news
            public String url;//	http://zhbj.qianlong.com/static/html/2014/09/20/774C6B5041671D7D6A25.html
        }
    }
}
