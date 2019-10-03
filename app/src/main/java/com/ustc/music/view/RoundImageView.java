package com.ustc.music.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.ustc.music.R;

public class RoundImageView extends AppCompatImageView {
    float width, height;
    int radiusDP;
    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        context.getTheme().ob
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0);
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.RoundImageView_radius:
                    radiusDP = a.getDimensionPixelSize(attr, 30);
                    break;
            }
        }
        a.recycle();
    }
    
    

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width >= radiusDP && height > radiusDP) {
            Path path = new Path();
            //四个圆角
            path.moveTo(radiusDP, 0);
            path.lineTo(width - radiusDP, 0);
            path.quadTo(width, 0, width, radiusDP);
            path.lineTo(width, height - radiusDP);
            path.quadTo(width, height, width - radiusDP, height);
            path.lineTo(radiusDP, height);
            path.quadTo(0, height, 0, height - radiusDP);
            path.lineTo(0, radiusDP);
            path.quadTo(0, 0, radiusDP, 0);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }
}
