package com.skh.universitysay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.skh.universitysay.R;
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.bean.Post;
import com.skh.universitysay.ui.GridImgDetailActivity;
import com.skh.universitysay.ui.PostDetailActivity;
import com.skh.universitysay.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by SKH on 2017/4/12 0012.
 * 社区数据设配器
 */

public class SheQuAdapter extends RecyclerView.Adapter<SheQuAdapter.SheQuViewHolder>{

    private List<Post> mPostList;
    private Context mContext;
    private MyUser mMyUser;

    public SheQuAdapter(Context context) {
        this.mContext = context;
        mPostList = new ArrayList<>();
        mMyUser = BmobUser.getCurrentUser(MyUser.class);
    }

    @Override
    public SheQuAdapter.SheQuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_post, parent, false);
        return new SheQuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SheQuAdapter.SheQuViewHolder holder, int position) {
        final Post post = mPostList.get(position);

        if (post.getAuthor() != null) {
            holder.mPostUser.setText(post.getAuthor().getUserNickName());
        } else {
            holder.mPostUser.setText("佚名");
        }
        if (post.getAuthor() != null) {
            if (post.getAuthor().getHeadImgUrl() != null) {
                Glide.with(mContext).load(post.getAuthor().getHeadImgUrl())
                        .centerCrop().override(132, 132).crossFade().into(holder.mPostImage);
            } else {
                holder.mPostImage.setImageResource(R.mipmap.user_logo_default);
            }
        } else {
            holder.mPostImage.setImageResource(R.mipmap.user_logo_default);
        }
        if (!TextUtils.isEmpty(post.getContent())) {
            holder.mPostContent.setVisibility(View.VISIBLE);
            holder.mPostContent.setText(post.getContent());
        } else {
            holder.mPostContent.setVisibility(View.GONE);
        }
        final List<String> userImgsArrayList = post.getImageUrlList();
        if (!userImgsArrayList.isEmpty()) {
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

        holder.mPostGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imageBrower(i, userImgsArrayList);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("post", post);
                mContext.startActivity(intent);
            }
        });

        holder.mPostLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMyUser == null) {
                    Toast.makeText(mContext, "请先登录!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!post.getLiked()) {
                        Post p = new Post();
                        p.setObjectId(post.getObjectId());
                        p.increment("likeNum", 1);
                        p.setLiked(true);
                        // 把当前用户添加到帖子的likes字段
                        BmobRelation relation = new BmobRelation();
                        // 把用户加到多对多关系中
                        relation.add(mMyUser);
                        // 多对多关联指向post的likes字段
                        p.setLikes(relation);
                        p.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    holder.mPostLikeNum.setText(String.valueOf(post.getLikeNum()));
                                } else {
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(mContext, "已经赞过了,亲", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

    /**
     * 打开图片查看器
     *
     * @param position          图片在九宫格中的位置
     * @param userImgsArrayList 图片列表
     */
    private void imageBrower(int position, List<String> userImgsArrayList) {
        Intent intent = new Intent(mContext, GridImgDetailActivity.class);
        intent.putStringArrayListExtra(GridImgDetailActivity.EXTRA_IMAGE_URLS,
                (ArrayList<String>) userImgsArrayList);
        intent.putExtra(GridImgDetailActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }
}
