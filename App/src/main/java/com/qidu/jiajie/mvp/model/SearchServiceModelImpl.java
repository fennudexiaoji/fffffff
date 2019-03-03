package com.qidu.jiajie.mvp.model;

import com.qidu.jiajie.mvp.contract.SearchServiceContract;

/**
 * Created by 7du-28 on 2018/4/20.
 */

public class SearchServiceModelImpl implements SearchServiceContract.Model{


    @Override
    public void onSaveHistoryKeyword(String keyword) {
        /*boolean isExist=HistoryKeyword.isHistoryKeywordExits(keyword);
        if(isExist){
            HistoryKeyword.deleteHistoryKeyword(keyword);
        }
        HistoryKeyword historyKeyword=new HistoryKeyword();
        historyKeyword.setAddTime(System.currentTimeMillis());
        historyKeyword.setKeyword(keyword);
        historyKeyword.save();*/
    }

    @Override
    public void onClearHistoryKeyword() {
        //HistoryKeyword.deleteAllHistoryKeyword();
    }
}
