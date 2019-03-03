package com.app.base.bean;

/**
 * Created by 7du-28 on 2018/1/15.
 */

public class AppInfo {


    /**
     * versionCode : 11
     * versionName : v1.0.0
     * isForceUpdate : true
     */

    private int versionCode;
    private String versionName;
    private boolean isForceUpdate;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(boolean isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }
}
