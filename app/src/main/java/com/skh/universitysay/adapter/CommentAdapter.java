package com.skh.universitysay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skh.universitysay.R;
import com.skh.universitysay.bean.Comment;
import com.skh.universitysay.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/4/20 0020.
 * 评论内容适配器
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    private List<Comment> mCommentList;
    private Context mContext;

    public CommentAdapter(Context context) {
        this.mContext = context;
        mCommentList = new ArrayList<>();
    }

    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_list_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);

        if (comment.getUser() != null) {
            if (comment.getUser().getHeadImgUrl() != null) {
                Glide.with(mContext).load(comment.getUser().getHeadImgUrl())
                        .centerCrop().override(132, 132).crossFade().into(holder.mCommentHead);
            } else {
                holder.mCommentHead.setImageResource(R.mipmap.user_logo_default);
            }
        }
        if (comment.getUser() != null) {
            holder.mCommentUser.setText(comment.getUser().getUserNickName());
        } else {
            holder.mCommentUser.setText("佚名");
        }
        if (!TextUtils.isEmpty(comment.getContent())) {
            holder.mCommentContent.setText(comment.getContent());
        }
        String createAt = comment.getCreatedAt();
        String strDate = TimeUtil.getDescriptionTimeFromTimestamp(TimeUtil.stringToLong(createAt,
                TimeUtil.FORMAT_DATE_TIME_SECOND));
        holder.mCommentDate.setText(strDate);
    }

    @Override
    public int getItemCount() {
        if (mCommentList != null) {
            return mCommentList.size();
        }
        return 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment_head)
        ImageView mCommentHead;
        @BindView(R.id.comment_user)
        TextView mCommentUser;
        @BindView(R.id.comment_content)
        TextView mCommentContent;
        @BindView(R.id.comment_date)
        TextView mCommentDate;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setCommentData(List<Comment> commentList) {
        mCommentList.clear();
        this.mCommentList = commentList;
        notifyDataSetChanged();
    }
}
