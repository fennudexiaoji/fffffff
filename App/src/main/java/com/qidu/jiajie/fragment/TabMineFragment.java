package com.qidu.jiajie.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.common.lib.base.AbsBaseFragment;

import com.qidu.jiajie.R;

//服务分类
@SuppressLint("ValidFragment")
public class TabMineFragment extends AbsBaseFragment {
    private String mTitle;

    public static TabMineFragment getInstance(String title) {
        TabMineFragment sf = new TabMineFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserCenterFragment userCenterFragment=new UserCenterFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,userCenterFragment)
                .commit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_mine, null);
        return v;
    }

}