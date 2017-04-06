package com.skh.universitysay.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skh.universitysay.R;
import com.skh.universitysay.bean.AndroidItemBean;
import com.skh.universitysay.ui.WebViewFenLeiActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/3/22 0022.
 * Android数据适配器
 */

public class TabAndroidAdapter extends RecyclerView.Adapter<TabAndroidAdapter.TabAndroidViewHolder>{

    private List<AndroidItemBean> mAndroidItemBeen;
    private Context mContext;

    public TabAndroidAdapter(Context context) {
        this.mContext = context;
        mAndroidItemBeen = new ArrayList<>();
    }

    @Override
    public TabAndroidAdapter.TabAndroidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_android_item, parent, false);
        return new TabAndroidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TabAndroidAdapter.TabAndroidViewHolder holder, int position) {
        AndroidItemBean itemBean = mAndroidItemBeen.get(position);

        String title = itemBean.getDesc();
        final String author = itemBean.getWho();
        String time = itemBean.getPublishedAt();
        final List<String> images = itemBean.getImages();
        final String url = itemBean.getUrl();

        holder.mAndroidTitle.setText(title);
        holder.mAndroidTime.setText(time);

        if (author != null) {
            holder.mAndroidAuthor.setText(author);
        } else {
            holder.mAndroidAuthor.setText("佚名");
        }

        if (itemBean.getImages() != null) {
            String img = itemBean.getImages().get(0);
            Glide.with(mContext).load(img)
                    .override(100, 124)
                    .centerCrop()
                    .into(holder.mAndroidImg);
        } else {
            Glide.with(mContext).load(R.drawable.banner01)
                    .override(100, 124)
                    .centerCrop()
                    .into(holder.mAndroidImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, WebViewFenLeiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("author", author);
                bundle.putString("url", url);
                bundle.putStringArrayList("images", (ArrayList<String>) images);
                intent.putExtra("extra", bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mAndroidItemBeen != null) {
            return mAndroidItemBeen.size();
        }
        return 0;
    }

    class TabAndroidViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tab_android_img)
        ImageView mAndroidImg;
        @BindView(R.id.tab_android_title)
        TextView mAndroidTitle;
        @BindView(R.id.tab_android_author)
        TextView mAndroidAuthor;
        @BindView(R.id.tab_android_time)
        TextView mAndroidTime;

        public TabAndroidViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setAndroidItemBeen(List<AndroidItemBean> androidItemBeen) {
        mAndroidItemBeen.clear();
        this.mAndroidItemBeen = androidItemBeen;
        notifyDataSetChanged();
    }

    public void setMoreAndroidItemBean(List<AndroidItemBean> androidItemBean) {
        mAndroidItemBeen.addAll(androidItemBean);
        notifyDataSetChanged();
    }
}
