package com.ustc.music.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ustc.music.R;
import com.ustc.music.adapter.MusicListAdapter;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.entity.Music;

import java.util.List;
import java.util.Map;

public class MyDialog {

    public static void showBottomDialog(Context context){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context,R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(context,R.layout.dialog_custom_layout,null);
        dialog.setContentView(view);

        List<Music> list = ((BaseActivity) context).getList();

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        ListView listView = dialog.findViewById(R.id.music_list);
        MusicListAdapter adapter = new MusicListAdapter(context, list, dialog);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, clickView, position, id) -> {
            dialog.dismiss();
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}
