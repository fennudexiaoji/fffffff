package com.common.lib.bean;

/**
 * Created by 7du-28 on 2017/7/27.
 */

public class ActionItem {

    private boolean isSelect;

    private String itemName;

    private int itemType;


    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
