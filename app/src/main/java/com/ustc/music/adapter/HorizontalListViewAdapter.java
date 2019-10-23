package com.ustc.music.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ustc.music.R;
import com.ustc.music.view.RoundImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorizontalListViewAdapter extends BaseAdapter {

    private  Context context;
    //数据源
    private  List<Map<String, String>> strings;

    public HorizontalListViewAdapter(Context applicationContext, List<Map<String, String>> strings) {
        this.context = applicationContext;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            //初次使用适配器 convertView为空
            //所以先初始化 convertView
            convertView = View.inflate(context, R.layout.item_horizontallistview, null);
            //该类中包含了父容器Layout中的所有小组件
            holder = new ViewHolder();
            //初始化父容器中Layout小组件的View
            holder.title = convertView.findViewById(R.id.title);
            holder.roundImageView = convertView.findViewById(R.id.round_img);
            //将小组件们放入convertView以供下次能直接拿出使用
            holder.accessNum = convertView.findViewById(R.id.access_num);
            convertView.setTag(holder);
        }else {
            //convertView已经是初始化了的，所以直接丛里面将小组件拿出来用
            holder = (ViewHolder) convertView.getTag();
        }

        //为小组件初始化数据
        holder.title.setText(strings.get(position).get("title"));
        String access_num = strings.get(position).get("access_num");
        if(access_num != null) holder.accessNum.setText(access_num);
        else holder.accessNum.setVisibility(View.INVISIBLE);
        String id = strings.get(position).get("id");
        convertView.setTag(R.id.title, id);
        Glide.with(context)
                .load(strings.get(position).get("coverUrl")) //加载url
                .placeholder(R.drawable.img300) //默认的图片
                .error(R.drawable.img300) //图片加载失败显示的图片
                .into(holder.roundImageView); //将从网络中加载的图片放入imageView中
        return convertView;
    }
}
//将Layout中的要用到的小组件打包
class ViewHolder {
    RoundImageView roundImageView;
    TextView title;
    TextView accessNum;
    String id;
}