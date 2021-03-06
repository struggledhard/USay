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
import com.skh.universitysay.bean.WebItemBean;
import com.skh.universitysay.ui.WebViewFenLeiActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/3/28 0028.
 * web适配器
 */

public class TabWebAdapter extends RecyclerView.Adapter<TabWebAdapter.TabWebViewHolder>{

    private List<WebItemBean> mWebItemBeen;
    private Context mContext;

    public TabWebAdapter(Context context) {
        this.mContext = context;
        mWebItemBeen = new ArrayList<>();
    }

    @Override
    public TabWebAdapter.TabWebViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_web_item, parent, false);
        return new TabWebViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TabWebAdapter.TabWebViewHolder holder, int position) {
        WebItemBean itemBean = mWebItemBeen.get(position);

        final String newid = itemBean.get_id();
        final String title = itemBean.getDesc();
        final String author = itemBean.getWho();
        String time = itemBean.getPublishedAt();
        final List<String> images = itemBean.getImages();
        final String url = itemBean.getUrl();

        holder.mWebTitle.setText(title);
        holder.mWebTime.setText(time);

        if (author != null) {
            holder.mWebAuthor.setText(author);
        } else {
            holder.mWebAuthor.setText("佚名");
        }

        if (itemBean.getImages() != null) {
            String img = itemBean.getImages().get(0);
            Glide.with(mContext).load(img)
                    .override(100, 124)
                    .centerCrop()
                    .into(holder.mWebImg);
        } else {
            Glide.with(mContext).load(R.drawable.banner01)
                    .override(100, 124)
                    .centerCrop()
                    .into(holder.mWebImg);
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
        if (mWebItemBeen != null) {
            return mWebItemBeen.size();
        }
        return 0;
    }

    class TabWebViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tab_web_img)
        ImageView mWebImg;
        @BindView(R.id.tab_web_author)
        TextView mWebAuthor;
        @BindView(R.id.tab_web_title)
        TextView mWebTitle;
        @BindView(R.id.tab_web_time)
        TextView mWebTime;

        public TabWebViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setWebItemBeen(List<WebItemBean> webItemBeen) {
        mWebItemBeen.clear();
        this.mWebItemBeen = webItemBeen;
        notifyDataSetChanged();
    }

    public void setMoreWebItemBean(List<WebItemBean> webItemBean) {
        mWebItemBeen.addAll(webItemBean);
        notifyDataSetChanged();
    }
}
