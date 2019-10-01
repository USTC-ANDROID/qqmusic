package com.ustc.music.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

public class RoundImageView extends AppCompatImageView {
    float width, height;
    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        if (width >= 30 && height > 30) {
            Path path = new Path();
            //四个圆角
            path.moveTo(30, 0);
            path.lineTo(width - 30, 0);
            path.quadTo(width, 0, width, 30);
            path.lineTo(width, height - 30);
            path.quadTo(width, height, width - 30, height);
            path.lineTo(30, height);
            path.quadTo(0, height, 0, height - 30);
            path.lineTo(0, 30);
            path.quadTo(0, 0, 30, 0);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }
}
