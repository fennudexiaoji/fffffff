package com.app.base.bean;

import android.widget.TextView;
import com.app.base.widget.LabelsView;

import java.util.List;

public class HistoryKeyword implements LabelsView.LabelTextProvider {

    private String keyword;

    private long addTime;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    /*public static boolean isHistoryKeywordExits(String keyword) {
        return new Select()
                .from(HistoryKeyword.class).where("keyword = ?", keyword)
                .exists();
    }
    public static List<HistoryKeyword> getHistoryKeywordList() {
        return new Select()
                .from(HistoryKeyword.class)*//*.limit(5)*//*.execute();
    }*/

    @Override
    public CharSequence getLabelText(TextView label, int position, Object data) {
        return keyword;
    }

    /*public static List<HistoryKeyword> getHistoryKeywordList(String keyword) {
        return new Select()
                .from(HistoryKeyword.class).where("keyword like ?", keyword)*//*.limit(5)*//*.execute();
    }*/

    /*public static void deleteHistoryKeyword(String keyword){
        new Delete().from(HistoryKeyword.class).where("keyword = ?", keyword).execute();
    }

    public static void deleteAllHistoryKeyword(){
        new Delete().from(HistoryKeyword.class).execute();
    }*/
}
