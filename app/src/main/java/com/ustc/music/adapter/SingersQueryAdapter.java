package com.ustc.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ustc.music.R;

public class SingersQueryAdapter extends BaseAdapter {

    private String[] values;

    private Context context;

    private int selected;

    public SingersQueryAdapter(String[] values, Context context, int selected) {
        this.values = values;
        this.context = context;
        this.selected = selected;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SingersQueryItem holder = null;
        if(convertView == null) {
            holder = new SingersQueryItem();
            convertView = LayoutInflater.from(context).inflate(R.layout.singers_query_item, null);
            holder.tv = convertView.findViewById(R.id.query_item);
            convertView.setTag(holder);
        } else {
            holder = (SingersQueryItem) convertView.getTag();
        }
        holder.tv.setText(values[position]);
        if(position == selected) {
            convertView.findViewById(R.id.query_item_bg).setBackgroundResource((R.drawable.layout_blue_circle_border_radius_50));
            holder.tv.setTextColor(context.getResources().getColor(R.color.white, null));
        }
        return convertView;
    }
}

class SingersQueryItem {
    TextView tv;
}