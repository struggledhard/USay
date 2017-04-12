package com.skh.universitysay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.skh.universitysay.R;

import java.util.List;

/**
 * Created by SKH on 2017/4/12 0012.
 * 发布图片适配器
 * GridView
 */

public class PostGridAdapter extends BaseAdapter{

    private List<String> mList;
    private LayoutInflater mLayoutInflater;

    public PostGridAdapter(List<String> list, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyGridViewHolder myGridViewHolder;
        if (view == null) {
            myGridViewHolder = new MyGridViewHolder();
            view = mLayoutInflater.inflate(R.layout.post_img_item, viewGroup, false);
            myGridViewHolder.imageView = (ImageView) view.findViewById(R.id.post_grid_item);
            view.setTag(myGridViewHolder);
        } else {
            myGridViewHolder = (MyGridViewHolder) view.getTag();
        }

        String url = mList.get(i);
        Glide.with(viewGroup.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(myGridViewHolder.imageView);

        return view;
    }

    private static class MyGridViewHolder {
        ImageView imageView;
    }
}
