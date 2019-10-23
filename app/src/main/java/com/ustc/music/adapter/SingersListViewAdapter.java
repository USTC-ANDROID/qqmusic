package com.ustc.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ustc.music.R;
import com.ustc.music.entity.Singer;
import com.ustc.music.view.RoundImageView;

import java.util.List;

public class SingersListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Singer> datas;

    public SingersListViewAdapter(Context context, List<Singer> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.singer_item_layout, null);
            holder = new Holder();
            holder.iv = convertView.findViewById(R.id.head);
            holder.tv = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv.setText(datas.get(position).getName());
        Glide.with(context)
                .load(datas.get(position).getPic())
                .placeholder(R.drawable.img300)
                .error(R.drawable.img300)
                .into(holder.iv);
        return convertView;
    }

    class Holder {
        RoundImageView iv;
        TextView tv;
    }
}
