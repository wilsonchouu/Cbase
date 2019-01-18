package com.cbase.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * @author : zhouyx
 * @date : 2017/9/13
 * @description : 文件工具
 */
public class FileUtils {

    /**
     * 读取文件为byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] read(String filePath) {
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len;
            while ((len = in.read(buffer, 0, bufSize)) != -1) {
                bao.write(buffer, 0, len);
            }
            in.close();
            bao.close();
            return bao.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存文件
     *
     * @param filePath
     * @param data
     */
    public static void save(String filePath, byte[] data) {
        File file = new File(filePath);
        try {
            FileOutputStream osw;
            if (file.exists()) {
                osw = new FileOutputStream(file, true);
                osw.write(data);
                osw.flush();
            } else {
                file.createNewFile();
                osw = new FileOutputStream(file);
                osw.write(data);
            }
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件
     *
     * @param originalFile
     * @param targetFile
     */
    public static void copy(File originalFile, File targetFile) {
        if (originalFile == null) {
            return;
        }
        if (!originalFile.exists()) {
            return;
        }
        if (targetFile == null) {
            return;
        }
        try {
            InputStream in = new FileInputStream(originalFile);
            OutputStream out = new FileOutputStream(targetFile);
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len;
            while ((len = in.read(buffer, 0, bufSize)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件
     *
     * @param originalPath
     * @param targetPath
     */
    public static void copy(String originalPath, String targetPath) {
        if (originalPath == null) {
            return;
        }
        if (targetPath == null) {
            return;
        }
        copy(new File(originalPath), new File(targetPath));
    }

    /**
     * 删除单个文件
     *
     * @param path 要删除的文件路径
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文
     *
     * @param path 要删除目录的文件路径
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String path) {
        // 如果path不以文件分隔符结尾，自动添加文件分隔符
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        // 如果dir对应文件不存在或不是一个目录，则返回
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteFile(file.getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目件
            else {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        return dirFile.delete();
    }

    /**
     * 通知系统更新图片库
     *
     * @param context
     * @param filePath
     */
    public static void noticeMediaScan(Context context, String filePath) {
        filePath = filePath + "";
        if ("".equals(filePath) || "null".equals(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (file.length() == 0) {
            return;
        }
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
        context.sendBroadcast(scanIntent);
    }

    /**
     * 转换文件大小
     *
     * @param fileSize
     * @return
     */
    public static String formatFileSize(long fileSize) {
        DecimalFormat format = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileSize < 1024) {
            fileSizeString = format.format(fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = format.format(fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = format.format(fileSize / 1048576) + "M";
        } else {
            fileSizeString = format.format(fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 读取Assets文件夹文件
     *
     * @param resource
     * @param path
     * @return
     */
    public static String readAssetsFile(Resources resource, String path) {
        if (resource == null) {
            return "";
        }
        if (path == null) {
            return "";
        }
        try {
            AssetManager assets = resource.getAssets();
            InputStream inputStream = assets.open(path);
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            int ret = inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取SD卡文件
     *
     * @param path
     * @return
     */
    public static String readSdCardFile(String path) {
        if (path == null) {
            return "";
        }
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        try {
            InputStream inputStream = new FileInputStream(file);
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            int ret = inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
