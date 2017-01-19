package com.example.wang.study;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wang.study.mydragimage.MyDragImageActivity;

/**
 * 控制 SystemBar 相关：

 FULLSCREEN
 HIDE_NAVIGATION
 LOW_PROFILE

 布局相关：

 LAYOUT_SCREEN
 LAYOUT_HIDE_NAVIGATION
 LAYOUT_STABLE

 沉浸式相关 (4.4 引入)：

 IMMERSIVE
 IMMERSIVE_STICKY
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        findViewById(R.id.iv_welcome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MyDragImageActivity.class);
                startActivity(intent);
            }
        });
    }
}
