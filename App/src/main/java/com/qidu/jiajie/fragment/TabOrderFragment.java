package com.qidu.jiajie.fragment;

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

import java.util.ArrayList;

/**
 *订单
 */

public class TabOrderFragment extends AbsBaseFragment{
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String mTitle;
    private String[] listTitle;
    public static TabOrderFragment getInstance(String title) {
        TabOrderFragment sf = new TabOrderFragment();
        sf.mTitle = title;
        return sf;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_order, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.title_parent_top).setBackgroundColor(getResources().getColor(R.color.app_main_color));
        view.findViewById(R.id.header_left_btn_img).setVisibility(View.GONE);

        TextView titleCenter=view.findViewById(R.id.header_text);
        titleCenter.setTextColor(getResources().getColor(R.color.white));
        titleCenter.setText("订单");
        listTitle = new String[]{"全部", "待付款", "待确认","待服务","服务中","已关闭"};
        for (int i=0;i<listTitle.length;i++) {
            mFragments.add(OrderListFragment.getInstance(i+""));
        }
        ViewPager vp=view.findViewById(R.id.vp);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        vp.setAdapter(mAdapter);
        SlidingTabLayout tabLayout = view.findViewById(R.id.sliding_tab_layout);
        tabLayout.setViewPager(vp, listTitle);
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
            return listTitle[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
