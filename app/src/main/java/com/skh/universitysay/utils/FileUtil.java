package com.skh.universitysay.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

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

}
