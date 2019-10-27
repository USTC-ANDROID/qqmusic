package com.ustc.music.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.music.R;

public class SmileToast extends Toast {

    public SmileToast(Context context) {
        super(context);

    }


    public static SmileToast makeSmileToast(Context context, String content, int duration) {
        SmileToast toast = new SmileToast(context);
        View inflate = LayoutInflater.from(context)
                .inflate(R.layout.view_smile_toast, null);
        TextView tv = inflate.findViewById(R.id.tv_toast);
        tv.setText(content);
        toast.setView(inflate);
        toast.setDuration(duration);
        return toast;
    }
}
