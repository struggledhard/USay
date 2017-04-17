package com.skh.universitysay.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.skh.universitysay.R;
import com.skh.universitysay.other.ImageDealDialog;
import com.skh.universitysay.utils.FileUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by SKH on 2017/4/13 0013.
 * 单张图片显示Fragment
 */

public class ImageDetailFragment extends Fragment{

    @BindView(R.id.detail_image)
    ImageView mImageView;

    public static final int REQUEST_CODE = 0X110;
    public static final String DIALOG = "dialog";

    private String mImageUrl;
    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putSerializable("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? (String) getArguments().getSerializable("url") : null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        mAttacher = new PhotoViewAttacher(mImageView);
        // 轻点照片，返回消失
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finishAfterTransition();
            }

            @Override
            public void onOutsidePhotoTap() {
                getActivity().finishAfterTransition();
            }
        });
        // 图片的长按事件监听
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ImageDealDialog imageDealDialog = new ImageDealDialog();
                imageDealDialog.setTargetFragment(ImageDetailFragment.this, REQUEST_CODE);
                imageDealDialog.show(getFragmentManager(), DIALOG);
                return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(ImageDetailFragment.this)
                .load(mImageUrl)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
        mAttacher.update();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            String dialogItemStr = data.getStringExtra(ImageDealDialog.RESPONSE);
            switch (dialogItemStr) {
                case "保存到手机":
                    Bitmap bitmap = mAttacher.getVisibleRectangleBitmap();
                    FileUtil.saveBitmapToJpg(getActivity(), bitmap);
                    Toast.makeText(getActivity(), "照片已保存（手机相册 -> USay）", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
