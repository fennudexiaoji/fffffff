package com.qidu.jiajie.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;


public class ApkInstallBroadCastReceiver extends BroadcastReceiver{
    private String fileDirName="upgrade_apk";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            deleteApk(context);
        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            deleteApk(context);
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            deleteApk(context);
        }
    }


    private void deleteApk(Context context){
        String fileKey="bangjiajie.apk";
        final String saveDir = context.getApplicationContext().getExternalFilesDir(fileDirName) + File.separator;
        File file = new File(saveDir, fileKey);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }
}
