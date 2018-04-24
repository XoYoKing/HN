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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.PermissionsListener;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.utils.MediaScanner;


/**
 * 退货详情
 *
 * @author Administrator
 */
public class ReturnDetailActivity extends BaseActivity {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.img)
    ImageView img;
    private String[] permissions = {Manifest.permission.CAMERA};

    private int id;
    private String title;
    private String photoPath;//显示的图片地址
    private String cropPath;//裁剪完成的输出地址
    private boolean isCrop;//是否裁剪  默认为false


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
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        textTitle.setText("回执详情");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }
    /**
     *
     */
    public static void startActivity(Context context, int id, String title) {
        Intent intent = new Intent(context, ReturnDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @OnClick({R.id.text_title_back,R.id.bt,R.id.img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
            break;
            case R.id.bt:
                isCrop = false;
                requestPermissions(permissions, mListener);
                break;
            case R.id.img:
                //编辑图片
//                RxGalleryFinal.with(context)
//                        .image()            //选择图片
//                        .radio()            //单选
//                        .crop()              //设置剪裁
//                        .imageLoader(ImageLoaderType.GLIDE)     //调用glide图片加载器
//                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
//                            @Override
//                            protected void onEvent(ImageRadioResultEvent baseResultEvent) throws Exception {
//                                String filePath=baseResultEvent.getResult().getOriginalPath();
//                                Logger.i("裁剪路径:" + filePath);
//                                File file = new File(filePath);
//                                Glide.with(context).load(file).into(img);
//                            }
//                        })
//                    .openGallery();
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
            File file = new File(photoPath);
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
                photoPath = cropPath;//修改当前显示的地址
                isCrop = false;//已裁剪完成 改变状态
                File file = new File(cropPath);
                Glide.with(context).load(file).into(img);
            }
            return false;
        }
    });

}
