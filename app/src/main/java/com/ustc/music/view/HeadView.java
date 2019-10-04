package com.ustc.music.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ustc.music.R;

public class HeadView extends LinearLayout {

    private ImageView leftImgView ;
    private ImageView rightImgView;
    private TextView title;
    private int leftImageViewId;
    private int rightImageViewId;
    private String titleText;

    public HeadView(Context context) {
        this(context, null);
    }

    public HeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.head_layout, this);
        leftImgView = findViewById(R.id.leftImg);
        rightImgView = findViewById(R.id.rightImg);
        title = findViewById(R.id.title);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HeadView, defStyleAttr, defStyleRes);

        leftImageViewId = typedArray.getResourceId(R.styleable.HeadView_leftImg, R.drawable.comeback);
        rightImageViewId = typedArray.getResourceId(R.styleable.HeadView_rightImg, R.drawable.search);
        titleText = typedArray.getString(R.styleable.HeadView_title);

        typedArray.recycle();


        leftImgView.setImageResource(leftImageViewId);
        rightImgView.setImageResource(rightImageViewId);
        title.setText(titleText);

//        LinearLayout.inflate(context, R.layout.head_layout, this);
    }
}
