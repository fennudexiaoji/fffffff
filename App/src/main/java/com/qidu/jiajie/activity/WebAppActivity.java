package com.qidu.jiajie.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.qidu.jiajie.utils.NotificationPermissionUtil;
import com.qidu.jiajie.utils.VersionUpdateUtil;


public class WebAppActivity extends BaseWebActivity{

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void initViewAndEvent() {
        boolean isOpen= NotificationPermissionUtil.isNotificationEnabled(this);
        if(!isOpen){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示")
                    .setMessage("检测到您没有打开通知权限，是否去打开")
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationPermissionUtil.goNotificationSetting(WebAppActivity.this);
                        }
                    }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter); //注册广播接收器
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode== IntentParams.REQUEST_SETTING_NOTIFICATION_PERMISSION&&resultCode==RESULT_OK){
            //通知权限开启成功
        }*/
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                //Toast.makeText(context, "网络打开",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "网络关闭，请打开网络",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
