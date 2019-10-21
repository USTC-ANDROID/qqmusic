package com.ustc.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ustc.music.R;
import com.ustc.music.view.NetworkImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchSongListRecyclerViewAdapter extends RecyclerView.Adapter<SearchSongListRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,String>> dataList = new ArrayList<>();

    public void addAllData(List<Map<String,String>> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public SearchSongListRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView songListPic;
        public TextView songListName;
        public TextView listenNum;
        public TextView songCnt;
        public TextView songListCreatorName;

        public ViewHolder(View itemView) {
            super(itemView);
            songListPic = itemView.findViewById(R.id.songListPic);
            songListName = itemView.findViewById(R.id.songListName);
            listenNum = itemView.findViewById(R.id.songListListenNum);
            songCnt = itemView.findViewById(R.id.songListCnt);
            songListCreatorName = itemView.findViewById(R.id.songListCreatorName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_list_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.songListPic.setImageURL(dataList.get(position).get("songListPic"));
        holder.songListName.setText(dataList.get(position).get("songListName"));
        holder.listenNum.setText(dataList.get(position).get("listenNum"));
        holder.songCnt.setText(dataList.get(position).get("songCnt"));
        holder.songListCreatorName.setText(dataList.get(position).get("creatorName"));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
