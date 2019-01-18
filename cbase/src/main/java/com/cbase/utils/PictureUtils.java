package com.cbase.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * @author : zhouyx
 * @date : 2017/10/9
 * @description : 系统相机/图片裁剪工具
 */
public class PictureUtils {

    /**
     * 打开相机
     *
     * @param activity
     * @param code
     * @param path
     */
    public static void openCamera(Activity activity, int code, String path) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        activity.startActivityForResult(intent, code);
    }

    /**
     * 打开相机
     *
     * @param fragment
     * @param code
     * @param path
     */
    public static void openCamera(Fragment fragment, int code, String path) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        fragment.startActivityForResult(intent, code);
    }

    /**
     * 打开相册选择照片
     *
     * @param activity
     * @param code
     */
    public static void selectPicture(Activity activity, int code) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, code);
    }

    /**
     * 打开相册选择照片
     *
     * @param fragment
     * @param code
     */
    public static void selectPicture(Fragment fragment, int code) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        fragment.startActivityForResult(intent, code);
    }

    /**
     * 对图片进行剪裁，通过Intent来调用系统自带的图片剪裁API
     *
     * @param activity
     * @param code
     * @param inputPath
     * @param outputPath
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     */
    public static void cropPhoto(Activity activity, int code, String inputPath, String outputPath, int aspectX, int aspectY, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(inputPath)), "image/*");
        intent.putExtra("crop", "true");
        /* aspectX aspectY 是裁剪后图片的宽高比 */
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        /* outputX outputY 是裁剪图片宽 */
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // 关闭人脸
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputPath)));
        activity.startActivityForResult(intent, code);
    }

    /**
     * 对图片进行剪裁，通过Intent来调用系统自带的图片剪裁API
     *
     * @param activity
     * @param code
     * @param inputPath
     * @param outputPath
     */
    public static void cropPhoto(Activity activity, int code, String inputPath, String outputPath) {
        cropPhoto(activity, code, inputPath, outputPath, 5, 5, 256, 256);
    }

    /**
     * 对图片进行剪裁，通过Intent来调用系统自带的图片剪裁API
     *
     * @param fragment
     * @param code
     * @param inputPath
     * @param outputPath
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     */
    public static void cropPhoto(Fragment fragment, int code, String inputPath, String outputPath, int aspectX, int aspectY, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(inputPath)), "image/*");
        intent.putExtra("crop", "true");
        /* aspectX aspectY 是裁剪后图片的宽高比 */
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        /* outputX outputY 是裁剪图片宽 */
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // 关闭人脸
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputPath)));
        fragment.startActivityForResult(intent, code);
    }

    /**
     * 对图片进行剪裁，通过Intent来调用系统自带的图片剪裁API
     *
     * @param fragment
     * @param code
     * @param inputPath
     * @param outputPath
     */
    public static void cropPhoto(Fragment fragment, int code, String inputPath, String outputPath) {
        cropPhoto(fragment, code, inputPath, outputPath, 5, 5, 256, 256);
    }

}
