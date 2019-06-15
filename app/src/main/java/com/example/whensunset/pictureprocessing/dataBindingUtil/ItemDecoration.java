package com.example.whensunset.pictureprocessing.dataBindingUtil;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by whensunset on 2018/3/3.
 */

public class ItemDecoration {
    public static final String TAG = "何时夕:MainActivity";

    public static RecyclerView.ItemDecoration defaultGridLayoutItemDecoration() {
        return new RecyclerView.ItemDecoration(){
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(5 , 5 , 5 , 5);
            }
        };
    }

    public static RecyclerView.ItemDecoration defaultItemDecoration() {
        return new RecyclerView.ItemDecoration(){
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(5 , 0 , 5 , 0);
            }
        };
    }
}
