package com.example.admin.hn.ui.account;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.base.PermissionsListener;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.utils.MediaScanner;


/**
 * 回执详情
 *
 * @author Administrator
 */
public class ReturnDetailActivity extends BaseActivity {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView text_tile_right;
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.img)
    ImageView img;
    private String[] permissions = {Manifest.permission.CAMERA};
    private String url = Api.BASE_URL + Api.GET_RECEIPT_PHOTO;
    private String upload_url= Api.BASE_URL + Api.UPLOAD;
    private String receiveNo;
    private String photoPath;//显示的图片地址
    private String cropPath;//裁剪完成的输出地址
    private boolean isCrop;//是否裁剪  默认为false
    private File file;
    private FinalHttp fh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_details);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    @Override
    public void initTitleBar() {
        Intent intent = getIntent();
        receiveNo = intent.getStringExtra("receiveNo");
        textTitle.setText("回执详情");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }
    /**
     *
     */
    public static void startActivity(Context context,String receiveNo) {
        Intent intent = new Intent(context, ReturnDetailActivity.class);
        intent.putExtra("receiveNo", receiveNo);
        ((Activity) context).startActivityForResult(intent, 1001);
    }

    @OnClick({R.id.text_title_back,R.id.bt,R.id.img,R.id.text_tile_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                close();
            break;
            case R.id.bt:
                isCrop = false;
                requestPermissions(permissions, mListener);
                break;
            case R.id.text_tile_right:
                if (file!=null && file.exists()) {
                    uploadImg();
                }else {
                    ToolAlert.showToast(context,"请先拍摄照片");
                }
                break;
            case R.id.img:
                isCrop = true;
                //生成裁剪输出地址
                cropPath = RxGalleryFinalApi.getModelPath();
                RxGalleryFinalApi.cropScannerForResult(this, cropPath, photoPath);
                break;
        }
    }


    private PermissionsListener mListener = new PermissionsListener() {
        @Override
        public void onGranted() {
            openCamera();
        }

        @Override
        public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
            if (!isNeverAsk) {//请求权限没有全被勾选不再提示然后拒绝
                ToolAlert.dialog(context, "权限申请",
                        "为了能正常使用拍摄照片功能，请授予所需权限!",
                        "授权","取消",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermissions(permissions, mListener);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            } else {//全被勾选不再提示
                ToolAlert.dialog(context, "权限申请",
                        "为了能正常使用拍摄照片功能，请手动授予所需权限!",
                        "授权","取消",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
            }
        }
    };

    @Override
    public void initData() {
        super.initData();
        fh = new FinalHttp();
        sendHttp();
    }

    private void sendHttp() {
        params.put("receiveNo", receiveNo + "");
        http.postJson(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("回执详情", json);
                if (GsonUtils.isSuccess(json)) {
                    String photoUrl = GsonUtils.getString(json, "photoUrl");
                    if (ToolString.isEmpty(photoUrl)) {
                        bt.setVisibility(View.GONE);
                        ToolViewUtils.glideImage(photoUrl, img, R.drawable.load_fail);
                    }else {
                        text_tile_right.setText("上传");
                        bt.setVisibility(View.VISIBLE);
                    }
                }else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
            }
        });
    }
    /**
     * 上传图片
     */
    private void uploadImg() {
        AjaxParams params = new AjaxParams();
        params.put("userId", HNApplication.mApp.getUserId());//船舶用户ID 默认传递
        params.put("receiveNo", receiveNo + "");
        try {
            params.put("file", file); // 上传文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fh.post(upload_url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                Logger.e("上传成功", json);
                if (GsonUtils.isSuccess(json)) {
                    ToolAlert.showToast(context,"上传成功");
                    close();
                } else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Logger.e("上传失败", strMsg);
                ToolAlert.showToast(context, strMsg);
            }
        });

    }

    private void close() {
        setResult(1001);
        finish();
    }

    /**
     * 打开相机拍照
     */
    private void openCamera() {
        RxGalleryFinalApi.openZKCamera(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RxGalleryFinalApi.TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            photoPath = RxGalleryFinalApi.fileImagePath.getPath();
            Logger.i("拍照OK，图片路径:" + photoPath);
            file = new File(photoPath);
            Glide.with(context).load(file).into(img);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCrop && cropPath != null) {//如果是从裁剪返回 刷新图片
            RxGalleryFinalApi.cropActivityForResult(ReturnDetailActivity.this, cropPath, new MediaScanner.ScanCallback() {
                @Override
                public void onScanCompleted(String[] images) {
                    if (ToolString.isEmpty(images[0])) {
                        cropPath = images[0];
                        handler.sendEmptyMessage(100);
                    }
                }
            });
        }
    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 100) {
                isCrop = false;//已裁剪完成 改变状态
                file = new File(cropPath);
                if (file.exists()) {
                    photoPath = cropPath;//修改当前显示的地址
                    Glide.with(context).load(file).into(img);
                }

            }
            return false;
        }
    });

}
