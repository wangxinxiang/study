package com.example.mylibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by wang on 2017/1/17.
 * 可拖拽图片
 */

public class MyDragImageView extends ImageView {
    private static float MIN_PERCENT = 0.2f;
    private static long DURATION = 300;
    private static float MAX_TRANSLATE_Y = 500;

    private Paint mPaint;

    private float mTranslateX,mTranslateY;  //图片偏移坐标
    private float mScale = 1;   //图片缩放比例
    private int mAlpha = 255;     //图片透明度

    private int mWidth,mHeight;     //图片的宽和高
    private float mDownX,mDownY;      //down操作时的坐标
    private boolean isDragImage = false;    //是否处于拖拽状态
    private boolean canFinish = false;      //

    private OnDragImageViewListener onDragImageViewListener;
    private OnDragImageClickListener onDoubleClickListener;

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
        setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAlpha(mAlpha);
        canvas.drawRect(0, 0, 2000, 3000, mPaint);
        canvas.translate(mTranslateX, mTranslateY);
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
                canFinish = !canFinish;
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
                    return true ;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getPointerCount() == 1) {
                    if (mTranslateY > MAX_TRANSLATE_Y) {
                        if (onDragImageViewListener != null) {
                            onDragImageViewListener.onExit(this, mTranslateX, mTranslateY);
                        }
                    } else {
                        backAnimation();
                    }
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mTranslateX == 0 && mTranslateY == 0 && canFinish) {
                                if (onDoubleClickListener != null) {   //单机击事件
                                    onDoubleClickListener.onDragImageDoubleClick(MyDragImageView.this);
                                }
                            }
                            canFinish = false;
                        }
                    }, 300);
                    isDragImage = false;
                }
        }

        return super.dispatchTouchEvent(event);
    }

    private void onEventMove(MotionEvent event) {
        mTranslateX = event.getX() - mDownX;
        mTranslateY = event.getY() - mDownY;

        float percent = 1 - mTranslateY / MAX_TRANSLATE_Y;  //缩放比例
        if (percent < 0) percent = 0;
        if (mTranslateY > 0) {
            mAlpha = (int) (255 *  percent);

            if (percent < MIN_PERCENT) percent = MIN_PERCENT;       //缩放性有限制
            mScale = percent;
        } else {
            mScale = 1;
            mAlpha = 255;
        }
        invalidate();
    }

    /**
     * 通过动画执行图片回到原始位置渐变
     */
    private void backAnimation() {
        startAlphaAnimation();
        startTranslateXAnimation();
        startTranslateYAnimation();
        startScaleAnimation();
    }

    private void startAlphaAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mAlpha, 255);
        valueAnimator.setDuration(DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAlpha = (int) animation.getAnimatedValue();
            }
        });
        valueAnimator.start();
    }

    private void startTranslateXAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mTranslateX, 0);
        valueAnimator.setDuration(DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateX = (float) animation.getAnimatedValue();
            }
        });
        valueAnimator.start();
    }

    private void startTranslateYAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mTranslateY, 0);
        valueAnimator.setDuration(DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateY = (float) animation.getAnimatedValue();
            }
        });
        valueAnimator.start();
    }

    private void startScaleAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mScale, 1);
        valueAnimator.setDuration(DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScale = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void setOnDragImageViewListener(OnDragImageViewListener mListener) {
        this.onDragImageViewListener = mListener;
    }

    public void setOnDoubleClickListener(OnDragImageClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    public interface OnDragImageViewListener {
        void onExit(MyDragImageView view, float translateX, float translateY);
    }

    public interface OnDragImageClickListener {
        void onDragImageDoubleClick(MyDragImageView view);
    }
}
