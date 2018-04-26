//package com.example.admin.hn.utils;
//
//import android.app.Activity;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.example.admin.hn.R;
//import com.lzy.imagepicker.loader.ImageLoader;
//
//import java.io.File;
//
//
///**
// * Created by Administrator on 2017/12/25 0025.
// */
//
//public class GlideLoader implements ImageLoader {
//
//
//    @Override
//    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//        Glide.with(activity).load(new File(path))
//                .error(R.drawable.load_fail)
//                .centerCrop()
//                .into(imageView); //设置图片
//    }
//
//    @Override
//    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
//
//    }
//
//    @Override
//    public void clearMemoryCache() {
//
//    }
//}
