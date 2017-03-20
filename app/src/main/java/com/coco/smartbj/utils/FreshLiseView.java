package com.coco.smartbj.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义listview，可以实现上下滑动刷新的功能
 */

public class FreshLiseView extends ListView {
    public FreshLiseView(Context context) {
        super(context);
    }

    public FreshLiseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FreshLiseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
