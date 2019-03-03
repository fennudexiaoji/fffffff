package com.qidu.jiajie.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.app.base.bean.AppInfo;
import com.app.base.utils.HttpUrl;
import com.common.lib.base.BaseApplication;
import com.common.lib.global.AppUtils;
import com.common.lib.retrofit.RetrofitUtils;
import com.common.lib.retrofit.rxandroid.DownLoadSubscriber;
import com.google.gson.Gson;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import io.reactivex.functions.Consumer;

public class VersionUpdateUtil {
    /***
     * 创建通知栏
     */
    private static NotificationCompat.Builder mBuilder;
    protected static NotificationManager mNotificationManager;
    private static Notification notification;
    protected static RemoteViews views;
    protected static int NOTIFICATION_ID = 9968;


    private static VersionUpdateUtil updateUtil;
    private Activity activity;
    private String fileKey="bangjiajie.apk";
    private String fileDirName="upgrade_apk";
    private String newVerName = "";// 新版本名称
    private int newVerCode = -1;// 新版本号
    boolean isForceUpdate=false;//是否强制更新
    public static VersionUpdateUtil getInstance(Activity activity){
        if(updateUtil==null){
            updateUtil=new VersionUpdateUtil(activity);
        }
        return updateUtil;
    }

    private VersionUpdateUtil(Activity activity) {
        this.activity = activity;
    }

