package com.skh.universitysay.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.skh.universitysay.fragment.ImageDetailFragment;

/**
 * Created by SKH on 2017/4/13 0013.
 * 图片浏览时
 * 长按弹出对话框
 */

public class ImageDealDialog extends DialogFragment{
    private String[] mImageDealVals = new String[]{"保存到手机"};
    public static final String RESPONSE = "response";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(mImageDealVals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(which);
            }
        });

        return builder.create();
    }

    private void setResult(int which) {
        // 判断是否设置了targetFragment
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(RESPONSE, mImageDealVals[which]);
        getTargetFragment().onActivityResult(ImageDetailFragment.REQUEST_CODE, Activity.RESULT_OK, intent);
    }
}
