package com.skh.universitysay.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skh.universitysay.bean.Favorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SKH on 2017/4/30 0030.
 * 收藏数据库
 */

public class FavoriteDB {
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;
    private static FavoriteDB sFavoriteDB;

    private FavoriteDB(Context context) {
        mDBHelper = new DBHelper(context, "USay.db", null, 1);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public synchronized static FavoriteDB getInstance(Context context) {
        if (sFavoriteDB == null) {
            sFavoriteDB = new FavoriteDB(context);
        }
        return sFavoriteDB;
    }

    public void saveFavorite(Favorite favorite) {
        if (favorite != null) {
            ContentValues values = new ContentValues();
            values.put("new_id", favorite.getNewId());
            values.put("author", favorite.getAuthor());
            values.put("url", favorite.getUrl());
            values.put("image", favorite.getImage());
            values.put("title", favorite.getTitle());
            mDatabase.insert("favorite", null, values);
        }
    }

    public void deleteFavorite(Favorite favorite) {
        if (favorite != null) {
            String string = favorite.getNewId();
            mDatabase.delete("favorite", "new_id = ?", new String[] {string});
        }
    }

    public List<Favorite> findFavorite() {
        List<Favorite> favoriteList = new ArrayList<>();
        Cursor cursor = mDatabase.query("favorite", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象
                Favorite favorite = new Favorite();
                favorite.setNewId(cursor.getString(1));
                favorite.setAuthor(cursor.getString(2));
                favorite.setUrl(cursor.getString(3));
                favorite.setImage(cursor.getString(4));
                favorite.setTitle(cursor.getString(5));
                favoriteList.add(favorite);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favoriteList;
    }

    public boolean isFavorite(Favorite favorite) {
        String string = favorite.getNewId();
        Cursor cursor = mDatabase.query("favorite", new String[] {"new_id"}, "new_id = ?", new String[] {string}, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }
}
