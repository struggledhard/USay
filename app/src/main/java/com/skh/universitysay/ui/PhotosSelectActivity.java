package com.skh.universitysay.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.litao.android.lib.BaseGalleryActivity;
import com.litao.android.lib.Configuration;
import com.litao.android.lib.entity.PhotoEntry;
import com.skh.universitysay.R;
import com.skh.universitysay.other.EventEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/4/11 0011.
 * 选择照片
 */

public class PhotosSelectActivity extends BaseGalleryActivity implements View.OnClickListener{

    @BindView(R.id.photo_select_toolbar)
    Toolbar mPhotoSelectToolbar;
    @BindView(R.id.album)
    TextView mTextViewOpenAlbum;
    @BindView(R.id.selected_count)
    TextView mTextViewSelectedCount;
    @BindView(R.id.send_photos)
    TextView mTextViewSend;

    private List<PhotoEntry> mSelectedPhotos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        attachFragment(R.id.gallery_root);

        initToolbar();

        mTextViewOpenAlbum.setOnClickListener(this);
        mTextViewSelectedCount.setOnClickListener(this);
        mTextViewSend.setOnClickListener(this);
    }

    private void initToolbar() {
        mPhotoSelectToolbar.setTitle("选择照片");
        setSupportActionBar(mPhotoSelectToolbar);
        mPhotoSelectToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mPhotoSelectToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.album:
                openAlbum();
                break;
            case R.id.send_photos:
                sendPhotos();
                break;
        }
    }

    @Override
    public Configuration getConfiguration() {
        //default configuration
        Configuration cfg=new Configuration.Builder()
                .hasCamera(true)
                .hasShade(true)
                .hasPreview(true)
                .setSpaceSize(4)
                .setPhotoMaxWidth(120)
                .setCheckBoxColor(0xFF3F51B5)
                .setDialogHeight(Configuration.DIALOG_HALF)
                .setDialogMode(Configuration.DIALOG_GRID)
                .setMaximum(9)
                .setTip(null)
                .setAblumsTitle(null)
                .build();
        return cfg;
    }

    @Override
    public List<PhotoEntry> getSelectPhotos() {
        return mSelectedPhotos;
    }

    @Override
    public void onSelectedCountChanged(int count) {
        mTextViewSelectedCount.setVisibility(count>0?View.VISIBLE:View.INVISIBLE);
        mTextViewSelectedCount.setText(String.valueOf(count));
    }

    @Override
    public void onAlbumChanged(String name) {
        getSupportActionBar().setSubtitle(name);
    }

    @Override
    public void onTakePhoto(PhotoEntry entry) {
        EventBus.getDefault().post(entry);
        finish();
    }

    @Override
    public void onChoosePhotos(List<PhotoEntry> entries) {
        EventBus.getDefault().post(new EventEntry(entries,EventEntry.RECEIVED_PHOTOS_ID));
        finish();
    }

    @Override
    public void onPhotoClick(PhotoEntry entry) {

    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void photosMessageEvent(EventEntry entry){
        if (entry.id == EventEntry.SELECTED_PHOTOS_ID) {
            mSelectedPhotos = entry.photos;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
