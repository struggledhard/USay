package com.skh.universitysay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skh.universitysay.R;
import com.skh.universitysay.bean.Post;
import com.skh.universitysay.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/4/12 0012.
 * 社区数据设配器
 */

public class SheQuAdapter extends RecyclerView.Adapter<SheQuAdapter.SheQuViewHolder>{

    private List<Post> mPostList;
    private Context mContext;

    public SheQuAdapter(Context context) {
        this.mContext = context;
        mPostList = new ArrayList<>();
    }

    @Override
    public SheQuAdapter.SheQuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_post, parent, false);
        return new SheQuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SheQuAdapter.SheQuViewHolder holder, int position) {
        Post post = mPostList.get(position);

        if (post.getAuthor() != null) {
            holder.mPostUser.setText(post.getAuthor().getUserNickName());
        } else {
            holder.mPostUser.setText("佚名");
        }
        if (post.getAuthor() != null) {
            Glide.with(mContext).load(post.getAuthor().getHeadImgUrl())
                    .centerCrop().override(56, 56).crossFade().into(holder.mPostImage);
        } else {
            holder.mPostImage.setImageResource(R.mipmap.user_logo_default);
        }
        if (!TextUtils.isEmpty(post.getContent())) {
            holder.mPostContent.setVisibility(View.VISIBLE);
            holder.mPostContent.setText(post.getContent());
        } else {
            holder.mPostContent.setVisibility(View.GONE);
        }
        if (!post.getImageUrlList().isEmpty()) {
            holder.mPostGrid.setVisibility(View.VISIBLE);
            holder.mPostGrid.setAdapter(new PostGridAdapter(post.getImageUrlList(), mContext));
        } else {
            holder.mPostGrid.setVisibility(View.GONE);
        }

        String createAt = post.getCreatedAt();
        String strDate = TimeUtil.getDescriptionTimeFromTimestamp(TimeUtil.stringToLong(createAt,
                TimeUtil.FORMAT_DATE_TIME_SECOND));
        holder.mPostDate.setText(strDate);
        holder.mPostLikeNum.setText(String.valueOf(post.getLikeNum()));
        holder.mPostCommitNum.setText(String.valueOf(post.getCommentNum()));
    }

    @Override
    public int getItemCount() {
        if (mPostList != null) {
            return mPostList.size();
        }
        return 0;
    }

    class SheQuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.post_image)
        ImageView mPostImage;  // 头像
        @BindView(R.id.ll_right)
        LinearLayout mLayout;
        @BindView(R.id.post_user)
        TextView mPostUser;  // 发布人
        @BindView(R.id.post_content)
        TextView mPostContent;   // 发布内容
        @BindView(R.id.post_grid)
        GridView mPostGrid;  // 发表的图片
        @BindView(R.id.post_date)
        TextView mPostDate;   // 发表时间
        @BindView(R.id.post_like_num)
        TextView mPostLikeNum;   // 点赞数
        @BindView(R.id.post_like)
        ImageButton mPostLike;  // dianzan
        @BindView(R.id.post_commit_num)
        TextView mPostCommitNum;   // 评论数
        @BindView(R.id.post_commit)
        ImageButton mPostCommit;  // 评论

        public SheQuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setPostData(List<Post> postList) {
        mPostList.clear();
        this.mPostList = postList;
        notifyDataSetChanged();
    }

    public void setPostMoreData(List<Post> postList) {
        mPostList.addAll(postList);
        notifyDataSetChanged();
    }
}