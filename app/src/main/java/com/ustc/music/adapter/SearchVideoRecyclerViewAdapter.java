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

public class SearchVideoRecyclerViewAdapter extends RecyclerView.Adapter<SearchVideoRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,String>> dataList = new ArrayList<>();

    public void addAllData(List<Map<String,String>> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.dataList.clear();
    }

    public SearchVideoRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView vedioPic;
        public TextView vedioName;
        public TextView singerName;

        public ViewHolder(View itemView) {
            super(itemView);
            vedioPic = itemView.findViewById(R.id.vedioPic);
            vedioName = itemView.findViewById(R.id.vedioName);
            singerName = itemView.findViewById(R.id.searchVedioSingerName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_vedio_recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.vedioPic.setImageURL(dataList.get(position).get("vedioPic"),dataList.get(position).get("playCnt"));
        holder.vedioName.setText(dataList.get(position).get("vedioName"));
        holder.singerName.setText(dataList.get(position).get("singerName"));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
