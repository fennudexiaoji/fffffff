package com.app.base.bean;

import com.bigkoo.pickerview.model.IPickerViewData;


public class FranchiseesBean implements IPickerViewData {

    private int position;
    private String value;

    public FranchiseesBean(int position, String value) {
        this.position = position;
        this.value = value;
    }

    public int getId() {
        return position;
    }

    public void setId(int id) {
        this.position = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String cardNo) {
        this.value = cardNo;
    }

    @Override
    public String getPickerViewText() {
        return value;
    }
}
