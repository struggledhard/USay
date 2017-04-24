package com.skh.universitysay.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.skh.universitysay.R;
import com.skh.universitysay.bean.MyUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/4/23 0023.
 * 点赞适配器
 */

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.LikesViewHolder>{

    private List<MyUser> mUserList;
    private Context mContext;

    public LikesAdapter(Context context) {
        this.mContext = context;
        mUserList = new ArrayList<>();
    }

    @Override
    public LikesAdapter.LikesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.likes_list_item, parent, false);
        return new LikesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LikesAdapter.LikesViewHolder holder, int position) {
        MyUser myUser = mUserList.get(position);

        if (myUser != null) {
            if (myUser.getHeadImgUrl() != null) {
                Glide.with(mContext).load(myUser.getHeadImgUrl())
                        .asBitmap().centerCrop()
                        .into(new SimpleTarget<Bitmap>(116, 166) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RoundedBitmapDrawable bitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                bitmapDrawable.setCircular(true);
                                holder.mLikesImage.setImageDrawable(bitmapDrawable);
                            }
                        });
            } else {
                holder.mLikesImage.setImageResource(R.drawable.ic_islogin_circle_gray_24dp);
            }

            if (!TextUtils.isEmpty(myUser.getUserNickName())) {
                holder.mLikesUser.setText(myUser.getUserNickName());
            } else {
                holder.mLikesUser.setText("佚名");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mUserList != null) {
            return mUserList.size();
        }
        return 0;
    }

    class LikesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.likes_image)
        ImageView mLikesImage;
        @BindView(R.id.likes_user)
        TextView mLikesUser;

        public LikesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setLikesUserData(List<MyUser> myUserList) {
        this.mUserList = myUserList;
        notifyDataSetChanged();
    }
}
