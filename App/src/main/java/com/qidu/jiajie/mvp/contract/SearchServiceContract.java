package com.qidu.jiajie.mvp.contract;

import com.app.base.bean.HistoryKeyword;
import com.common.lib.base.view.IUIView;

import java.util.List;

/**
 * Created by 7du-28 on 2018/4/20.
 */

public class SearchServiceContract {

    public interface Model {
        /**
         * 获取登陆数据
         * @return Observable<LoginData>
         *//*
        Observable<List<Store>> login(HashMap<String, String> treeMap);*/


        void onSaveHistoryKeyword(String keyword);
        void onClearHistoryKeyword();

    }
    //prensent接口省略
    public interface Presenter{
        void onSaveHistoryKeyword(String keyword);
        void onClearHistoryKeyword();
    }

    public interface View extends IUIView {
        void getHistoryKeywordListData(List<HistoryKeyword> list);
    }
}
