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
import com.skh.universitysay.bean.ExpandItemBean;
import com.skh.universitysay.ui.WebViewFenLeiActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/3/28 0028.
 * expand适配器
 */

public class TabExpandAdapter extends RecyclerView.Adapter<TabExpandAdapter.TabExpandViewHolder>{

    private List<ExpandItemBean> mExpandItemBeen;
    private Context mContext;

    public TabExpandAdapter(Context context) {
        this.mContext = context;
        mExpandItemBeen = new ArrayList<>();
    }

    @Override
    public TabExpandAdapter.TabExpandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_expand_item, parent, false);
        return new TabExpandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TabExpandAdapter.TabExpandViewHolder holder, int position) {
        ExpandItemBean itemBean = mExpandItemBeen.get(position);

        final String newid = itemBean.get_id();
        final String title = itemBean.getDesc();
        final String author = itemBean.getWho();
        String time = itemBean.getPublishedAt();
        final List<String> images = itemBean.getImages();
        final String url = itemBean.getUrl();

        holder.mExpandTitle.setText(title);
        holder.mExpandTime.setText(time);

        if (author != null) {
            holder.mExpandAuthor.setText(author);
        } else {
            holder.mExpandAuthor.setText("佚名");
        }

        if (itemBean.getImages() != null) {
            String img = itemBean.getImages().get(0);
            Glide.with(mContext).load(img)
                    .override(100, 124)
                    .centerCrop()
                    .into(holder.mExpandImg);
        } else {
            Glide.with(mContext).load(R.drawable.banner01)
                    .override(100, 124)
                    .centerCrop()
                    .into(holder.mExpandImg);
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
                bundle.putString("new_id", newid);
                bundle.putString("title", title);
                intent.putExtra("extra", bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mExpandItemBeen != null) {
            return mExpandItemBeen.size();
        }
        return 0;
    }

    class TabExpandViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tab_expand_img)
        ImageView mExpandImg;
        @BindView(R.id.tab_expand_author)
        TextView mExpandAuthor;
        @BindView(R.id.tab_expand_title)
        TextView mExpandTitle;
        @BindView(R.id.tab_expand_time)
        TextView mExpandTime;

        public TabExpandViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setExpandItemBeen(List<ExpandItemBean> expandItemBeen) {
        mExpandItemBeen.clear();
        this.mExpandItemBeen = expandItemBeen;
        notifyDataSetChanged();
    }

    public void setMoreExpandItemBean(List<ExpandItemBean> expandItemBeen) {
        mExpandItemBeen.addAll(expandItemBeen);
        notifyDataSetChanged();
    }
}
