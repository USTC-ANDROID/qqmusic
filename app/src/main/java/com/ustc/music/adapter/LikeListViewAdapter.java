package com.ustc.music.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustc.music.R;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.db.MyDatabaseHelper;
import com.ustc.music.entity.Music;

import java.util.List;

public class LikeListViewAdapter extends BaseAdapter {

    private BaseActivity context;
    private List<Music> datas;
    MyDatabaseHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;

    public LikeListViewAdapter(BaseActivity context, List<Music> datas) {
        this.context = context;
        this.datas = datas;
        mDatabaseHelper = new MyDatabaseHelper(context);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
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
        Holder holder;
        if(convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.music_list_item, null);
            holder.index = convertView.findViewById(R.id.music_index);
            holder.name = convertView.findViewById(R.id.music_name);
            holder.author = convertView.findViewById(R.id.music_author);
            holder.addLike = convertView.findViewById(R.id.add_like);
            holder.addLike.setOnClickListener(e -> {
                mSqLiteDatabase.delete("music", "mid = ?",
                        new String[] {datas.get(position).getMid()});
                datas.remove(position);
                LikeListViewAdapter.this.notifyDataSetChanged();
            });
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.index.setText("" + (position + 1));
        holder.name.setText(datas.get(position).getTitle());
        holder.author.setText(datas.get(position).getAuthor());
        holder.addLike.setImageResource(R.drawable.liking);
        return convertView;
    }

    class Holder {
        TextView index;
        TextView name;
        TextView author;
        ImageView addLike;
    }
}
