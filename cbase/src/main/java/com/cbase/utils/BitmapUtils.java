package com.cbase.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author : zhouyx
 * Date   : 2017/9/12
 * Description : Bitmap工具
 */
public class BitmapUtils {

    /**
     * 缩放图片
     *
     * @param bitmap
     * @param sx
     * @param sy
     * @return
     */
    public static Bitmap scale(Bitmap bitmap, float sx, float sy) {
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 缩放图片
     *
     * @param bitmap
     * @param sxy
     * @return
     */
    public static Bitmap scale(Bitmap bitmap, float sxy) {
        return scale(bitmap, sxy, sxy);
    }

    /**
     * 保存图片到文件
     *
     * @param filePath
     * @param bitmap
     */
    public static boolean save(String filePath, Bitmap bitmap) {
        if (filePath == null) {
            return false;
        }
        if (bitmap == null) {
            return false;
        }
        File file = new File(filePath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存图片到文件
     *
     * @param filePath
     * @param data
     * @return
     */
    public static boolean save(String filePath, byte[] data) {
        if (filePath == null) {
            return false;
        }
        if (data == null) {
            return false;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return save(filePath, bitmap);
    }

    /**
     * 从asset目录读取图片
     *
     * @param resource
     * @param fileName
     * @return
     */
    public static Bitmap decodeAssets(Resources resource, String fileName) {
        if (resource == null) {
            return null;
        }
        if (fileName == null) {
            return null;
        }
        AssetManager assets = resource.getAssets();
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            InputStream inputStream = assets.open(fileName);
            Bitmap image = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片读取
     *
     * @param fd
     * @return
     */
    public static Bitmap decodeFileDescriptor(FileDescriptor fd) {
        if (fd == null) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片读取
     *
     * @param filePath
     * @return
     */
    public static Bitmap decodeFile(String filePath) {
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取视频缩略图
     *
     * @param filePath
     * @return
     */
    public static byte[] getThumbnailByte(String filePath, int width, int height) {
        if (filePath == null) {
            return null;
        }
        try {
            // 获取视频的缩略图
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND);
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            bao.flush();
            bao.close();
            return bao.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        if (context == null) {
            return null;
        }
        if (uri == null) {
            return null;
        }
        try {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) {
                return null;
            }
            cursor.moveToFirst();
            String str = cursor.getString(1);
            cursor.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 裁剪图片为圆角
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getHeight(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels * bitmap.getWidth() / 128;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (output != bitmap) {
            bitmap.recycle();
        }
        return output;
    }

    /**
     * 从resource目录读取图片
     *
     * @param resource
     * @param resourceId
     * @return
     */
    public static Bitmap decodeResource(Resources resource, int resourceId) {
        if (resource == null) {
            return null;
        }
        if (resourceId == 0) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeResource(resource, resourceId, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从resource目录读取Drawable
     *
     * @param resource
     * @param resourceId
     * @return
     */
    public static Drawable getDrawable(Resources resource, int resourceId) {
        if (resource == null) {
            return null;
        }
        if (resourceId == 0) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resource.getDrawable(resourceId, null);
        } else {
            return resource.getDrawable(resourceId);
        }
    }

    /**
     * Bitmap转为byte[]
     *
     * @param bitmap
     * @return
     */
    public static byte[] toByte(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            bao.flush();
            bao.close();
            return bao.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Bitmap转Base64
     *
     * @param bitmap
     * @return
     */
    public static String toBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            bao.flush();
            bao.close();
            byte[] bytes = bao.toByteArray();
            return Base64.encodeToString(bytes, Base64.URL_SAFE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Base64转Bitmap
     *
     * @param base64
     * @return
     */
    public static Bitmap toBitmap(String base64) {
        if (base64 == null) {
            return null;
        }
        try {
            byte[] bytes = Base64.decode(base64, Base64.URL_SAFE);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotate(Bitmap bitmap, int degree) {
        if (bitmap == null) {
            return null;
        }
        if (degree == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 读取图片属性：旋转的角度 三星手机特别注意
     *
     * @param filePath 图片绝对路径
     * @return degree旋转的角度
     */
    public static int getImageDegree(String filePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 读取文件为byte[]
     *
     * @param filePath
     * @return
     */
    private static byte[] read(String filePath) {
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
     * 文件转Base64
     *
     * @param filePath
     * @return
     */
    public static String toBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        byte[] bytes = read(filePath);
        if (bytes == null) {
            return null;
        }
        return Base64.encodeToString(bytes, Base64.URL_SAFE);
    }

    /**
     * 根据宽高计算缩放
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            float totalPixels = width * height;
            float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * 按照指定的宽高成比例的读取缩小的图片
     *
     * @param filePath
     * @param width
     * @param height
     * @return
     */
    public static Bitmap decodeFileInSampleSize(String filePath, int width, int height) {
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            FileDescriptor fileDescriptor = inputStream.getFD();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            // 计算缩放
            int simpleSize = calculateInSampleSize(options, width, height);
            options.inJustDecodeBounds = false;
            options.inSampleSize = simpleSize;
            // 正式读取图片
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据宽高计算缩放
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateMatrix(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        float widthRatio = reqWidth / (options.outWidth * 1.0f);
        float heightRatio = reqHeight / (options.outHeight * 1.0f);
        return (int) (widthRatio > heightRatio ? heightRatio : widthRatio);
    }

    /**
     * 精确缩放图片到某个尺寸
     *
     * @param filePath
     * @param width
     * @param height
     * @return
     */
    public static Bitmap decodeFileMatrix(String filePath, int width, int height) {
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            FileDescriptor fileDescriptor = inputStream.getFD();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            // 计算缩放
            int scale = calculateMatrix(options, width, height);
            options.inJustDecodeBounds = false;
            // 正式读取图片
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            if (scale <= 1) {
                // 取得想要缩放的matrix参数
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            }
            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
