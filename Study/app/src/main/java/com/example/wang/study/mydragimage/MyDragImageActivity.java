package com.example.wang.study.mydragimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.WindowManager;

import com.example.wang.study.R;


/**
 * Created by wang on 2017/1/17.
 * MyDragImageView使用
 */

public class MyDragImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_drag_image);


    }

    /**
     * 共享动画
     */
    public void onClick(View view) {
        Intent intent = new Intent(this, ShowDragImageActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "image");
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
    }
}
