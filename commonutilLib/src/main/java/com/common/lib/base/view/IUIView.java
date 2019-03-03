package com.common.lib.base.view;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

/**
 * Created by baozi on 2017/2/20.
 * 用户页面,操作页面，对应Activity,frgament...
 */

public interface IUIView extends IBaseView {

    /**
     * res资源获取
     *
     * @return
     */
    Resources getResources();

    /**
     * 回退
     */
    void onBack();

    /**
     * 获取Acitivity
     *
     * @return
     */
    Activity getActivity();

    /**
     * Frgament跳转.
     *
     * @param tofragment
     */
    void startFragment(Fragment tofragment);

    /**
     * Frgament跳转.
     *
     * @param tofragment
     */
    void startFragment(Fragment tofragment, String tag);

}
