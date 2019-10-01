package com.ustc.music.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.music.R;

public class HorizontalListViewAdapter extends BaseAdapter {

    private final Context context;
    private final int screenWidth;
    private final String[] strings;

    public HorizontalListViewAdapter(Context applicationContext, int screenWidth, String[] strings) {
        this.context = applicationContext;
        this.screenWidth = screenWidth;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_horizontallistview, null);

            holder = new ViewHolder();
//            holder.tv = convertView.findViewById(R.id.test);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //计算每条的宽度
//        ViewGroup.LayoutParams layoutParams = holder.rl.getLayoutParams();
//        layoutParams.width = screenWidth / 7 * 2;
//        holder.rl.setLayoutParams(layoutParams);

//        Toast.makeText(context, strings[position], Toast.LENGTH_LONG).show();
//        holder.tv.setText(strings[position]);
        return convertView;
    }
}

class ViewHolder {
//    TextView tv;
//    RelativeLayout rl;


}