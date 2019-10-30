package com.ustc.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ustc.music.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchSongRecyclerViewAdapter extends RecyclerView.Adapter<SearchSongRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private SearchSongClickListener mListener;
    private List<Map<String, String>> dataList;

    public interface SearchSongClickListener{
        public void clickListener(View v);
    }

    public void addAllData(List<Map<String, String>> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public SearchSongRecyclerViewAdapter(Context context, List<Map<String, String>> dataList,
                                         SearchSongClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.dataList = dataList;
    }

    @Override
    public void onClick(View view) {
        mListener.clickListener(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView songTitle;
        public TextView singerName;

        public ViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
            singerName = itemView.findViewById(R.id.searchSingerName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.songTitle.setText(dataList.get(position).get("songName"));
        holder.songTitle.setTag(position);
        holder.songTitle.setOnClickListener(this);
        holder.singerName.setText(dataList.get(position).get("singerName"));
        holder.singerName.setOnClickListener(this);
        holder.singerName.setTag(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
