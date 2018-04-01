package com.example.admin.hn.utils;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.hn.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


/**
 * 检查更新
 */
public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    public static HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    public static String mSavePath;
    /* 记录进度条数量 */
    public static int progress;
    /* 是否取消更新 */
    public static boolean cancelUpdate = false;

    public static Context mContext;
    /* 更新进度条 */
    public static ProgressBar mProgress;
    public static Dialog mDownloadDialog;
    public static String url = "http://139.196.189.45:8080/hanjue/app/hanjue.apk";

    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
//                    installApk();
                    break;
                default:
                    break;
            }
        }


    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        if (isUpdate()) {
            // 显示提示对话框
            showNoticeDialog();
        } else {
            Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private boolean isUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode(mContext);

        // 把version.xml放到网络上，然后获取文件信息
        //		InputStream inStream = ParseXmlService.class.getClassLoader()
        //				.getResourceAsStream("version.xml");
        //		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
        //		ParseXmlService service = new ParseXmlService();
        //		try {
        //			mHashMap = service.parseXml(inStream);
        //		} catch (Exception e) {
        //			e.printStackTrace();
        //		}
        //		if (null != mHashMap) {
        //			int serviceCode = Integer.valueOf(mHashMap.get("version"));
        //			// 版本判断
        //			if (serviceCode > versionCode) {
        //				return true;
        //			}
        //		}
        return false;
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(
                    "com.cisetech.hjb", 0
            ).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    public static void showNoticeDialog() {
        // 构造对话框
        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        // 更新
        builder.setPositiveButton(
                R.string.soft_update_updatebtn, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 显示下载对话框
                        showDownloadDialog();
                    }
                }
        );
        // 稍后更新
        builder.setNegativeButton(
                R.string.soft_update_later, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    public static void showDownloadDialog() {
        // 构造软件下载对话框
        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(
                R.string.soft_update_cancel, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置取消状态
                        cancelUpdate = true;
                    }
                }
        );
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 下载文件
        //		downloadApk();
//        OkHttpUtil.dowloadProgress(MyApplication.getContext(), url);


    }

    /**
     * 下载apk文件
     */
    public static void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    public static class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED
                )) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(mHashMap.get("url"));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, mHashMap.get("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }


    /**
     * 安装APK文件
     */
    public static void installApk(Context context) {
        //安装文件，一般固定写法
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(
//                Uri.fromFile(
//                        new File(
//                                Environment.getExternalStorageDirectory(), "hjb.apk"
//                        )
//                ), "application/vnd.android.package-archive"
//        );
//        context.startActivity(intent);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String fileName = Environment.getExternalStorageDirectory() + "/" + "HN.apk";

        Uri uri = Uri.fromFile(new File(fileName));
        if (Build.VERSION.SDK_INT >= 24) {
//参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri =
                    FileProvider.getUriForFile(context, "com.example.admin.hn.utils.UpdateManager", new File(fileName));
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }


}