package com.skh.universitysay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skh.universitysay.R;
import com.skh.universitysay.adapter.LikesAdapter;
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.bean.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by SKH on 2017/4/19 0019.
 * 喜欢详情列表界面
 */

public class PostDetailLikesFragment extends Fragment {

    @BindView(R.id.post_detail_likes)
    RecyclerView mPostDetailLikesRecycler;
    @BindView(R.id.post_detail_likes_no)
    TextView mLikesNo;

    private Post mPost;
    private LikesAdapter mLikesAdapter;

    public static PostDetailLikesFragment newInstance(Post post) {
        final PostDetailLikesFragment fragment = new PostDetailLikesFragment();
        final Bundle bundle = new Bundle();
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
        View view = inflater.inflate(R.layout.pd_likes_fragment, container, false);
        ButterKnife.bind(this, view);

        mLikesAdapter = new LikesAdapter(getActivity());
        initLikesRecycler();
        getLikesData();

        return view;
    }

    private void initLikesRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mPostDetailLikesRecycler.setLayoutManager(layoutManager);
        mPostDetailLikesRecycler.setAdapter(mLikesAdapter);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mPostDetailLikesRecycler.addItemDecoration(dividerItemDecoration);
    }

    private void getLikesData() {
        BmobQuery<MyUser> query = new BmobQuery<>();
        Post post = new Post();
        post.setObjectId(mPost.getObjectId());
        query.addWhereRelatedTo("likes", new BmobPointer(post));
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e == null) {
                    if (list.isEmpty()) {
                        mPostDetailLikesRecycler.setVisibility(View.GONE);
                        mLikesNo.setVisibility(View.VISIBLE);
                        mLikesNo.setText(R.string.likes_no);
                    } else {
                        mPostDetailLikesRecycler.setVisibility(View.VISIBLE);
                        mLikesNo.setVisibility(View.GONE);
                        mLikesAdapter.setLikesUserData(list);
                        mLikesAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
