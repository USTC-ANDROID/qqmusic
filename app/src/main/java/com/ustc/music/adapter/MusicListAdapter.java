package com.ustc.music.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ustc.music.R;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.entity.Music;
import com.ustc.music.entity.Singer;
import com.ustc.music.view.RoundImageView;

import java.util.List;

public class MusicListAdapter extends BaseAdapter {
    private Context context;
    private List<Music> datas;
    private Dialog dialog;

    public MusicListAdapter(Context context, List<Music> datas, Dialog dialog) {
        this.context = context;
        this.datas = datas;
        this.dialog = dialog;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.all_music_list_item, null);
            holder = new Holder();
            holder.name = convertView.findViewById(R.id.music_name);
            holder.author = convertView.findViewById(R.id.music_author);
            holder.remove = convertView.findViewById(R.id.remove_music);
            holder.musicItem = convertView.findViewById(R.id.music_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String mid = datas.get(position).getMid();
        holder.remove.setOnClickListener(e -> {
            ((BaseActivity) context).remove(mid);
            dialog.dismiss();
        });
        holder.musicItem.setOnClickListener(e -> {
            ((BaseActivity) context).playByMid(mid);
            dialog.dismiss();
        });
        holder.name.setText(datas.get(position).getTitle());
        holder.author.setText(datas.get(position).getAuthor());

        return convertView;
    }

    class Holder {
        TextView name;
        TextView author;
        TextView remove;
        LinearLayout musicItem;
    }
}
