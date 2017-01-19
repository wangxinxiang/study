package com.example.wang.study.mydragimage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.WindowManager;

import com.example.mylibrary.MyDragImageView;
import com.example.wang.study.R;

/**
 * Created by wang on 2017/1/17.
 * MyDragImageView使用
 */

public class ShowDragImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.avtivity_show_darg_image);
        MyDragImageView myDragImageView = (MyDragImageView) this.findViewById(R.id.my_drag_view);
        myDragImageView.setOnDragImageViewListener(new MyDragImageView.OnDragImageViewListener() {
            @Override
            public void onExit(MyDragImageView view, float translateX, float translateY) {
                onBackPressed();
            }
        });
        myDragImageView.setOnDoubleClickListener(new MyDragImageView.OnDragImageClickListener() {
            @Override
            public void onDragImageDoubleClick(MyDragImageView view) {
                onBackPressed();
            }
        });

        /**
         * 共享动画
         */
        ViewCompat.setTransitionName(myDragImageView, "image");
    }
}
