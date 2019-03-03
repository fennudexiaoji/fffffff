package com.qidu.jiajie.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.app.base.widget.LoadMoreView;
import com.common.lib.widget.ClearEditText;
import com.qidu.jiajie.R;
import com.qidu.jiajie.adapter.LocationListAdapter;
import com.qidu.jiajie.utils.DataUtils;

import java.util.List;

/**
 * 关键字搜索地址
 */

public class KeyWordSearchAddressFragment extends DialogFragment implements  PoiSearch.OnPoiSearchListener{
    private ListView listView;
    private LocationListAdapter adapter;
    private ClearEditText keyWordEdit;
    private View btnBack;
    private int currentPage=1;//当前页数    0和的数据一样，从一开始
    private int pageSize=10;
    private String keyWord;
    protected InputMethodManager imm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fragment_keyword_search_address, container, false);
        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadMoreView=new LoadMoreView(getActivity());
        btnBack=view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyWordSearchAddressFragment.this.dismiss();
            }
        });
        //btnSend=view.findViewById(R.id.btn_send);
        /*btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        keyWordEdit=view.findViewById(R.id.search_edit);
        listView=view.findViewById(R.id.address_list);
        List<PoiItem> poiItemList=null;
        adapter = new LocationListAdapter(listView,
                poiItemList);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }
            @Override
            public void onScroll(AbsListView absListView,  int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (!mIsLoading && !mIsPageFinished && lastVisibleItem == totalItemCount) {
                    mIsLoading = true;
                    // 显示加载更多布局
                    listView.addFooterView(mLoadMoreView);
                    searchAddressByKeyword();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.getCount()==0){
                    return;
                }
                adapter.setSelection(position);
                PoiItem poiSelectedItem = adapter.getItem(adapter.getSelection());

                if(poiSelectedItem.getCityCode().equals("020")){
                    if(listener!=null){
                        listener.onSuccessCallBack(DataUtils.getAddressData(poiSelectedItem));
                    }
                    KeyWordSearchAddressFragment.this.dismiss();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("温馨提示")
                            .setMessage("目前帮家洁平台只能选择广州市范围地址哦")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create()
                            .show();
                }

            }
        });
        keyWordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWord=s.toString();
                currentPage=1;
                if(adapter!=null){
                    adapter.refresh(null);
                }
                if(!TextUtils.isEmpty(keyWord)){
                    searchAddressByKeyword();
                }
            }
        });
        if(mLoadMoreView!=null){
            listView.removeFooterView(mLoadMoreView);
        }

    }


    private void searchAddressByKeyword() {//cityCode为城市区号
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(pageSize);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setDistanceSort(true);//距离排序
        PoiSearch poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();// 异步搜索
    }
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<PoiItem> list=poiResult.getPois();
            if(!list.isEmpty()){
                if(currentPage==1){
                    adapter.refresh(list);
                    adapter.setSelection(0);
                    //默认显示
                    //PoiItem selected = adapter.getItem(adapter.getSelection());

                }else {
                    adapter.append(list);
                }
                if(list.size()==pageSize){
                    currentPage++;
                    onFinishLoading(false);
                }else {
                    onFinishLoading(true);
                }
                //setData(poiItems.size() >= 10 ? -1 : 0);
            }else {
                noMoreData();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    public void onFinishLoading(boolean isPageFinished) {
        mIsLoading = false;
        setIsPageFinished(isPageFinished);

    }

    private void setIsPageFinished(boolean isPageFinished) {
        mIsPageFinished = isPageFinished;
        listView.removeFooterView(mLoadMoreView);
    }
    public void noMoreData(){
        mIsLoading = true;
        setIsPageFinished(true);
    }
    private boolean mIsLoading = false;
    private boolean mIsPageFinished = false;
    private View mLoadMoreView;




    /*private void callbackOnHandle(String message) {
        final Fragment fragment = getTargetFragment();
        if (fragment instanceof SendPOIAddressCallback) {
            ((SendPOIAddressCallback) fragment).onSuccessCallBack(message);
        }
    }*/
    private SendPOIAddressCallback listener;
    public void setSendPOIAddressCallback(SendPOIAddressCallback listener){
        this.listener=listener;
    }

    public interface SendPOIAddressCallback {
        void onSuccessCallBack(String message);
    }


}
