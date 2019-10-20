package com.ustc.music.adapter;

import android.content.Context;
import android.net.Uri;
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

public class SearchAlbumRecyclerViewAdapter extends RecyclerView.Adapter<SearchAlbumRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,String>> dataList = new ArrayList<>();

    public void addAllData(List<Map<String,String>> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public SearchAlbumRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView albumPic;
        public TextView albumName;
        public TextView singerName;

        public ViewHolder(View itemView) {
            super(itemView);
            albumPic = itemView.findViewById(R.id.albumPic);
            albumName = itemView.findViewById(R.id.albumName);
            singerName = itemView.findViewById(R.id.searchAlbumSingerName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_album_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.albumPic.setImageURL(dataList.get(position).get("albumPic"));
        holder.albumName.setText(dataList.get(position).get("albumName"));
        holder.singerName.setText(dataList.get(position).get("singerName"));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
