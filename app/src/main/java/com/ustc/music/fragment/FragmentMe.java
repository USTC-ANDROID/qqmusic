package com.ustc.music.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ustc.music.R;
import com.ustc.music.adapter.LikeListViewAdapter;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.db.MyDatabaseHelper;
import com.ustc.music.entity.Music;
import com.ustc.music.url.DataUrl;
import com.ustc.music.view.LazyBaseFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentMe extends LazyBaseFragment {

    ListView listView;
    LikeListViewAdapter adapter;
    List<Music> datas = new ArrayList<>();
    Button bt;
    MyDatabaseHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;

    @Override
    protected void findViewById(View rootView) {
        bt = rootView.findViewById(R.id.play_all);
        bt.setOnClickListener(e -> {
            for (int i = 0; i < datas.size(); i++) {
                Music music = datas.get(i);
                ((BaseActivity)getActivity()).playService.add(music);
            }
        });
        listView = rootView.findViewById(R.id.like_list);
        adapter = new LikeListViewAdapter((BaseActivity)getActivity(), datas);
        listView.setAdapter(adapter);
        mDatabaseHelper = new MyDatabaseHelper(getActivity());
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("musicsource", datas.get(position).getAvatar());
                ((BaseActivity)getActivity()).playService.playNow(datas.get(position));
            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_menu_3;
    }

    @Override
    protected void lazyLoad() {
//        这里进行网络请求
        datas.clear();
        Cursor cursor = mSqLiteDatabase.query("music", new String[]{"mid", "author", "avatar", "lrc", "title"},
                null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            Music music = new Music(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            datas.add(music);
        }
        adapter.notifyDataSetChanged();
    }
}
