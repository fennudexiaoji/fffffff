package com.qidu.jiajie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.base.utils.CommonKey;
import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.common.lib.base.AbsBaseFragment;
import com.common.lib.divider.DividerItemDecoration;
import com.common.lib.global.AppGlobal;
import com.common.lib.list.BaseRecyclerAdapter;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.adapter.NoticeListAdapter;
import com.qidu.jiajie.bean.Notice;
import com.qidu.jiajie.mvp.model.HomeImplAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 通知
 */

public class TabNoticeFragment extends AbsBaseFragment{
    private String param;
    protected RecyclerView mRecyclerView;
    private NoticeListAdapter adapter;
    public static TabNoticeFragment getInstance(String param) {
        TabNoticeFragment sf = new TabNoticeFragment();
        sf.param = param;
        return sf;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_notice, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.title_parent_top).setBackgroundColor(getResources().getColor(R.color.app_main_color));
        view.findViewById(R.id.header_left_btn_img).setVisibility(View.GONE);

        TextView titleCenter=view.findViewById(R.id.header_text);
        titleCenter.setTextColor(getResources().getColor(R.color.white));
        titleCenter.setText("消息");


        mRecyclerView=view.findViewById(R.id.recycler);
        adapter = new NoticeListAdapter(getActivity());
        DividerItemDecoration decoration=new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL,R.drawable.list_divider_default);
        decoration.showLastFootViewDivider(false);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View var1, int var2, long var3) {
                Intent intent=new Intent(getActivity(), WebAppActivity.class);
                intent.putExtra(IntentParams.WEB_URL, BuildConfig.DEFAULT_HOST+"/#/notice");
                getActivity().startActivity(intent);
            }
        });
        List<Notice> list=new ArrayList<>();
        Notice noticeSystem=new Notice();
        noticeSystem.setMsgType(CommonKey.KEY_NOTICE_SYSTEM);
        noticeSystem.setTitle("系统通知");
        noticeSystem.setBulletin_content("你有新的消息未查看，请及时查看");
        list.add(noticeSystem);

        Notice noticePublic=new Notice();
        noticePublic.setMsgType(CommonKey.KEY_NOTICE_CUSTOM);
        noticePublic.setTitle("在线客服");
        noticePublic.setBulletin_content("工作时间：10:00-22:00");
        list.add(noticePublic);

        /*Notice noticeApproval=new Notice();
        noticeApproval.setMsgType(CommonKey.KEY_NOTICE_APPROVAL);
        noticeApproval.setTitle("审批通知");
        noticeApproval.setBulletin_content("你有新的消息未查看，请及时查看");
        list.add(noticeApproval);*/
        adapter.refresh(list);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View var1, int position, long var3) {
                if(position==0){
                    String url="/#/notice";
                    startWebActivity(url);
                }else if(position==1){
                    String url="https://lyt.zoosnet.net/lr/chatpre.aspx?id=LYT42657310";
                    Intent intent=new Intent(getActivity(), WebAppActivity.class);
                    intent.putExtra(IntentParams.WEB_URL, url);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    public void startWebActivity(String url) {
        Intent intent=new Intent(getActivity(), WebAppActivity.class);
        intent.putExtra(IntentParams.WEB_URL, BuildConfig.DEFAULT_HOST+url);
        getActivity().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppGlobal.isLogin()){
            //getNoticeUnRead();
        }
    }

    private void getNoticeUnRead(){
        HomeImplAPI.getNoticeUnRead().subscribe(new Consumer<Notice>() {
            @Override
            public void accept(Notice notice) throws Exception {
                if(!notice.getUnread().equals("0")){
                    adapter.getDataList().get(0).setUnread(notice.getUnread());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}
