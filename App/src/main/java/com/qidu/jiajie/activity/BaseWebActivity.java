package com.qidu.jiajie.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.common.lib.base.AbsBaseActivity;
import com.common.lib.fileutils.FileUtils;
import com.common.lib.global.AppGlobal;
import com.common.lib.utils.SharedPreferencesUtils;
import com.qidu.jiajie.AppApplication;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.broadcast.SMSBroadcastReceiver;
import com.qidu.jiajie.plugin.AndroidCallObject;
import com.qidu.jiajie.utils.DataUtils;
import com.qidu.jiajie.utils.MyAMapLocationUtil;
import com.qidu.jiajie.utils.PermissionUtil;
import com.qidu.jiajie.utils.PhotoUtils;
import com.qidu.jiajie.utils.RequestPermissionsResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseWebActivity extends AbsBaseActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10002;
    private static final int PICK_ACTIVITY_REQUEST_CODE = 10003;
    private static final int CROP_ACTIVITY_REQUEST_CODE = 10008;
    private String imageFilePath; //拍照和选择照片后图片路径
    private File cropFile; //裁剪后的图片文件
    //分别是4.4之前的回调参数 ，
    private ValueCallback<Uri> mUploadMsg;
    // 5.0之后的回调参数
    public ValueCallback<Uri[]> mUploadMsgForAndroid5;
    // permission Code
    private static final int P_CODE_PERMISSIONS = 101;
    private RequestPermissionsResultListener permissionsResultListener;

    private WebView webView;
    private View titleLayout,goBack,finish,webErrorLayout;
    private TextView reloadBtn,testTip;
    private TextView titleCenter,errorDescribe;
    //private Boolean showTitleLayout=false;
    private ProgressBar progressBar;
    //定义支付域名（替换成公司申请H5的域名即可）
    BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(IntentParams.ACTION_GET_USER_INFO_BY_WX_LOGIN)){
                String response=intent.getStringExtra(IntentParams.KEY_USER_INFO_BY_WX_LOGIN);
                if(webView!=null){
                    webView.loadUrl("javascript:wxAuthorizationLoginSuccess("+response+")");
                }
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        testTip= (TextView) findViewById(R.id.test_tip);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        titleLayout=findViewById(R.id.title_layout);
        errorDescribe= (TextView) findViewById(R.id.error_describe);
        reloadBtn= (TextView) findViewById(R.id.reloadBtn);
        webErrorLayout=findViewById(R.id.web_error_layout);
        goBack=findViewById(R.id.go_back);
        finish=findViewById(R.id.finish);
        titleCenter= (TextView) findViewById(R.id.title_center);
        webView= (WebView) findViewById(R.id.web_view);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentParams.ACTION_GET_USER_INFO_BY_WX_LOGIN);
        registerReceiver(broadcastReceiver, intentFilter);
        initSmsReader();
        initWebView();
        String url=getIntent().getStringExtra(IntentParams.WEB_URL);

        if(!TextUtils.isEmpty(url)){//点击通知栏打开链接
            //showTitleLayout=true;
            //titleLayout.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(AppGlobal.getToken())){
                Map<String, String> extraHeaders = new HashMap<>();
                extraHeaders.put("X-Token",AppGlobal.getToken());
                webView.loadUrl(url, extraHeaders);
            }else {
                webView.loadUrl(url);
            }
        }else {
            //titleLayout.setVisibility(View.GONE);
            /*DataUtils.synCookies(this,webView,HttpUrl.DEFAULT_HOST);
            CookieManager cookieManager = CookieManager.getInstance();
            String newCookie = cookieManager.getCookie(HttpUrl.DEFAULT_HOST);
            LogUtils.i("同步后cookie---", newCookie);*/
            if(!TextUtils.isEmpty(AppGlobal.getToken())){
                Map<String, String> extraHeaders = new HashMap<>();
                extraHeaders.put("X-Token",AppGlobal.getToken());//商户申请H5时提交的授权域名
                webView.loadUrl(HttpUrl.TEST_HOST, extraHeaders);
            }else {
                webView.loadUrl(BuildConfig.DEFAULT_HOST);
            }
            //dialogShow2();
            /*String urlStr= (String) SharedPreferencesUtils.getInstance(this).getData(IntentParams.WEB_URL,"");
            if(!TextUtils.isEmpty(urlStr)){
                testTip.setVisibility(View.VISIBLE);
                webView.loadUrl(urlStr);
                if(urlStr.equals(HttpUrl.DEFAULT_HOST)){
                    testTip.setText("正式环境");
                }else {
                    testTip.setText("测试环境");
                }
            }else {
                showConfigAddressDialog(this);
            }*/
        }

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mookKeyBack();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
                webErrorLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });
        initViewAndEvent();

    }
    public void showConfigAddressDialog(final Context context){
        final String[] items = new String[]{BuildConfig.DEFAULT_HOST, HttpUrl.TEST_HOST};//创建item
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("选择您运行环境链接")
                .setIcon(R.drawable.icon_app)
                .setCancelable(false)
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        SharedPreferencesUtils.getInstance(context).saveData(IntentParams.WEB_URL,items[i]);
                        webView.loadUrl(items[i]);
                        testTip.setVisibility(View.VISIBLE);
                        if(i==0){
                            testTip.setText("正式环境");
                        }else {
                            testTip.setText("测试环境");
                        }
                    }
                })
                /*.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })*/
                .create();
        alertDialog.show();
    }
    private void dialogShow2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_url_input, null);
        final EditText content = (EditText) v.findViewById(R.id.dialog_content);
        String urlStr= (String) SharedPreferencesUtils.getInstance(this).getData(IntentParams.WEB_URL,"");
        if(!TextUtils.isEmpty(urlStr)){
            content.setText(urlStr);
        }
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!content.getText().toString().startsWith("http")){
                    Toast.makeText(getApplicationContext(),"请输入地址",Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                SharedPreferencesUtils.getInstance(getApplicationContext()).saveData(IntentParams.WEB_URL,content.getText().toString());
                webView.loadUrl(content.getText().toString());
            }
        });
    }
    protected abstract void initViewAndEvent();

    public void loadWebUrl(String webUrl){
        if(webView!=null&&!TextUtils.isEmpty(webUrl)){
            webView.loadUrl(webUrl);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if(webView!=null){
            webView.saveState(outState);
        }
    }

    // 模拟点击回退键盘
    public void mookKeyBack() {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /*Android中webView加载H5时,若相关需要的方法配置好后,仍然出现H5页面在不同的手机上出现文字或者图标类似换行的现象,
        在加载Webview的Activity里面重写以下的方法即可:*/
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
            Configuration config=new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config,res.getDisplayMetrics() );
        }
        return res;
    }

    private void initWebView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().acceptThirdPartyCookies(webView);
        }
        CookieManager.getInstance().acceptCookie();
        CookieManager.getInstance().setAcceptCookie(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            webView.setWebContentsDebuggingEnabled(true);
        }
        String userAgent = webView.getSettings().getUserAgentString();//找到webview的useragent
        webView.getSettings().setUserAgentString(userAgent.replace("Android", "APP_WEBVIEW Android"));
        //通过浏览器下载webview的资源
        //webView.setDownloadListener(new WebViewDownLoadListener());
        //对于不需要使用 file 协议的应用，禁用 file 协议；
        /*webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowFileAccessFromFileURLs(false);*/
        webView.getSettings().setAllowFileAccess(true);// 可以读取文件缓存
        webView.getSettings().setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        // 开启H5(APPCache)缓存功能
        webView.getSettings().setAppCacheEnabled(true);
        // 应用可以有数据库
        webView.getSettings().setDatabaseEnabled(true);
        //webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开浏览器新窗口
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /*if (HttpUrl.DEFAULT_HOST.endsWith(".html")) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }*/
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        //支持放大缩小
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //设置放大缩小
        webView.getSettings().setBuiltInZoomControls(true);
        //隐藏放大缩小的控件
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        AndroidCallObject androidCallObject=new AndroidCallObject(this,webView);
        permissionsResultListener=androidCallObject.getPermissionsResultListener();
        webView.addJavascriptInterface(androidCallObject,"android");

    }


    protected boolean isFinish=false;
    public void setIsFinish(boolean isFinish){
        //ToastUtils.show("回到可以不可以后退的页面"+isFinish);
        this.isFinish=isFinish;
    }
    /*private long exitTime = 0;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode()==4&&isFinish){
            if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                if((System.currentTimeMillis()-exitTime) > 2000){
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    AppApplication.getInstance().exit();
                }
                return false;
            }
            return false;//表示不分发
        }else{
            return super.dispatchKeyEvent(event);
        }

    }*/

    /*@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (webView.isCustomViewShowing() && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)) {
            return webView.onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if(webView!=null) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
    private WebChromeClient webChromeClient=new WebChromeClient(){
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定", null)
                    .setCancelable(false)
                    .create()
                    .show();
            result.confirm();
            return true;
        }
        @Override
        public boolean onJsConfirm(WebView view, String url, String message,final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            })
             .create()
             .show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (title.contains("404") || title.contains("System Error")){
                webErrorLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }else if(/*showTitleLayout&&*/title!=null){
                titleCenter.setText(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);

        }

        //For Android  >= 4.1
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            mUploadMsg = valueCallback;
            showOptions();
        }
        //For Android 5.0+
        @Override
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                FileChooserParams fileChooserParams){
            if (mUploadMsgForAndroid5 != null) {
                mUploadMsgForAndroid5.onReceiveValue(null);
                mUploadMsgForAndroid5 = null;
            }
            mUploadMsgForAndroid5 = filePathCallback;
            showOptions();
            return true;
        }

        //定位相关
        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onPermissionRequest(PermissionRequest request) {
            super.onPermissionRequest(request);
        }

        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            super.onPermissionRequestCanceled(request);
        }
    };

    //https://github.com/DdongGua/UPloadimage-master
    //弹出选择图片的dialog
    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new DialogOnCancelListener());
        //alertDialog.setTitle("请选择操作");
        // gallery, camera.
        String[] options = {"相册", "拍照"};
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if (PermissionUtil.isOverMarshmallow()) {
                        if (!PermissionUtil.isPermissionValid(BaseWebActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            Toast.makeText(BaseWebActivity.this,
                                    "请去\"设置\"中开启本应用的图片媒体访问权限",
                                    Toast.LENGTH_SHORT).show();

                            restoreUploadMsg();
                            requestPermissionsAndroidM();
                            return;
                        }

                    }
                    //都要执行的代码 ，去打开系统相册
                    try {
                        PhotoUtils.pickPhoto(BaseWebActivity.this,PICK_ACTIVITY_REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(BaseWebActivity.this,
                                "请去\"设置\"中开启本应用的图片媒体访问权限",
                                Toast.LENGTH_SHORT).show();
                        restoreUploadMsg();
                    }

                } else {
                    if (PermissionUtil.isOverMarshmallow()) {
                        if (!PermissionUtil.isPermissionValid(BaseWebActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(BaseWebActivity.this,
                                    "请去\"设置\"中开启本应用的图片媒体访问权限",
                                    Toast.LENGTH_SHORT).show();

                            restoreUploadMsg();
                            requestPermissionsAndroidM();
                            return;
                        }

                        if (!PermissionUtil.isPermissionValid(BaseWebActivity.this, Manifest.permission.CAMERA)) {
                            Toast.makeText(BaseWebActivity.this,
                                    "请去\"设置\"中开启本应用的相机权限",
                                    Toast.LENGTH_SHORT).show();

                            restoreUploadMsg();
                            requestPermissionsAndroidM();
                            return;
                        }
                    }

                    try {
                        File imageFile;
                        imageFile= FileUtils.createImageFile(getApplicationContext());
                        imageFilePath = imageFile.getPath();
                        PhotoUtils.takePhoto(BaseWebActivity.this,imageFile,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(BaseWebActivity.this,
                                "请去\"设置\"中开启本应用的相机和图片媒体访问权限",
                                Toast.LENGTH_SHORT).show();

                        restoreUploadMsg();
                    }
                }
            }
        });
        alertDialog.show();
    }
    private WebViewClient webViewClient=new WebViewClient(){

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i("aaaaaaa","onPageStarted--->"+url);
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view,url);
            String token=AppGlobal.getToken();
            if(url.startsWith("http://jiajie-touch")&&!TextUtils.isEmpty(token)){
                //一定要在onPageFinished方法中写入才有效
                String key_token = "token";
                String key_user_id="user_id";
                //user_id
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("window.localStorage.setItem('"+ key_token +"','"+ AppGlobal.getToken() +"');", null);
                    webView.evaluateJavascript("window.localStorage.setItem('"+ key_user_id +"','"+ AppGlobal.getUserId() +"');", null);
                } else {
                    webView.loadUrl("javascript:localStorage.setItem('"+ key_token +"','"+ AppGlobal.getToken() +"');");
                    webView.loadUrl("javascript:localStorage.setItem('"+ key_user_id +"','"+ AppGlobal.getUserId() +"');");
                }
            }
            Log.i("aaaaa","token--->"+AppGlobal.getToken());
            Log.i("aaaaa","userId--->"+AppGlobal.getUserId());
        }

        //处理https请求
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }
        //7.0以上系统访问
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                String url=request.getUrl().toString();
                //view.loadUrl(request.getUrl().toString());
                return onShouldOverrideUrlLoading(view,url);
            } else {
                //view.loadUrl(request.toString());
                return onShouldOverrideUrlLoading(view,request.toString());
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return onShouldOverrideUrlLoading(view,url);
        }

        // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //Log.e(TAG, "onReceivedError: ----url:" + error.getDescription());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return;
            }
            webErrorLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }

        // 新版本，只会在Android6及以上调用
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            webErrorLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }

    };

    private boolean onShouldOverrideUrlLoading(WebView view, String url){
        Log.i("aaaaaaa","hahhahahh--->"+url);
        // 如下方案可在非微信内部WebView的H5页面中调出微信支付
        //微信H5支付核心代码
        if (url.startsWith("weixin://wap/pay?")||url.startsWith("alipays:") || url.startsWith("alipay")|| url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"未安装客户端",Toast.LENGTH_SHORT).show();
                return false;
            }
        }/*else if(url.startsWith("alipays:") || url.startsWith("alipay")){
            //推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
            final PayTask task = new PayTask(BaseWebActivity.this);
            boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                @Override
                public void onPayResult(final H5PayResultModel result) {
                    final String returnUrl = result.getReturnUrl();
                    if (!TextUtils.isEmpty(returnUrl)) {
                        BaseWebActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl(returnUrl);
                            }
                        });
                    }
                }
            });
            //判断是否成功拦截 若成功拦截，则无需继续加载该URL；否则继续加载
            if (!isIntercepted) {
                view.loadUrl(url);
            }
            return true;
        }*/ else if(url.equals("http://jiajie-touch-v2.7dugo.com/#/login")){

            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }else {
            Map<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("Referer", BuildConfig.DEFAULT_HOST);//商户申请H5时提交的授权域名
            view.loadUrl(url, extraHeaders);
        }
        return true;
    }
    /*mWebView.evaluateJavascript("javascript:getSaveAccount()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    mIsSavePass = value.equals("true");
                }
            });*/
    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            webView.pauseTimers();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);

        unregisterReceiver(broadcastReceiver);
        MyAMapLocationUtil.getSingleton().destroyLocation();
        if (webView != null) {
            webView.removeAllViews();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                webView.removeJavascriptInterface("android");
            }
            webView.destroy();
        } else {
            finish();
        }
    }



    /**
     * 判断当前是否有网络连接,但是如果该连接的网络无法上网，也会返回true
     * @param mContext
     * @return
     */
    public static boolean isNetConnection(Context mContext) {
        if (mContext!=null){
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean connected = networkInfo.isConnected();
            if (networkInfo!=null&&connected){
                if (networkInfo.getState()== NetworkInfo.State.CONNECTED){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(resultCode== PoiLocationActivity.resultCode){
            if(data==null){
                return;
            }
            String json=data.getStringExtra(IntentParams.KEY_ADDRESS_INFO);
            webView.loadUrl("javascript:searchAddressByPoiSuccess("+json+")");
        }else if(resultCode== QRCodeScanActivity.QR_RESULT_CODE){
            if(data==null){
                return;
            }
            String result=data.getStringExtra(IntentParams.KEY_QR_CODE_SCAN_RESULT_VALUE);
            JSONObject object=new JSONObject();
            try {
                object.put("scan_result",result);
                webView.loadUrl("javascript:onScanResultSuccess("+object.toString()+")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(resultCode != Activity.RESULT_OK){
            restoreUploadMsg();
        }
        if(resultCode == Activity.RESULT_OK&&requestCode==CROP_ACTIVITY_REQUEST_CODE){
            try {
                //如果当前手机版本小于5.0
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    //如果回调参数没有声明，那么就返回
                    if (mUploadMsg == null) {
                        return;
                    }
                    if (data != null) {
                        Uri uri = Uri.fromFile(cropFile);
                        mUploadMsg.onReceiveValue(uri);
                    }
                    mUploadMsg=null;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (mUploadMsgForAndroid5 == null) {        // for android 5.0+
                        return;
                    }
                    if (data != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                            String authority = getApplicationContext().getPackageName() + ".provider";
                            Uri uri = FileProvider.getUriForFile(getApplicationContext(), authority, cropFile);
                            mUploadMsgForAndroid5.onReceiveValue(new Uri[]{uri});
                        }else {
                            Uri uri = Uri.fromFile(cropFile);
                            mUploadMsgForAndroid5.onReceiveValue(new Uri[]{uri});
                        }
                    }
                    mUploadMsgForAndroid5=null;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(resultCode == Activity.RESULT_OK&&requestCode==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(imageFilePath==null){
                return;
            }
            try {
                Uri outputUri = null;
                Uri imageUri=null;
                cropFile=FileUtils.createImageFile(getApplicationContext());
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    String authority = getApplicationContext().getPackageName() + ".provider";
                    outputUri = FileProvider.getUriForFile(getApplicationContext(), authority,cropFile);
                    imageUri = FileProvider.getUriForFile(getApplicationContext(), authority,new File(imageFilePath));
                } else {
                    outputUri = Uri.fromFile(cropFile);
                    imageUri = Uri.fromFile(new File(imageFilePath));
                }
                PhotoUtils.cropImageUri(BaseWebActivity.this,outputUri,imageUri,CROP_ACTIVITY_REQUEST_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(resultCode == Activity.RESULT_OK&&requestCode==PICK_ACTIVITY_REQUEST_CODE){
            //从相册选择
            if (data != null && resultCode == Activity.RESULT_OK) {
                try {
                    Uri outputUri = null;
                    Uri imageUri=null;
                    cropFile=FileUtils.createImageFile(getApplicationContext());
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                        String authority = getApplicationContext().getPackageName() + ".provider";
                        outputUri = FileProvider.getUriForFile(getApplicationContext(), authority,cropFile);
                            /*imageUri = data.getData();
                            Log.i("aaaaa","--->"+imageUri.getPath());
                            imageUri = FileProvider.getUriForFile(this,authority, new File(imageUri.getPath()));*/
                        //imageUri = FileProvider.getUriForFile(getApplicationContext(), authority,new File(imageFilePath));
                        //Log.i("aaaaa","cropImageUri--->"+imageFilePath);
                        //9.0
                        imageUri = data.getData();
                    } else {
                        outputUri = Uri.fromFile(cropFile);
                        imageUri = data.getData();
                    }
                    PhotoUtils.cropImageUri(BaseWebActivity.this,outputUri,imageUri,CROP_ACTIVITY_REQUEST_CODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }




    private class DialogOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            restoreUploadMsg();
        }
    }

    //如果用户点击了取消按钮，就让存储图片的参数为空，?H5就不会做后续的操作了
    private void restoreUploadMsg() {
        if (mUploadMsg != null) {
            mUploadMsg.onReceiveValue(null);
            mUploadMsg = null;

        } else if (mUploadMsgForAndroid5 != null) {
            mUploadMsgForAndroid5.onReceiveValue(null);
            mUploadMsgForAndroid5 = null;
        }
    }


    //6.0以上动态申请权限的逻辑
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==P_CODE_PERMISSIONS){
            requestResult(permissions, grantResults);
            restoreUploadMsg();
        }else {
            if(permissionsResultListener!=null){
                permissionsResultListener.onRequestPermissionsResult(requestCode,permissions,grantResults);
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermissionsAndroidM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> needPermissionList = new ArrayList<>();
            needPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.CAMERA);

            PermissionUtil.requestPermissions(BaseWebActivity.this, P_CODE_PERMISSIONS, needPermissionList);

        } else {
            return;
        }
    }

    public void requestResult(String[] permissions, int[] grantResults) {
        ArrayList<String> needPermissions = new ArrayList<String>();

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (PermissionUtil.isOverMarshmallow()) {

                    needPermissions.add(permissions[i]);
                }
            }
        }

        if (needPermissions.size() > 0) {
            StringBuilder permissionsMsg = new StringBuilder();

            for (int i = 0; i < needPermissions.size(); i++) {
                String strPermissons = needPermissions.get(i);

                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_white_external_hint));

                } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_read_external_hint));

                } else if (Manifest.permission.CAMERA.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_camera_hint));

                }
            }

            String strMessage = "请允许使用\"" + permissionsMsg.substring(1).toString() + "\"权限, 以正常使用APP的所有功能.";

            Toast.makeText(BaseWebActivity.this, strMessage, Toast.LENGTH_SHORT).show();

        } else {
            return;
        }
    }



    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private void initSmsReader(){
        final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(SMS_RECEIVED_ACTION);
        //设置优先级
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("code",message);
                    //在相应的编辑框中填写验证码
                    webView.loadUrl("javascript:onSMSReceiveSuccess("+jsonObject.toString()+")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
