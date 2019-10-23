package com.ustc.music.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.music.R;
import com.ustc.music.adapter.SingersListViewAdapter;
import com.ustc.music.adapter.SingersQueryAdapter;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.entity.Singer;
import com.ustc.music.util.RequestUtil;
import com.ustc.music.view.HeadView;
import com.ustc.music.view.HorizontalListView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SingersActivity extends BaseActivity {

    private String[] addrs = new String[] {"全部", "内地", "港台", "欧美", "日本", "韩国", "其他"};
    private String[] sexs = new String[] {"男", "女", "组合"};

    private char indexBase = 'A';

    private ListView singerIndexs;
    private ListView singerList;


    private HorizontalListView addrListView;
    private HorizontalListView sexListView;
    private HeadView headView;

    private ArrayAdapter<String> indexsAdapter;
    private SingersQueryAdapter addrListViewAdapter;
    private SingersQueryAdapter sexListViewAdapter;
    private SingersListViewAdapter singerArrayAdapter;
    private List<Singer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(savedInstanceState, R.layout.activity_singers);
        initEvent();
    }

    @Override
    protected void initView() {
        addrListView = findViewById(R.id.addr);
        sexListView = findViewById(R.id.sex);
        singerIndexs = findViewById(R.id.singers_index);
        singerList = findViewById(R.id.singers);
        headView = findViewById(R.id.head);

    }

    @Override
    protected void initData() {
        RequestUtil.get("http://106.54.230.9:8080/singers/list", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                List<Singer> singers = com.alibaba.fastjson.JSONObject.parseArray(string, Singer.class);
                list.clear();
                list.addAll(singers);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        singerArrayAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }

    private void initEvent() {
        headView.getLeftImgView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        singerIndexs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(Singer singer : list) {
//                    Log.v("singerActivity", singer.getZ().charAt(0) + "==" + (char)(position + 'a') + " " + (singer.getZ().charAt(0) == (char)(position + 'a')));
                    if(singer.getZ().charAt(0) == (char)(position + 'a')) {
                        Toast.makeText(SingersActivity.this, "click", Toast.LENGTH_LONG).show();
                        int i = list.indexOf(singer);
                        singerList.smoothScrollToPosition(i);

//                        Toast.makeText(SingersActivity.this, "click" + i + "  " + (char)(position + 'A'), Toast.LENGTH_LONG).show();

                        break;
                    }

                }
            }
        });
        singerIndexs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SingersActivity.this, "select", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initAdapter() {
        singerArrayAdapter = new SingersListViewAdapter(this, list);
        singerList.setAdapter(singerArrayAdapter);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add(String.valueOf((char)(indexBase + i)));
            Log.v("singersactivity", String.valueOf(indexBase + i));
        }
        indexsAdapter = new ArrayAdapter<>(this, R.layout.string_layout, R.id.text,list);
        singerIndexs.setAdapter(indexsAdapter);
        addrListViewAdapter = new SingersQueryAdapter(addrs, this, 0);
        sexListViewAdapter = new SingersQueryAdapter(sexs, this, 0);
        addrListView.setAdapter(addrListViewAdapter);
        sexListView.setAdapter(sexListViewAdapter);
        addrListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int childCount = parent.getChildCount();
                int selected = addrListViewAdapter.getSelected();
                if(selected < childCount && selected >= 0) {
                    View childView = parent.getChildAt(selected);
                    childView.findViewById(R.id.query_item_bg).setBackgroundResource((R.drawable.layout_white_circle_border_radius_50));
                    ((TextView)childView.findViewById(R.id.query_item)).setTextColor(getResources().getColor(R.color.Black, null));
                }
                if(position < childCount && position >= 0) {
                    View childView = parent.getChildAt(position);
                    childView.findViewById(R.id.query_item_bg).setBackgroundResource((R.drawable.layout_blue_circle_border_radius_50));
                    ((TextView)childView.findViewById(R.id.query_item)).setTextColor(getResources().getColor(R.color.white, null));
                }
                addrListViewAdapter.setSelected(position);
            }
        });

        sexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int childCount = parent.getChildCount();
                int selected = sexListViewAdapter.getSelected();
                if(selected < childCount && selected >= 0) {
                    View childView = parent.getChildAt(selected);
                    childView.findViewById(R.id.query_item_bg).setBackgroundResource((R.drawable.layout_white_circle_border_radius_50));
                    ((TextView)childView.findViewById(R.id.query_item)).setTextColor(getResources().getColor(R.color.Black, null));
                }
                if(position < childCount && position >= 0) {
                    View childView = parent.getChildAt(position);
                    childView.findViewById(R.id.query_item_bg).setBackgroundResource((R.drawable.layout_blue_circle_border_radius_50));
                    ((TextView)childView.findViewById(R.id.query_item)).setTextColor(getResources().getColor(R.color.white, null));
                }
                sexListViewAdapter.setSelected(position);

            }
        });
    }
}

