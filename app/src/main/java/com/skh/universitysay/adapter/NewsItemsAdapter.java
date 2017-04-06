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

import com.skh.universitysay.R;
import com.skh.universitysay.bean.NewsItems;
import com.skh.universitysay.ui.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/3/12 0012.
 * 博客头条适配器
 */

public class NewsItemsAdapter extends RecyclerView.Adapter<NewsItemsAdapter.NewsViewHolder>{
    private List<NewsItems> mItemses;
    private Context mContext;

    public NewsItemsAdapter(Context context) {
        this.mContext = context;
        mItemses = new ArrayList<>();

    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_recycler_item, parent, false);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItems newsItems = mItemses.get(position);

        final String authorId = newsItems.getAuthorId();
        final String imgHeadLink = newsItems.getHeadImgLink();
        final String contentUrl = newsItems.getContentUrl();

        holder.title.setText(newsItems.getTitle());
        holder.authorId.setText(newsItems.getAuthorId());
        holder.date.setText(newsItems.getDate());
        holder.viewPerson.setText(newsItems.getViewPerson());

        String newsTypeStr = newsItems.getNewsType();
        if (newsTypeStr != null) {
            holder.newsType.setText(newsItems.getNewsType());
        } else {
            holder.imgNewsType.setVisibility(View.GONE);
            holder.newsType.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("authorId", authorId);
                bundle.putString("imgHeadLink", imgHeadLink);
                bundle.putString("contentUrl", contentUrl);
                intent.putExtra("data", bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mItemses != null) {
            return mItemses.size();
        }
        return 0;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author_id)
        TextView authorId;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.view_person)
        TextView viewPerson;
        @BindView(R.id.news_type)
        TextView newsType;
        @BindView(R.id.img_news_type)
        ImageView imgNewsType;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setNewsItemsList(List<NewsItems> newsItemsList) {
        mItemses.clear();
        this.mItemses = newsItemsList;
        notifyDataSetChanged();
    }

    public void setMoreNewsItemsList(List<NewsItems> newsItemsList) {
        mItemses.addAll(newsItemsList);
        notifyDataSetChanged();
    }
}
