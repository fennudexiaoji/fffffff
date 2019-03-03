package com.qidu.jiajie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.lib.base.AbsBaseFragment;
import com.common.lib.global.AppGlobal;
import com.flyco.tablayout.SlidingTabLayout;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.LoginActivity;

import java.util.ArrayList;

/**
 * 赚钱
 */

public class MakeMoneyFragment extends AbsBaseFragment{
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String mTitle;
    private String[] mTitles;
    private View rootView;
    public static MakeMoneyFragment getInstance(String title) {
        MakeMoneyFragment sf = new MakeMoneyFragment();
        sf.mTitle = title;
        return sf;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_make_money, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView.findViewById(R.id.title_parent_top).setBackgroundColor(getResources().getColor(R.color.app_main_color));
        rootView.findViewById(R.id.header_left_btn_img).setVisibility(View.GONE);
        if(!AppGlobal.isLogin()){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        TextView titleCenter=view.findViewById(R.id.header_text);
        titleCenter.setTextColor(getResources().getColor(R.color.white));
        titleCenter.setText("赚钱");
        mTitles = new String[]{"综合排序", "服务类别", "离我最近"};
        for (String title : mTitles) {
            mFragments.add(NeedListFragment.getInstance(title));
        }
        ViewPager vp=rootView.findViewById(R.id.vp);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        vp.setAdapter(mAdapter);
        SlidingTabLayout tabLayout = rootView.findViewById(R.id.sliding_tab_layout);
        tabLayout.setViewPager(vp, mTitles);
        vp.setCurrentItem(0);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
