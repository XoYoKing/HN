package com.example.admin.hn.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.hn.base.MyApplication;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Random;

/**
 * 图片工具类
 *
 * @author duantao
 */
public class ToolPicture {
    /**
     * 上下文
     **/
    private static final Context context = MyApplication.getContext();
    /**
     * 截取应用程序界面（去除状态栏）
     * activity 界面Activity
     * @return Bitmap对象
     */
    /***
     * 屏幕显示材质
     **/
    private static final DisplayMetrics mDisplayMetrics = new DisplayMetrics();


    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        Bitmap bitmap2 = Bitmap.createBitmap(
                bitmap,
                0,
                statusBarHeight,
                getDisplayMetrics().widthPixels,
                getDisplayMetrics().heightPixels - statusBarHeight
        );
        view.destroyDrawingCache();
        return bitmap2;
    }


    /**
     * 获取系统显示材质
     ***/
    public static DisplayMetrics getDisplayMetrics() {
        WindowManager windowMgr = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE
        );
        windowMgr.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics;
    }


    /**
     * 截取应用程序界面
     *
     * @param activity 界面Activity
     *
     * @return Bitmap对象
     */
    public static Bitmap takeFullScreenShot(Activity activity) {

        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);

        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();

        View view = activity.getWindow().getDecorView();

        Bitmap bmp2 = Bitmap.createBitmap(480, 800, Config.ARGB_8888);

        //view.draw(new Canvas(bmp2));

        //bmp就是截取的图片了，可通过bmp.compress(CompressFormat.PNG, 100, new FileOutputStream(file));把图片保存为文件。

        //1、得到状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        System.out.println("状态栏高度：" + statusBarHeight);

        //2、得到标题栏高度
        int wintop = activity.getWindow().findViewById(android.R.id.content).getTop();
        int titleBarHeight = wintop - statusBarHeight;
        System.out.println("标题栏高度:" + titleBarHeight);

        //		//把两个bitmap合到一起
        //		Bitmap bmpall=Biatmap.createBitmap(width,height,Config.ARGB_8888);
        //		Canvas canvas=new Canvas(bmpall);
        //		canvas.drawBitmap(bmp1,x,y,paint);
        //		canvas.drawBitmap(bmp2,x,y,paint);

        return bmp;
    }

    /**
     * 生成二维矩阵
     *
     * @param str
     *
     * @return
     * @throws WriterException
     */
    public static Bitmap create2DCode(String str) throws WriterException {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致无法识别
        BitMatrix matrix = new MultiFormatWriter().encode(
                str, BarcodeFormat.QR_CODE, 400, 400
        );
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(
                width, height, Config.ARGB_8888
        );
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     *
     * @return degree 旋转的角度
     * @throws IOException
     */
    public static int gainPictureDegree(String path) throws Exception {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
            );
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
            }
        } catch (Exception e) {
            throw (e);
        }

        return degree;
    }


    /**
     * 旋转图片
     *
     * @param angle  角度
     * @param bitmap 源bitmap
     *
     * @return Bitmap 旋转角度之后的bitmap
     */
    public static Bitmap rotaingBitmap(int angle, Bitmap bitmap) {
        //旋转图片 动作   
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);
        //重新构建Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true
        );
        return resizedBitmap;
    }


    /**
     * Drawable转成Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565
            );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(
                    0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()
            );
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    /**
     * 从资源文件中获取图片
     *
     * @param context    上下文
     * @param drawableId 资源文件id
     */
    public static Bitmap gainBitmap(Context context, int drawableId) {
        Bitmap bmp = BitmapFactory.decodeResource(
                context.getResources(), drawableId
        );
        return bmp;
    }


    /**
     * 灰白图片（去色）
     *
     * @param bitmap 需要灰度的图片
     *
     * @return 去色之后的图片
     */
    public static Bitmap toBlack(Bitmap bitmap) {
        Bitmap resultBMP = Bitmap.createBitmap(
                bitmap.getWidth(), bitmap.getHeight(), Config.RGB_565
        );
        Canvas c = new Canvas(resultBMP);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return resultBMP;
    }


    /**
     * 将bitmap转成 byte数组
     */
    public static byte[] toBtyeArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    /**
     * 将byte数组转成 bitmap
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


    /**
     * 将Bitmap转换成指定大小
     *
     * @param bitmap 需要改变大小的图片
     * @param width  宽
     * @param height 高
     */
    public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }


    /**
     * 在图片右下角添加水印
     *
     * @param srcBMP  原图
     * @param markBMP 水印图片
     *
     * @return 合成水印后的图片
     */
    public static Bitmap composeWatermark(Bitmap srcBMP, Bitmap markBMP) {
        if (srcBMP == null) {
            return null;
        }

        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newb = Bitmap.createBitmap(
                srcBMP.getWidth(), srcBMP.getHeight(), Config.ARGB_8888
        );
        Canvas cv = new Canvas(newb);
        // 在 0，0坐标开始画入原图
        cv.drawBitmap(srcBMP, 0, 0, null);
        // 在原图的右下角画入水印
        cv.drawBitmap(
                markBMP,
                srcBMP.getWidth() - markBMP.getWidth() + 5,
                srcBMP.getHeight() - markBMP.getHeight() + 5,
                null
        );
        // 保存
        cv.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        cv.restore();

        return newb;
    }


    /**
     * 将图片转成指定弧度（角度）的图片
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     *
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(
                bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888
        );
        //根据图片创建画布
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    /**
     * 缩放图片
     *
     * @param bmp  需要缩放的图片源
     * @param newW 需要缩放成的图片宽度
     * @param newH 需要缩放成的图片高度
     *
     * @return 缩放后的图片
     */
    public static Bitmap zoom(Bitmap bmp, int newW, int newH) {

        // 获得图片的宽高
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        // 计算缩放比例
        float scaleWidth = ((float) newW) / width;
        float scaleHeight = ((float) newH) / height;

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(
                bmp, 0, 0, width, height, matrix, true
        );

        return newbm;
    }


    /**
     * 获得倒影的图片
     *
     * @param bitmap 原始图片
     *
     * @return 带倒影的图片
     */
    public static Bitmap makeReflectionImage(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(
                bitmap, 0, height / 2, width, height / 2, matrix, false
        );
        Bitmap bitmapWithReflection = Bitmap.createBitmap(
                width, (height + height / 2), Config.ARGB_8888
        );

        Paint deafalutPaint = new Paint();
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(
                0,
                bitmap.getHeight(),
                0,
                bitmapWithReflection.getHeight() + reflectionGap,
                0x70ffffff,
                0x00ffffff,
                TileMode.CLAMP
        );
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(
                0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint
        );

        return bitmapWithReflection;
    }


    /**
     * 获取验证码图片
     *
     * @param width  验证码宽度
     * @param height 验证码高度
     *
     * @return 验证码Bitmap对象
     */
    public synchronized static Bitmap makeValidateCode(int width, int height) {
        return ValidateCodeGenerator.createBitmap(width, height);
    }


    /**
     * 获取验证码值
     *
     * @return 验证码字符串
     */
    public synchronized static String gainValidateCodeValue() {
        return ValidateCodeGenerator.getCode();
    }


    /**
     * 随机生成验证码内部类
     */
    final static class ValidateCodeGenerator {
        private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        //default settings
        private static final int DEFAULT_CODE_LENGTH = 4;
        private static final int DEFAULT_FONT_SIZE = 20;
        private static final int DEFAULT_LINE_NUMBER = 3;
        private static final int BASE_PADDING_LEFT = 5, RANGE_PADDING_LEFT = 10, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 10;
        private static final int DEFAULT_WIDTH = 60, DEFAULT_HEIGHT = 30;

        //variables
        private static String value;
        private static int padding_left, padding_top;
        private static Random random = new Random();


        public static Bitmap createBitmap(int width, int height) {
            padding_left = 0;
            //创建画布
            Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas c = new Canvas(bp);

            //随机生成验证码字符
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
                buffer.append(CHARS[random.nextInt(CHARS.length)]);
            }
            value = buffer.toString();

            //设置颜色
            c.drawColor(Color.WHITE);

            //设置画笔大小
            Paint paint = new Paint();
            paint.setTextSize(DEFAULT_FONT_SIZE);
            for (int i = 0; i < value.length(); i++) {
                //随机样式
                randomTextStyle(paint);
                padding_left += BASE_PADDING_LEFT + random.nextInt(RANGE_PADDING_LEFT);
                padding_top = BASE_PADDING_TOP + random.nextInt(RANGE_PADDING_TOP);
                c.drawText(
                        value.charAt(i) + "", padding_left, padding_top, paint
                );
            }
            for (int i = 0; i < DEFAULT_LINE_NUMBER; i++) {
                drawLine(c, paint);
            }
            //保存
            c.save(Canvas.ALL_SAVE_FLAG);
            c.restore();

            return bp;
        }


        public static String getCode() {
            return value;
        }


        private static void randomTextStyle(Paint paint) {
            int color = randomColor(1);
            paint.setColor(color);
            paint.setFakeBoldText(random.nextBoolean());//true为粗体，false为非粗体
            float skewX = random.nextInt(11) / 10;
            skewX = random.nextBoolean() ? skewX : -skewX;
            paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
            paint.setUnderlineText(true); //true为下划线，false为非下划线
            paint.setStrikeThruText(true); //true为删除线，false为非删除线
        }


        private static void drawLine(Canvas canvas, Paint paint) {
            int color = randomColor(1);
            int startX = random.nextInt(DEFAULT_WIDTH);
            int startY = random.nextInt(DEFAULT_HEIGHT);
            int stopX = random.nextInt(DEFAULT_WIDTH);
            int stopY = random.nextInt(DEFAULT_HEIGHT);
            paint.setStrokeWidth(1);
            paint.setColor(color);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }


        private static int randomColor(int rate) {
            int red = random.nextInt(256) / rate;
            int green = random.nextInt(256) / rate;
            int blue = random.nextInt(256) / rate;
            return Color.rgb(red, green, blue);
        }
    }


    /**
     * 设置bitmap
     */
    public static void setCropImg(Intent picdata, ImageView imageView) {
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = bundle.getParcelable("data");
            imageView.setImageBitmap(mBitmap);
            saveBitmap(
                    Environment.getExternalStorageDirectory() + "/crop_" +
                            System.currentTimeMillis() + ".png", mBitmap
            );
        }
    }


    /**
     * 保存裁剪后的bitmap
     */
    public static void saveBitmap(String fileName, Bitmap mBitmap) {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     * <p/>
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
     * <p/>
     * B.本地路径:url="file://mnt/sdcard/photo/image.png";
     * <p/>
     * C.支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @param url
     *
     * @return
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024 * 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024 * 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[1024 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }



}
