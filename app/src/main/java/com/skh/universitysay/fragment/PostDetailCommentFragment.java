package com.skh.universitysay.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skh.universitysay.R;
import com.skh.universitysay.adapter.CommentAdapter;
import com.skh.universitysay.bean.Comment;
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.bean.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by SKH on 2017/4/19 0019.
 * 评论详情界面
 */

public class PostDetailCommentFragment extends Fragment {

    @BindView(R.id.post_detail_comment)
    RecyclerView mPostDetailCommentRecycler;
    @BindView(R.id.post_detail_refresh)
    SwipeRefreshLayout mPostDetailRefresh;
    @BindView(R.id.post_detail_comment_content)
    EditText mCommentContent;
    @BindView(R.id.post_detail_comment_send)
    Button mCommentSend;
    @BindView(R.id.post_detail_comment_no)
    TextView mCommentNo;
//    @BindView(R.id.linear)
//    LinearLayout mLinear;

    private Post mPost;
    private MyUser mMyUser;
    private CommentAdapter mAdapter;

    public static PostDetailCommentFragment newInstance(Post post) {
        PostDetailCommentFragment fragment = new PostDetailCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("post", post);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPost = (Post) getArguments().getSerializable("post");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pd_comment_fragment, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new CommentAdapter(getActivity());
        mMyUser = BmobUser.getCurrentUser(MyUser.class);
        initCommentRecycler();
        initRefresh();
        commentClickEvent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoRefresh();
    }

    private void initCommentRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mPostDetailCommentRecycler.setLayoutManager(layoutManager);
        mPostDetailCommentRecycler.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mPostDetailCommentRecycler.addItemDecoration(dividerItemDecoration);
    }

    private void autoRefresh() {
        mPostDetailRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPostDetailRefresh.setRefreshing(true);
                getCommentData();
            }
        }, 100);
    }

    private void initRefresh() {
        mPostDetailRefresh.setColorSchemeColors(Color.RED, Color.BLACK, Color.YELLOW);
        mPostDetailRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCommentData();
            }
        });
    }

    private void getCommentData() {
        BmobQuery<Comment> query = new BmobQuery<>();
        Post post = new Post();
        post.setObjectId(mPost.getObjectId());
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.order("createdAt");
        // 希望查询到评论发布者的具体信息 用下内部查询
        // 这里稍复杂些 查询评论发布者的信息 和 帖子作者的信息
        // 参见include的并列对象查询和内嵌对象的查询
        query.include("user,post.author");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (mPostDetailRefresh != null && mPostDetailRefresh.isRefreshing()) {
                    mPostDetailRefresh.setRefreshing(false);
                    if (e == null) {
                        if (list.isEmpty()) {
                            mCommentNo.setVisibility(View.VISIBLE);
                            mPostDetailCommentRecycler.setVisibility(View.GONE);
                            mCommentNo.setText(R.string.comment_no);
                        } else {
                            mCommentNo.setVisibility(View.GONE);
                            mPostDetailCommentRecycler.setVisibility(View.VISIBLE);
                            mAdapter.setCommentData(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /*
     * 发表评论
     */
    private void commentClickEvent() {
        mCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMyUser != null) {
                    if (!TextUtils.isEmpty(mCommentContent.getText().toString())) {
                        Post post = new Post();
                        post.setObjectId(mPost.getObjectId());
                        Comment comment = new Comment();
                        comment.setUser(mMyUser);
                        comment.setPost(post);
                        comment.setContent(mCommentContent.getText().toString());
                        comment.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity(), "评论成功,请刷新!",
                                            Toast.LENGTH_SHORT).show();
                                    mCommentContent.setText("");
                                    // 隐藏键盘
                                    InputMethodManager manager =
                                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    manager.hideSoftInputFromWindow(mCommentContent.getWindowToken(), 0);
                                    Post post1 = new Post();
                                    post1.increment("commentNum", 1);
                                    post1.update(mPost.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e != null) {
                                                Toast.makeText(getActivity(), "评论失败!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "请输内容!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "请先登录!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
