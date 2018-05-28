package com.example.admin.hn.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * 打开本地文件方式
 */
public class ToolOpenFiles {
    /**
     *
     * <code>openFile</code>
     * @description: TODO(打开附件)
     * @param context
     * @param file
     * @since   2012-5-19    liaoyp
     */
    public static void openFile(Context context, File file){
        try{
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = getMIMEType(file);
            //设置intent的data和Type属性。
            if (Build.VERSION.SDK_INT >= 24) {
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri uri = FileProvider.getUriForFile(context, "com.example.admin.hn.utils.UpdateManager", file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, type);
            } else {
                intent.setDataAndType(Uri.fromFile(file), type);
            }
            //跳转
            context.startActivity(intent);
        }catch (Exception e) {
            // TODO: handle exception
            ToolAlert.showToast(context, "没有找到打开该文件的应用程序");
        }
    }
    private static String getMIMEType(File file) {
        String type="*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
        /* 获取文件的后缀名*/
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){
            if(end.equals(MIME_MapTable[i][0])) {
                type = MIME_MapTable[i][1];
                break;
            }
        }
        return type;
    }
    //可打开的文件类型
    private static String [][]  MIME_MapTable={
            //{后缀名，MIME类型}
            {".doc",    "application/msword"},
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls",    "application/vnd.ms-excel"},
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".html",   "text/html"},
            {".jpeg",   "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".log",    "text/plain"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".txt",    "text/plain"},
            {".xml",    "text/plain"},
            {".zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };
}
