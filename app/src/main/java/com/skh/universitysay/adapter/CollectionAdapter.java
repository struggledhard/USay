package com.skh.universitysay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skh.universitysay.R;
import com.skh.universitysay.bean.Favorite;
import com.skh.universitysay.ui.WebViewGeRenActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/5/1 0001.
 * 我的收藏适配器
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>{

    private Context mContext;
    private List<Favorite> mFavoriteList;

    public CollectionAdapter(Context context, List<Favorite> favoriteList) {
        this.mContext = context;
        this.mFavoriteList = favoriteList;
    }

    @Override
    public CollectionAdapter.CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.collection_list_item, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectionAdapter.CollectionViewHolder holder, int position) {
        Favorite favorite = mFavoriteList.get(position);

        if (favorite.getAuthor() != null) {
            holder.mCollectionAuthor.setText(favorite.getAuthor());
        } else {
            holder.mCollectionAuthor.setText("佚名");
        }

        holder.mCollectionTitle.setText(favorite.getTitle());
        String image = favorite.getImage();
        if (!image.isEmpty()) {
            Glide.with(mContext).load(image)
                    .centerCrop().crossFade()
                    .into(holder.mCollectionImg);
        } else {
            Glide.with(mContext).load(R.drawable.banner01)
                    .centerCrop().crossFade()
                    .into(holder.mCollectionImg);
        }

        final String url = favorite.getUrl();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewGeRenActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("str_url", url);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mFavoriteList != null) {
            return mFavoriteList.size();
        }
        return 0;
    }

    class CollectionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.collection_img)
        ImageView mCollectionImg;
        @BindView(R.id.collection_author)
        TextView mCollectionAuthor;
        @BindView(R.id.collection_title)
        TextView mCollectionTitle;

        public CollectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