    protected void initNotification() {
        if (mNotificationManager == null) {
            mBuilder = new NotificationCompat.Builder(
                    activity).setSmallIcon(android.R.drawable.stat_sys_download)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("更新");
            views = new RemoteViews(activity.getPackageName(), R.layout.notification_update_app_view);
            mBuilder.setContent(views);
            mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            notification=mBuilder.build();
            mNotificationManager.notify(NOTIFICATION_ID,notification);
        }
    }
    public boolean getUninatllApkInfo(Context context,String filePath) {
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            Log.e("archiveFilePath", filePath);
            PackageInfo info = pm.getPackageArchiveInfo(filePath,PackageManager.GET_ACTIVITIES);
            if (info != null) {
                result = true;//完整
            }
        } catch (Exception e) {
            result = false;//不完整

            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
        return result;
    }

    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }
    private String getFilePathByKey(String saveDir,String fileName){
        String savePath = null;
        try {
            savePath = isExistDir(saveDir);
            File file = new File(savePath, fileName);
            if(file.exists()){
                return file.getPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }
    /**
     * 下载apk
     */
    public void downloadUpdateFile(final String url) {
        int currentCode= AppUtils.getAppVersionCode(activity);
        final String saveDir = activity.getApplicationContext().getExternalFilesDir(fileDirName) + File.separator;
        String path =getFilePathByKey(saveDir,fileKey);
        if (path != null&&getUninatllApkInfo(activity,path)) {
            PackageManager packageManager = activity.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            if(packageInfo.versionCode==newVerCode&&packageInfo.versionCode!=currentCode){//本地缓存的版本等于服务端的版本
                AppUtils.installApk(new File(path),activity);
            }else {
                File file = new File(path);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
                if(newVerCode>currentCode) {
                    downloadFile(url,saveDir, fileKey);
                }
            }
        }else {
            //NetworkUtil.isWifiEnabled()
            if(path!=null){
                File file = new File(path);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            }
            downloadFile(url,saveDir,fileKey);
        }
    }
    public void downloadFile(String url,final String dirPath,final String fileName) {
        initNotification();
        RetrofitUtils.downLoadFile(BuildConfig.apkUrl,dirPath,fileName).subscribe(new DownLoadSubscriber(BaseApplication.getInstance()) {
            @Override
            protected void _onNext(String path) {
                mNotificationManager.cancel(NOTIFICATION_ID);
                AppUtils.installApk(new File(path),activity);
            }
            @Override
            protected void _onProgress(Integer progress) {
                Log.i("aaaaa","下载进度--》"+progress);
                if(progress%5==0){
                    views.setTextViewText(R.id.notificationTitle, "正在下载");
                    views.setTextViewText(R.id.notificationPercent, "已下载" + progress + "%");
                    views.setProgressBar(R.id.notificationProgress, 100, progress, false);
                    mNotificationManager.notify(NOTIFICATION_ID,notification);
                }
            }

            @Override
            protected void _onError(int errorCode, String msg) {
                mNotificationManager.cancel(NOTIFICATION_ID);
                String saveDir = activity.getApplicationContext().getExternalFilesDir(fileDirName) + File.separator;
                String path =getFilePathByKey(saveDir,fileKey);
                if(!TextUtils.isEmpty(path)){
                    File file = new File(path);
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }
            }
        });
        /*AndroidNetworking.download(url,dirPath,fileName)
                .setTag(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {

                        double percent = (double)bytesDownloaded / totalBytes;
                        DecimalFormat format = new DecimalFormat("0%");
                        String progressStr = format.format(percent);
                        int progress=Integer.parseInt(progressStr.replace("%",""));
                        if(progress%5==0){
                            views.setTextViewText(R.id.notificationTitle, "正在下载");
                            views.setTextViewText(R.id.notificationPercent, "已下载" + progress + "%");
                            views.setProgressBar(R.id.notificationProgress, 100, progress, false);
                            mNotificationManager.notify(NOTIFICATION_ID,notification);
                        }
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        mNotificationManager.cancel(NOTIFICATION_ID);
                        AppUtils.installApk(new File(dirPath+fileName),activity);
                    }
                    @Override
                    public void onError(ANError error) {
                        mNotificationManager.cancel(NOTIFICATION_ID);
                        String saveDir = activity.getApplicationContext().getExternalFilesDir(fileDirName) + File.separator;
                        String path =getFilePathByKey(saveDir,fileKey);
                        if(!TextUtils.isEmpty(path)){
                            File file = new File(path);
                            if (file.isFile() && file.exists()) {
                                file.delete();
                            }
                        }
                    }
                });*/
    }
    // 判断是否更新版本
    public void updateVersion(AppInfo appInfo) {
        isForceUpdate=appInfo.isIsForceUpdate();
        newVerName=appInfo.getVersionName();
        newVerCode=appInfo.getVersionCode();
        int verCode = AppUtils.getAppVersionCode(activity);
            if(!isForceUpdate){
                if (newVerCode > verCode) {
                    doNewVersionUpdate();// 更新版本
                }/*else {
                    notNewVersionUpdate();
                }*/
            }else {
                //强制更新
                if (newVerCode > verCode) {
                    downloadUpdateFile(BuildConfig.apkUrl);
                }/*else {
                    notNewVersionUpdate();
                }*/
            }
    }



    /**
     * 更新版本
     */
    public void doNewVersionUpdate() {
        //int verCode =AppUtils.getAppVersionCode(activity);
        //String verName =AppUtils.getAppVersionName(activity);
        StringBuffer sb = new StringBuffer();
        /*sb.append("当前版本：");
        sb.append(verName);
        sb.append(" Code:");
        sb.append(verCode);*/
        sb.append("发现版本：");
        sb.append(newVerName);
        /*sb.append(" Code:");
        sb.append(newVerCode);*/
        //sb.append(",是否更新");
        Dialog dialog = new AlertDialog.Builder(activity)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 开始下载
                        downloadUpdateFile(BuildConfig.apkUrl);
                    }
                })
                .setNegativeButton("暂不更新",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        // 显示更新框
        dialog.show();
    }


    /**
     * 不更新版本
     */
    public void notNewVersionUpdate() {
        //int verCode = AppUtils.getAppVersionCode(activity);
        String verName = AppUtils.getAppVersionName(activity);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本：");
        sb.append(verName);
        sb.append("\n已是最新版本，无需更新");
        Dialog dialog = new AlertDialog.Builder(activity).setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}
