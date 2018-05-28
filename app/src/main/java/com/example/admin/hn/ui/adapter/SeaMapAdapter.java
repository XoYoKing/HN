package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.http.DownloadListener;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.ArticleInfo;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolOpenFiles;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.widget.LoadingFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/7/31 15:35
 * 海圖資料
 */
public class SeaMapAdapter extends CommonAdapter<ArticleInfo> {

    public SeaMapAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final ArticleInfo info, int position) {
        viewHolder.setText(R.id.tv_name, info.pubTitle + "");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolString.isEmpty(info.originalUrl)) {
                    try {
                        File file = new File(Environment.getExternalStorageDirectory(), info.originalName);
                        if (file.exists()) {//如果文件已存在 直接打开文件
                            ToolOpenFiles.openFile(mContext,file);
                        }else {
                            LoadingFragment dialog = LoadingFragment.showLoading(mContext, "正在下载...");
                            OkHttpUtil.downloadFile(mContext, info.originalUrl, dialog, info.originalName, new DownloadListener() {
                                @Override
                                public void onFailure() {
                                    ToolAlert.showToast(mContext, "下载失败");
                                }

                                @Override
                                public void onSuccess(File file) {
                                    ToolAlert.showToast(mContext, "下载成功");
                                    ToolOpenFiles.openFile(mContext,file);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
