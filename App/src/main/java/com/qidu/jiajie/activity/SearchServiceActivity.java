package com.qidu.jiajie.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.app.base.bean.HistoryKeyword;
import com.app.base.widget.LabelsView;
import com.common.lib.base.AbsBaseActivity;
import com.common.lib.utils.ToastUtils;
import com.common.lib.widget.ClearEditText;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import com.qidu.jiajie.R;
import com.qidu.jiajie.bean.TabEntity;

/**
 * 搜索服务-搜索商家
 */

public class SearchServiceActivity extends AbsBaseActivity{
    private LabelsView labelsView;
    private ClearEditText searchKey;
    private View bthSearchSure,back,clearHistory;
    private CommonTabLayout mTabLayout;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    protected void initViewsAndEvents() {
        mTabLayout= (CommonTabLayout) findViewById(R.id.tl_service);
        mTabEntities.add(new TabEntity("服务", 0, 0));
        mTabEntities.add(new TabEntity("商家", 0, 0));
        mTabLayout.setTabData(mTabEntities);

        clearHistory=findViewById(R.id.clear_history);
        back=findViewById(R.id.back);
        bthSearchSure=findViewById(R.id.btn_search_sure);
        searchKey = (ClearEditText) findViewById(R.id.search_key);
        searchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword=searchKey.getText().toString().trim();
                    if(TextUtils.isEmpty(keyword)){
                        return false;
                    }
                    //mPresenter.onSaveHistoryKeyword(keyword);
                    return true;
                }
                return false;
            }
        });
        bthSearchSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword=searchKey.getText().toString().trim();
                if(TextUtils.isEmpty(keyword)){
                    return;
                }
                //mPresenter.onSaveHistoryKeyword(keyword);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPresenter.onClearHistoryKeyword();
                /*if(labelsView!=null){
                    List<HistoryKeyword> list=new ArrayList<>();
                    labelsView.setLabels(list, new LabelsView.LabelTextProvider<HistoryKeyword>() {
                        @Override
                        public CharSequence getLabelText(TextView label, int position, HistoryKeyword data) {
                            //根据data和position返回label需要显示的数据。
                            return data.getKeyword();
                        }
                    });
                }*/
            }
        });

        labelsView = (LabelsView) findViewById(R.id.labels);
        //标签的点击监听
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                //label是被点击的标签，data是标签所对应的数据，position是标签的位置。
                HistoryKeyword keyword= (HistoryKeyword) data;
                ToastUtils.show(keyword.getKeyword());
            }
        });

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                ToastUtils.show(position+"");
            }
            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setContentView(R.layout.activity_search_service);
    }


    /*@Override
    public void getHistoryKeywordListData(List<HistoryKeyword> list) {
        //labelsView.clearCompulsorys();
        labelsView.setLabels(list, new LabelsView.LabelTextProvider<HistoryKeyword>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, HistoryKeyword data) {
                //根据data和position返回label需要显示的数据。
                return data.getKeyword();
            }
        });
    }*/
}
