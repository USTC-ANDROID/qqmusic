package com.ustc.music.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper  extends SQLiteOpenHelper {

    public static final String CREATE_MUSIC ="create table music ("
            +"mid text primary key, "
            +"author text,"
            +"avatar text,"
            +"lrc text, "
            +"title text)";

    private Context mContext ;

    public MyDatabaseHelper(Context context) {
        super(context, "musics.db", null, 3);
        this.mContext = context ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(CREATE_MUSIC);
        db.execSQL(CREATE_MUSIC);
        //创建成功后提示
        Toast.makeText(mContext, "数据库和数据库表创建成功",Toast.LENGTH_SHORT).show() ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists music");
        onCreate(db);
    }

}