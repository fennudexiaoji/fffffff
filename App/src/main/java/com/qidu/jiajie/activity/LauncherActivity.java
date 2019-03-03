package com.qidu.jiajie.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.app.base.utils.IntentParams;
import com.common.lib.base.AbsBaseActivity;
import com.common.lib.base.BaseActivity;
import com.common.lib.fileutils.FileUtils;
import com.common.lib.global.CrashHandler;
import com.common.lib.global.PermissionUtils;
import com.qidu.jiajie.AppApplication;
import com.qidu.jiajie.MainActivity;
import com.qidu.jiajie.R;



public class LauncherActivity extends AbsBaseActivity {
    private Handler handler=new Handler();

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_MULTI_PERMISSION:
                    init();
                    break;
                default:
                    break;
            }
        }
    };
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        hideBottomUIMenu();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher);
        initViewsAndEvents(savedInstanceState);


    }
    @Override
    public void finish() {
        super.finish();
    }
    private void init(){
        if(FileUtils.checkFileDirectory(this)){
            FileUtils.initCacheFile(getApplicationContext());
        }

        // 异常处理，不需要处理时注释掉这两句即可！
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());

        if(AppApplication.isFirst){
            AppApplication.isFirst=false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle=getIntent().getBundleExtra(IntentParams.KEY_BUNDLE);
                    Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if(bundle!=null){
                        String url=bundle.getString(IntentParams.WEB_URL);
                        if(!TextUtils.isEmpty(url)){
                            intent.putExtra(IntentParams.WEB_URL,url);
                        }
                    }
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                }
            }, 2000);
        }else {
            Bundle bundle=getIntent().getBundleExtra(IntentParams.KEY_BUNDLE);
            Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if(bundle!=null){
                String url=bundle.getString(IntentParams.WEB_URL);
                if(!TextUtils.isEmpty(url)){
                    intent.putExtra(IntentParams.WEB_URL,url);
                }
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        //一次申请多个权限
        String[] requestPermissions = {
                PermissionUtils.PERMISSION_ACCESS_FINE_LOCATION,
                PermissionUtils.PERMISSION_ACCESS_COARSE_LOCATION,
                PermissionUtils.PERMISSION_READ_EXTERNAL_STORAGE,
                PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE,
                PermissionUtils.PERMISSION_CAMERA,
                PermissionUtils.PERMISSION_READ_SMS,
                PermissionUtils.PERMISSION_READ_PHONE_STATE
        };
        PermissionUtils.requestMultiPermissions(null,this,requestPermissions,mPermissionGrant);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
