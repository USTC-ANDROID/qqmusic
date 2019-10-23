package com.ustc.music.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.ustc.music.R;

public class ImageHeadScrollView extends ScrollView implements View.OnTouchListener {
    // 记录首次按下位置
    private float mfirstposition = 0;
    // 是否正在放大
    private boolean mscaling = false;

    private View dropzoomview;
    private int dropzoomviewwidth;
    private int dropzoomviewheight;

    public ImageHeadScrollView(Context context) {
        super(context);
    }

    public ImageHeadScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageHeadScrollView(Context context, AttributeSet attrs, int defstyleattr) {
        super(context, attrs, defstyleattr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Log.v("ImageHeadScrollView", "onFinishInflate()");
        init();
    }

    @Override
    protected void onMeasure(int widthmeasurespec, int heightmeasurespec) {
        super.onMeasure(widthmeasurespec, heightmeasurespec);

        Log.v("ImageHeadScrollView", "onMeasure()");
    }

    private void init() {
//        scrollBy(0, 3000);
        post(new Runnable() {
            @Override
            public void run() {
                Log.v("ImageHeadScrollView", "滚动");
//                scrollBy(0, 3000);

                Log.v("ImageHeadScrollView", "滚动完成");
            }
        });
        smoothScrollTo(3000, 3000);
        Log.v("ImageHeadScrollView", "init()");
        setOverScrollMode(OVER_SCROLL_NEVER);
//        if (getChildAt(0) != null) {
//            ViewGroup vg = (ViewGroup) getChildAt(0);
//            if (vg.getChildAt(0) != null) {
//                dropzoomview = vg.getChildAt(0);
//                setOnTouchListener(this);
//
//            }
//        }
        dropzoomview = findViewById(R.id.scroll_view_headimage);
        if(dropzoomview != null) setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.v("ImageHeadScrollView", "onTouch()");
        if (dropzoomviewwidth <= 0 || dropzoomviewheight <= 0) {
            dropzoomviewwidth = dropzoomview.getMeasuredWidth();
            dropzoomviewheight = dropzoomview.getMeasuredHeight();
        }
        Log.v("ImageHeadScrollView", "onTouch()  dropzoomviewwidth = " + dropzoomviewwidth + "   dropzoomviewheight = " + dropzoomviewheight);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //手指离开后恢复图片
                mscaling = false;
                replyImage();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mscaling) {
                    if (getScrollY() == 0) {
                        mfirstposition = event.getY();// 滚动到顶部时记录位置，否则正常返回
                    } else {
                        break;
                    }
                }
                int distance = (int) ((event.getY() - mfirstposition) * 0.6); // 滚动距离乘以一个系数
                if (distance < 0) { // 当前位置比记录位置要小，正常返回
                    break;
                }

                // 处理放大
                mscaling = true;

                Log.v("ImageHeadScrollView", "onTouch() == " + distance);
                setZoom(1 + distance);
                scrollBy(0, -distance);
//                scrollTo(0, -distance);
//                onScrollChanged();
//                return true; // 返回true表示已经完成触摸事件，不再处理
        }
        return false;
    }

    // 回弹动画 (使用了属性动画)
    public void replyImage() {

        Log.v("ImageHeadScrollView", "replyImage()");
        final float distance = dropzoomview.getMeasuredWidth() - dropzoomviewwidth;

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0f, 1.0f).setDuration((long) (distance * 0.7));

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cval = (float) animation.getAnimatedValue();
                setZoom(distance - ((distance) * cval));
            }
        });
        anim.start();

    }

    //缩放0
    public void setZoom(float s) {

        Log.v("ImageHeadScrollView", "setZoom()");
        if (dropzoomviewheight <= 0 || dropzoomviewwidth <= 0) {
            return;
        }

        Log.v("ImageHeadScrollView", "setZoom() == 放大");
        ViewGroup.LayoutParams lp = dropzoomview.getLayoutParams();
        lp.width = (int) (dropzoomviewwidth + s);
        lp.height = (int) (dropzoomviewheight * ((dropzoomviewwidth + s) / dropzoomviewwidth));
        dropzoomview.setLayoutParams(lp);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
