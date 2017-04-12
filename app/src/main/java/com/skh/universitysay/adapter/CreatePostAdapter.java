package com.skh.universitysay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.litao.android.lib.entity.PhotoEntry;
import com.skh.universitysay.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/4/11 0011.
 * 创建帖子的适配器
 * 用于显示从相册或拍照照片
 */

public class CreatePostAdapter extends RecyclerView.Adapter<CreatePostAdapter.CreatePostViewHolder>{

    private List<PhotoEntry> list = new ArrayList<>();

    private Context mContext;

    private LayoutInflater mInflater;

    private OnItmeClickListener mlistener;

    public  interface OnItmeClickListener{
        void onItemClicked(int position);

    }

    public CreatePostAdapter(Context mContext) {
        this.mContext = mContext;
        mlistener = (OnItmeClickListener) mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list.add(createAddEntry());
    }

    public void reloadList(List<PhotoEntry> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            list.add(createAddEntry());
        } else {
            list.clear();
        }
        notifyDataSetChanged();

    }

    public void appendList(List<PhotoEntry> data) {
        if (data != null) {
            list.addAll(list.size()-1,data);
        } else {
            list.clear();
        }
        notifyDataSetChanged();

    }


    public void appendPhoto(PhotoEntry entry) {
        if (entry != null) {
            list.add(list.size()-1,entry);
        }
        notifyDataSetChanged();
    }

    public List<PhotoEntry> getData(){
        return list.subList(0,list.size()-1);
    }

    public PhotoEntry getEntry(int position) {
        return list.get(position);
    }

    private PhotoEntry createAddEntry(){
        return new PhotoEntry();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public CreatePostAdapter.CreatePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_photo, parent, false);
        return new CreatePostViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(CreatePostAdapter.CreatePostViewHolder holder, int position) {
        if (position==list.size()-1){
            holder.mImageView.setImageResource(R.drawable.icon_addpic_unfocused);
        }else {
            PhotoEntry entry = list.get(position);
            Glide.with(mContext)
                    .load(new File(entry.getPath()))
                    .centerCrop()
                    .placeholder(com.litao.android.lib.R.mipmap.default_image)
                    .into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CreatePostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.image)
        ImageView mImageView;

        private int position;

        public CreatePostViewHolder(View itemView, int position) {
            super(itemView);
            this.position = position;
            ButterKnife.bind(this, itemView);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mlistener.onItemClicked(position);
        }
    }
}
