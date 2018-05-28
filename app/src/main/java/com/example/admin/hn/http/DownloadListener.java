package com.example.admin.hn.http;

import java.io.File;

public interface DownloadListener {
    /**
     * 下载失败的回调方法
     */
    void onFailure();

    /**
     * 这是一个下载成功之后,返回文件路径的方法
     *
     * @param file
     */
    void onSuccess(File file);
}
