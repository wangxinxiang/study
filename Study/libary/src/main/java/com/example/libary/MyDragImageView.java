package com.example.libary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by wang on 2017/1/17.
 * 具有共享元素的可拖拽图片
 */

public class MyDragImageView extends ImageView {
    private static float MIN_PERCENT = 0.2f;

    private Paint mPaint;

    private float mTranslateX,mTranslateY;  //图片偏移坐标
    private float mScale = 1;   //图片缩放比例
    private int mAlpha = 255;     //图片透明度

    private int mWidth,mHeight;     //图片的宽和高
    private float mDownX,mDownY;      //down操作时的坐标
    private boolean isDragImage = false;    //是否处于拖拽状态

    private onDragImageViewListener mListener;

    public MyDragImageView(Context context) {
        this(context, null);
    }

    public MyDragImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyDragImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyDragImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAlpha(mAlpha);
        canvas.drawRect(0, 0, mTranslateX, mTranslateY, mPaint);
        canvas.scale(mScale, mScale, mWidth / 2, mHeight / 2);
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //viewpager且不处于拖拽状态
                if (mTranslateY == 0 && mTranslateX != 0 && !isDragImage) {
                    return super.dispatchTouchEvent(event);
                }

                //单点操作，没有缩放操作
                if (event.getPointerCount() == 1) {
                    isDragImage = true;
                    onEventMove(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getPointerCount() == 1) {
                    isDragImage = false;
                    if (mListener != null) {
                        mListener.onExit(this, mTranslateX, mTranslateY);
                    }
                }
        }

        return super.dispatchTouchEvent(event);
    }

    private void onEventMove(MotionEvent event) {
        mTranslateX = event.getX() - mDownX;
        mTranslateY = event.getY() - mDownY;

        float percent = 1 - mTranslateY / mHeight;  //缩放比例
        if (percent < MIN_PERCENT) percent = MIN_PERCENT;

        if (mTranslateY > 0) {
            mScale = 1 - percent;
            mAlpha = (int) (255 * (1 - percent));
        } else {
            mScale = 1;
            mAlpha = 255;
        }

        invalidate();
    }

    public void setmListener(onDragImageViewListener mListener) {
        this.mListener = mListener;
    }

    public interface onDragImageViewListener {
        void onExit(MyDragImageView view, float translateX, float translateY);
    }
}
