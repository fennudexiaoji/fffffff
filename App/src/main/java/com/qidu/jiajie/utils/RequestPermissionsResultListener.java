package com.qidu.jiajie.utils;


import android.support.annotation.NonNull;

public interface RequestPermissionsResultListener {
    void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults);
}
