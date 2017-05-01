package com.skh.universitysay.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SKH on 2017/4/30 0030.
 * 收藏帮助类
 */

public class DBHelper extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "create table favorite ("
            + "id integer primary key autoincrement,"
            + "new_id text,"
            + "author text,"
            + "url text,"
            + "image text,"
            + "title text)";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
