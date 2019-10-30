package com.ustc.music.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ustc.music.R;

public class RoundLinearLayout extends LinearLayout {
    float width, height;
    int radiusDP;
    Paint p;
    public RoundLinearLayout(Context context) {
        this(context, null);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
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
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        p = new Paint();
        p.setColor(Color.rgb(71,53,38));// 设置红色
        p.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (width >= radiusDP && height > radiusDP) {
            Path path = new Path();
            //四个圆角
            canvas.drawCircle(width - width / 2, height - width / 2, width / 2, p);
            path.addCircle(width - width / 2, height - width / 2, width / 2 - 20,  Path.Direction.CW);
            canvas.clipPath(path);
//        }
        super.onDraw(canvas);
    }
}
