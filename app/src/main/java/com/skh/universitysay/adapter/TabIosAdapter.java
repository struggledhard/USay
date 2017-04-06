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
import com.skh.universitysay.bean.IosItemBean;
import com.skh.universitysay.ui.WebViewFenLeiActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/3/27 0027.
 * iOS适配器
 */

public class TabIosAdapter extends RecyclerView.Adapter<TabIosAdapter.TabIosViewHolder>{

    private List<IosItemBean> mIosItemBeen;
    private Context mContext;

    public TabIosAdapter(Context context) {
        this.mContext = context;
        mIosItemBeen = new ArrayList<>();
    }

    @Override
    public TabIosAdapter.TabIosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_ios_item, parent, false);
        return new TabIosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TabIosAdapter.TabIosViewHolder holder, int position) {
        IosItemBean itemBean = mIosItemBeen.get(position);

        String title = itemBean.getDesc();
        final String author = itemBean.getWho();
        String time = itemBean.getPublishedAt();
        final List<String> images = itemBean.getImages();
        final String url = itemBean.getUrl();

        holder.mIosTitle.setText(title);
        holder.mIosTime.setText(time);

        if (author != null) {
            holder.mIosAuthor.setText(author);
        } else {
            holder.mIosAuthor.setText("佚名");
        }

        if (itemBean.getImages() != null) {
            String img = itemBean.getImages().get(0);
            Glide.with(mContext).load(img)
                    .override(100, 124)
                    .centerCrop()
                    .into(holder.mIosImg);
        } else {
            Glide.with(mContext).load(R.drawable.banner01)
                    .override(100, 124)
                    .centerCrop()
                    .into(holder.mIosImg);
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
        if (mIosItemBeen != null) {
            return mIosItemBeen.size();
        }
        return 0;
    }

    class TabIosViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tab_ios_img)
        ImageView mIosImg;
        @BindView(R.id.tab_ios_title)
        TextView mIosTitle;
        @BindView(R.id.tab_ios_author)
        TextView mIosAuthor;
        @BindView(R.id.tab_ios_time)
        TextView mIosTime;

        public TabIosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setIosItemBeen(List<IosItemBean> iosItemBeen) {
        mIosItemBeen.clear();
        this.mIosItemBeen = iosItemBeen;
        notifyDataSetChanged();
    }

    public void setMoreIosItemBean(List<IosItemBean> iosItemBean) {
        mIosItemBeen.addAll(iosItemBean);
        notifyDataSetChanged();
    }
}
