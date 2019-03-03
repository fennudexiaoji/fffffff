package com.qidu.jiajie;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.common.lib.base.AbsBaseActivity;
import com.common.lib.global.AppGlobal;
import com.qidu.jiajie.bean.Notice;
import com.qidu.jiajie.bean.TabEntity;
import com.qidu.jiajie.databinding.ActivityMainBinding;
import com.qidu.jiajie.fragment.HomeFragment;
import com.qidu.jiajie.fragment.MakeMoneyFragment;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qidu.jiajie.fragment.ShopperCenterFragment;
import com.qidu.jiajie.fragment.TabNoticeFragment;
import com.qidu.jiajie.fragment.TabOrderFragment;
import com.qidu.jiajie.fragment.UserCenterFragment;
import com.qidu.jiajie.fragment.WorkHomeFragment;
import com.qidu.jiajie.mvp.model.HomeImplAPI;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import me.leolin.shortcutbadger.ShortcutBadger;


public class MainActivity extends AbsBaseActivity{
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ActivityMainBinding binding;
    private String[] mTitles=null;
    private int[] mIconUnselectIds;
    private int[] mIconSelectIds;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        String packageName=getPackageName();
        Log.i("aaaaa","包名--->"+packageName);
        if(packageName.equals("com.qidu.jiajie")){
            mTitles = new String[]{"首页", "消息", "订单", "我的"};
            mIconUnselectIds = new int[]{
                    R.drawable.tab_home_unselect, R.drawable.tab_notice_unselect,
                    R.drawable.tab_order_unselect, R.drawable.tab_mine_unselect};
            mIconSelectIds = new int[]{
                    R.drawable.tab_home_select, R.drawable.tab_notice_select,
                    R.drawable.tab_order_select, R.drawable.tab_mine_select};
            mFragments.add(HomeFragment.getInstance("" + mTitles[0]));
            mFragments.add(TabNoticeFragment.getInstance("" + mTitles[1]));
            mFragments.add(TabOrderFragment.getInstance("" + mTitles[2]));
            mFragments.add(UserCenterFragment.getInstance(""+mTitles[3]));
        }else if(packageName.equals("com.qidu.jiajie.manager")){
            mTitles = new String[]{"首页", "消息","赚钱"};
            mIconUnselectIds = new int[]{
                    R.drawable.tab_home_unselect, R.drawable.tab_notice_unselect,R.drawable.gzt_icon_make_money_unselect};
            mIconSelectIds = new int[]{
                    R.drawable.tab_home_select, R.drawable.tab_notice_select,
                    R.drawable.tab_order_select, R.drawable.gzt_icon_make_money_select};
            mFragments.add(WorkHomeFragment.getInstance("" + mTitles[0]));
            mFragments.add(TabNoticeFragment.getInstance("" + mTitles[1]));
            mFragments.add(MakeMoneyFragment.getInstance("" + mTitles[2]));
        }else if(packageName.equals("com.qidu.jiajie.seller")){
            mTitles = new String[]{"首页", "消息","赚钱"};
            mIconUnselectIds = new int[]{
                    R.drawable.tab_home_unselect, R.drawable.tab_notice_unselect, R.drawable.gzt_icon_make_money_unselect};
            mIconSelectIds = new int[]{
                    R.drawable.tab_home_select, R.drawable.tab_notice_select, R.drawable.gzt_icon_make_money_select};
            mFragments.add(ShopperCenterFragment.getInstance("" + mTitles[0]));
            mFragments.add(TabNoticeFragment.getInstance("" + mTitles[1]));
            mFragments.add(MakeMoneyFragment.getInstance("" + mTitles[2]));
        }

        for (int i = 0; i < mFragments.size(); i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        binding.vp.setNoScroll(true);
        binding.vp.setOffscreenPageLimit(mTitles.length);
        binding.vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        /** with ViewPager */
        binding.tl.setTabData(mTabEntities);
        binding.tl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.tl.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.vp.setCurrentItem(0);


        //两位数
        /*binding.tl.showMsg(0, 55);
        binding.tl.setMsgMargin(0, -5, 5);

        //三位数
        binding.tl.showMsg(1, 100);
        binding.tl.setMsgMargin(1, -5, 5);

        //设置未读消息红点
        binding.tl.showDot(2);
        MsgView rtv_2_2 = binding.tl.getMsgView(2);
        if (rtv_2_2 != null) {
            UnreadMsgUtils.setSize(rtv_2_2, ScreenUtils.dp2px(getActivity(),7.5f));
        }

        //设置未读消息背景
        binding.tl.showMsg(3, 5);
        binding.tl.setMsgMargin(3, 0, 5);
        MsgView rtv_2_3 = binding.tl.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }*/


    }
    private long exitTime = 0;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                AppApplication.getInstance().exit();
            }
            return false;
        }
        return false;
    }

    private void getNoticeUnRead(){
        HomeImplAPI.getNoticeUnRead().subscribe(new Consumer<Notice>() {
            @Override
            public void accept(Notice notice) throws Exception {
                int badgeCount=Integer.parseInt(notice.getUnread());
                ShortcutBadger.applyCount(getActivity(), badgeCount);
                String packageName=getPackageName();
                if(packageName.equals("com.qidu.jiajie")){
                    if(badgeCount>0){
                        binding.tl.showMsg(2, badgeCount);
                        binding.tl.setMsgMargin(2, -5, 5);
                    }
                }else {
                    if(badgeCount>0){
                        binding.tl.showMsg(1, badgeCount);
                        binding.tl.setMsgMargin(2, -5, 5);
                    }
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if(AppGlobal.isLogin()){
            //getNoticeUnRead();
        }
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
    /*private void test(){
        BaseResponse<String> objectBaseResponse = new BaseResponse<>();
        objectBaseResponse.setData(new String());
        objectBaseResponse.setCode(100);
        Observable.just(objectBaseResponse)
                .compose(new JsonParesTransformer<>(BaseResponse.class))
                .subscribe(new SimpleObserver<BaseResponse>(mPresenter.getCompositeSubscription()) {
                    @Override
                    public void call(BaseResponse o) {

                    }
                });

        Observable.just(objectBaseResponse).compose(new LoadingTransformer<BaseResponse>(new LoadingTransformer.LoadingInterface() {
            @Override
            public void onLoading() {
                Log.i("aaaaa","onLoading");
            }

            @Override
            public void onSuccess() {
                Log.i("aaaaa","onSuccess");
            }

            @Override
            public void onError() {
                Log.i("aaaaa","onError");
            }

            @Override
            public void onEmpty() {
                Log.i("aaaaa","onEmpty");
            }
        })).subscribe();
    }*/
}
