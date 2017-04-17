package com.skh.universitysay.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SKH on 2017/4/6 0006.
 * 文件操作工具类
 */

public class FileUtil {
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/USay/";

    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param c        context
     * @param fileName 文件名称
     * @param bitmap   图片
     * @return string
     */
    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/USay/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

    /**
     * 删除指定文件夹路径下的文件和子目录文件
     *
     * @param pathStr
     */
    public static void deletePathFiles(String pathStr) {
        File file = new File(pathStr);
        if (!file.exists() || !file.isDirectory()) {
            return;
        }

        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            // 判断是否以路径分隔符结尾
            if (pathStr.endsWith(File.separator)) {
                temp = new File(pathStr + tempList[i]);
            } else {
                temp = new File(pathStr + File.separator + tempList[i]);
            }

            if (temp.isFile()) {
                temp.delete();
            }

            if (temp.isDirectory()) {
                deletePathFiles(pathStr + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(pathStr + "/" + tempList[i]);// 再删除空文件夹
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹路径及名称 如c:/fqf
     */
    public static void delFolder(String folderPath) {
        try {
            deletePathFiles(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存到指定目录，但能立即更新到系统相册中（红米2）
     *
     * @param context    上下文环境
     * @param faceBitmap 位图资源
     * @return 保存图片的路径
     */
    public static String saveBitmapToJpg(Context context, Bitmap faceBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.i("LOG", "SD *****>> SD卡不存在");
        } else {
            Log.i("LOG", "SD *****>> SD卡 存在");
        }

        // 创建图片保存目录
        File faceImgDir = new File(Environment.getExternalStorageDirectory(), "USay");
        if (!faceImgDir.exists()) {
            faceImgDir.mkdir();
        }

        // 以系统时间命名文件
        String faceImgName = "usay-" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File file = new File(faceImgDir, faceImgName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            faceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 保存后要扫描一下文件，及时更新到系统目录（一定要加绝对路径，这样才能更新）
        MediaScannerConnection.scanFile(context,
                new String[]{Environment.getExternalStorageDirectory() +
                        File.separator + "USay" + File.separator + faceImgName},
                null, null);

        return (Environment.getExternalStorageDirectory() + File.separator +
                "USay" + File.separator + faceImgName);
    }
}
