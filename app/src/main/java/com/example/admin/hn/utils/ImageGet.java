package com.example.admin.hn.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.admin.hn.R;
import com.example.admin.hn.base.HNApplication;

/**
 * Created by geek on 2016/8/12.
 * 一个html格式的数据在textView中加载图片
 * 对<img></>标签的处理
 */
public class ImageGet implements Html.ImageGetter {
    private Context context;
    private TextView textView;
    private float totalRatio = 2f;//缩放比例
    private boolean isShow = true;

    public ImageGet(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }

    @Override
    public Drawable getDrawable(String paramString) {
        final URLDrawable urlDrawable = new URLDrawable(context);
        Glide.with(HNApplication.getContext())
                .load(paramString)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        setImageView(bitmap, urlDrawable);
                    }
                });
        return urlDrawable;
    }

    private void setImageView(Bitmap bitmap, URLDrawable urlDrawable) {
        int windowWidth = ToolAppUtils.getWindowWith(context);
        int width = bitmap.getWidth();
//        AbLogUtil.e("width", width + "");
//        AbLogUtil.e("windowWidth", windowWidth + "");
        if (width < windowWidth / 2) {
            //当图片的尺寸小于屏幕的一半，把图片放大2倍
            Bitmap bitmap1 = AbImageUtil.scaleBitmap(bitmap, totalRatio);
            if (bitmap1 != null) {
                bitmap = bitmap1;
                width = bitmap.getWidth();
            }
        } else if (width > windowWidth) {
            //计算缩放的比例
//            float i = Math.abs(1 - Float.parseFloat("" + (width - windowWidth)) / windowWidth);
            if (windowWidth >= 480 && windowWidth < 540) {
                totalRatio = 0.35f;
            } else if (windowWidth >= 540 && windowWidth < 720) {
                totalRatio = 0.4f;
            } else if (windowWidth >= 720 && windowWidth < 1080) {
                totalRatio = 0.56f;
            } else if (windowWidth >= 1080 && windowWidth < 1200) {
                totalRatio = 0.8f;
            } else {
                totalRatio = 1f;
            }
            Bitmap bitmap1 = AbImageUtil.scaleBitmap(bitmap, totalRatio);
            if (bitmap1 != null) {
                bitmap = bitmap1;
                width = bitmap.getWidth();
            }
        }
        int height = bitmap.getHeight();
//        AbLogUtil.e("scaleWidth", width + "");
        int i = ((windowWidth - width) / 2) - 40;
        Rect bounds = new Rect(i, 0, width + i, height);
        urlDrawable.drawable = AbImageUtil.bitmapToDrawable(bitmap);
        urlDrawable.setBounds(bounds);
        urlDrawable.drawable.setBounds(bounds);
        //一定要重新设置内容，否则图片容易错位
        textView.setText(textView.getText());
        //重新测量控件
        textView.measure(width + i, height);
        //重新摆放布局
        textView.requestLayout();
    }

    class URLDrawable extends BitmapDrawable {
        private Drawable drawable;

        public URLDrawable(Context context) {
            drawable = context.getResources().getDrawable(R.drawable.load_fail);
        }

        @Override
        public void draw(Canvas canvas) {
            //解决 trying to use a recycled bitmap
            try {
                if (drawable != null) {
                    drawable.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
