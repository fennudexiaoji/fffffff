package com.app.base.bean;


import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by 7du-28 on 2017/7/11.
 */

public class DateEntity implements IPickerViewData {
    private long timeMillis;

    private String date;

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getPickerViewText() {
        return date;
    }
}
