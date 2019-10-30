package com.ustc.music.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustc.music.R;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.db.MyDatabaseHelper;
import com.ustc.music.entity.Music;
import com.ustc.music.url.DataUrl;

import java.util.ArrayList;
import java.util.List;

public class SearchSongRecyclerViewAdapter extends RecyclerView.Adapter<SearchSongRecyclerViewAdapter.ViewHolder> {

    private BaseActivity mContext;
    //    private List<String> dataList = new ArrayList<>();
    private List<Music> dataList = new ArrayList<>();
    MyDatabaseHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;

    public void addAllData(List<Music> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public SearchSongRecyclerViewAdapter(BaseActivity context) {
        mContext = context;
        mDatabaseHelper = new MyDatabaseHelper(context);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView songTitle;
        public TextView singerName;
        public View itemView;
        public ImageView addLike;

        public ViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
            singerName = itemView.findViewById(R.id.searchSingerName);
            addLike = itemView.findViewById(R.id.add_like);
            this.itemView = itemView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.title.setText(dataList.get(position));
        Music music = dataList.get(position);
        holder.songTitle.setText(music.getTitle());
        holder.singerName.setText(music.getAuthor());

        Cursor query = mSqLiteDatabase.query("music", new String[]{"mid"},
                "mid = ?", new String[]{ music.getMid()},
                null, null, null, null);
        int flag = query.getCount();
        query.close();
        if(flag == 0) {
            holder.addLike.setImageResource(R.drawable.like);
        } else {
            holder.addLike.setImageResource(R.drawable.liking);
        }
        holder.addLike.setOnClickListener(e -> {
            if (flag != 0) {
                mSqLiteDatabase.delete("music", "mid = ?", new String[] {music.getMid()});
                holder.addLike.setImageResource(R.drawable.like);
            } else {
                ContentValues values = new ContentValues() ;
                values.put("mid", music.getMid());
                values.put("author", music.getAuthor());
                values.put("title", music.getTitle());
                values.put("lrc", music.getLrc());
                values.put("avatar",music.getAvatar());
                mSqLiteDatabase.insert("music", null, values);
                holder.addLike.setImageResource(R.drawable.liking);
            }
        });



        holder.itemView.setOnClickListener(e -> {
            mContext.playService.playNow(dataList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
