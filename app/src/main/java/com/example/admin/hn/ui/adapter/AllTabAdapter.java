package com.example.admin.hn.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.admin.hn.utils.ToolString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *         tabLayout+viewpager+fragment
 */
public class AllTabAdapter extends FragmentPagerAdapter {

    private FragmentActivity context;
    private ViewPager view;
    private List<TabInfo> list = new ArrayList<>();

    public AllTabAdapter(FragmentActivity fm, ViewPager view) {
        super(fm.getSupportFragmentManager());
        this.context = fm;
        this.view = view;
        this.view.setAdapter(this);
    }

    class TabInfo {
        private Class<?> clazz;
        private String tab;
        private String type;

        public TabInfo(String tab, Class<?> clazz) {
            this.tab = tab;
            this.clazz = clazz;
        }

        public TabInfo(String tab, String type, Class<?> clazz) {
            this.tab = tab;
            this.clazz = clazz;
            this.type = type;
        }
    }

    /**
     * 如果使用不同的fragment
     *
     * @param tab
     * @param clazz
     */
    public void addTab(String tab, Class<?> clazz) {
        TabInfo info = new TabInfo(tab, clazz);
        list.add(info);
        notifyDataSetChanged();
    }

    /**
     * 如果使用同一个fragment
     *
     * @param tab   title
     * @param type  用于网络请求的参数
     * @param clazz fragment 的类
     */
    public void addTab(String tab, String type, Class<?> clazz) {
        TabInfo info = new TabInfo(tab, type, clazz);
        list.add(info);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int arg0) {
        TabInfo tabinfo = list.get(arg0);
        Fragment fragment = Fragment.instantiate(context, tabinfo.clazz.getName());
        if (ToolString.isNoBlankAndNoNull(tabinfo.type)) {
            Bundle bundle = new Bundle();
            bundle.putString("type", tabinfo.type);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return list.get(position).tab;
    }


}
