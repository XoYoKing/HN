package com.example.admin.hn.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.interf.Initialable;
import com.example.admin.hn.utils.StateBarUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;

/**
*  @date on 2017/7/26 16:06
*  @author duantao
*  @describe 
*/
public class BaseFragment extends Fragment implements Initialable {


    protected FragmentActivity activity;

    /**
     * 设置ListView的高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            //            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        );
        activity = getActivity();
        initStateBar();
    }

    private void initStateBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#3084EC"));
        StateBarUtil.initSystemBar(getActivity());
    }

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getActivity().startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initTitleBar() {

    }
}
