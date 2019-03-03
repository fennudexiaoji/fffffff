package com.qidu.jiajie.plugin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.amap.api.location.AMapLocation;
import com.app.base.utils.IntentParams;
import com.common.lib.global.PermissionUtils;
import com.common.lib.utils.SharedPreferencesUtils;
import com.liang.scancode.utils.Constant;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.BaseWebActivity;
import com.qidu.jiajie.activity.PoiLocationActivity;
import com.qidu.jiajie.activity.QRCodeScanActivity;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.utils.AliPayUtil;
import com.qidu.jiajie.utils.DataUtils;
import com.qidu.jiajie.utils.MyAMapLocationUtil;
import com.qidu.jiajie.utils.RequestPermissionsResultListener;
import com.qidu.jiajie.utils.WXShareUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import me.leolin.shortcutbadger.ShortcutBadger;

@SuppressWarnings("unused")
public class AndroidCallObject extends Object implements RequestPermissionsResultListener {


    private WebView webView;
    private BaseWebActivity activity;
    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_MULTI_PERMISSION:
                    startLocationCurrent();
                     break;
                    case PermissionUtils.CODE_CAMERA:
                        Intent intent=new Intent(activity, QRCodeScanActivity.class);
                        intent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_QRCODE_MODE);
                        activity.startActivityForResult(intent,QRCodeScanActivity.QR_REQUEST_CODE);
                     break;
                default:
                    break;
            }
        }
    };


    public RequestPermissionsResultListener getPermissionsResultListener(){
        return this;
    }
    private void startLocationCurrent(){
        MyAMapLocationUtil.getSingleton().startLocationCurrentPosition(new MyAMapLocationUtil.LocationCallBack() {
            @Override
            public void locationSuccess(AMapLocation location) {

                final JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("provinceName",location.getProvince());
                    jsonObject.put("cityName",location.getCity());
                    jsonObject.put("cityCode",location.getCityCode());
                    jsonObject.put("adCode",location.getAdCode());
                    jsonObject.put("district",location.getDistrict()+"");
                    jsonObject.put("street",location.getStreet());
                    jsonObject.put("number",location.getStreetNum());
                    jsonObject.put("title",location.getPoiName()+"");
                    jsonObject.put("latitude",location.getLatitude());
                    jsonObject.put("longitude",location.getLongitude());
                    jsonObject.put("address",location.getAddress());

                    //SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(CommonKey.KEY_SELECT_LOCATION_INFO,jsonObject.toString());
                    //Log.d("aaaaaaa",jsonObject.toString());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:locationSuccess("+jsonObject.toString()+")");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void locationFailure() {

            }
        });
    }
    public AndroidCallObject(BaseWebActivity activity, WebView webView) {
        this.activity=activity;
        this.webView = webView;
    }
    //新开一个窗口
    @JavascriptInterface
    public void openNewWindow(String json){
        if(!TextUtils.isEmpty(json)){
            try {
                JSONObject object=new JSONObject(json);
                if(object.has("url")){
                    String url=object.getString("url");
                    Intent intent=new Intent(activity, WebAppActivity.class);
                    intent.putExtra(IntentParams.WEB_URL,url);
                    activity.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //searchAddressByPoiSuccess(json)
    @JavascriptInterface
    public void searchAddressByPoi(String jsonStr){
        Intent intent=new Intent(activity, PoiLocationActivity.class);
        activity.startActivityForResult(intent,0x678);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(activity, requestCode, permissions, grantResults, mPermissionGrant);
    }

    //js调用  android.locationCurrentPosition
    @JavascriptInterface
    public void locationCurrentPosition(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] requestPermissions = {
                        PermissionUtils.PERMISSION_ACCESS_FINE_LOCATION,
                        PermissionUtils.PERMISSION_ACCESS_COARSE_LOCATION
                };
                PermissionUtils.requestMultiPermissions(null,activity,requestPermissions,mPermissionGrant);
            }
        });
    }

    //微信分享链接
    @JavascriptInterface
    public void shareToThirdApp(String msg) {
        try {
            JSONObject jsonObject=new JSONObject(msg);
            if(jsonObject.has("whatTypeShare")){
                if(jsonObject.getString("whatTypeShare").equals("wx")){
                    if(jsonObject.getString("shareType").equals("url")){
                        String title=jsonObject.getString("title");
                        String content=jsonObject.getString("content");
                        if(title==null){
                            title="";
                        }
                        if(TextUtils.isEmpty(content)){
                            content="";
                        }
                        String imgUrl= null;
                        try {
                            if(jsonObject.has("imgurl")){
                                imgUrl = jsonObject.getString("imgurl");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        WXShareUtil.ShareContentWebpage shareWeb=new WXShareUtil.ShareContentWebpage(title,content,jsonObject.getString("shareContent"), R.drawable.icon_app,imgUrl);
                        //发给朋友 WXShareUtil.WEIXIN_SHARE_TYPE_TALK 分享到朋友圈 WEIXIN_SHARE_TYPE_FRENDS
                        if(jsonObject.getString("whoToShare").equals("talk")){
                            WXShareUtil.getInstance(activity).shareByWeixin(shareWeb,WXShareUtil.WEIXIN_SHARE_TYPE_TALK);
                        }else if(jsonObject.getString("whoToShare").equals("friends")){
                            WXShareUtil.getInstance(activity).shareByWeixin(shareWeb,WXShareUtil.WEIXIN_SHARE_TYPE_FRENDS);
                        }
                    }else if(jsonObject.getString("shareType").equals("text")){
                        WXShareUtil.ShareContentText shareText=new WXShareUtil.ShareContentText(jsonObject.getString("shareContent"));
                        if(jsonObject.getString("whoToShare").equals("talk")){
                            WXShareUtil.getInstance(activity).shareByWeixin(shareText,WXShareUtil.WEIXIN_SHARE_TYPE_TALK);
                        }else if(jsonObject.getString("whoToShare").equals("friends")){
                            WXShareUtil.getInstance(activity).shareByWeixin(shareText,WXShareUtil.WEIXIN_SHARE_TYPE_FRENDS);
                        }
                    }else if(jsonObject.getString("shareType").equals("pic")){
                        WXShareUtil.ShareContentPic sharePic;
                        if(!jsonObject.getString("shareContent").isEmpty()){
                            sharePic=new WXShareUtil.ShareContentPic(jsonObject.getString("shareContent"));
                        }else {
                            sharePic=new WXShareUtil.ShareContentPic(R.drawable.icon_app);
                        }
                        if(jsonObject.getString("whoToShare").equals("talk")){
                            WXShareUtil.getInstance(activity).shareByWeixin(sharePic,WXShareUtil.WEIXIN_SHARE_TYPE_TALK);
                        }else if(jsonObject.getString("whoToShare").equals("friends")){
                            WXShareUtil.getInstance(activity).shareByWeixin(sharePic,WXShareUtil.WEIXIN_SHARE_TYPE_FRENDS);
                        }
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //WXShareUtil.ShareContentWebpage shareText=new WXShareUtil.ShareContentWebpage("链接标题","分享内容","http://www.baidu.com",R.drawable.icon_app);
        //发给朋友 WXShareUtil.WEIXIN_SHARE_TYPE_TALK 分享到朋友圈 WEIXIN_SHARE_TYPE_FRENDS
        //WXShareUtil.getInstance(activity).shareByWeixin(shareWeb,WXShareUtil.WEIXIN_SHARE_TYPE_FRENDS);
    }




    @JavascriptInterface
    public void androidFinish(String msg) {
        try {
            JSONObject jsonObject=new JSONObject(msg);
            boolean isArrowFinish=jsonObject.getBoolean("isArrowFinish");
            activity.setIsFinish(isArrowFinish);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //微信授权登录
    @JavascriptInterface
    public void wxAuthorizationLogin() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IWXAPI api = WXAPIFactory.createWXAPI(activity, DataUtils.WECHAT_APP_ID);
                api.registerApp(DataUtils.WECHAT_APP_ID);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wx_login";
                api.sendReq(req);
            }
        });

    }

    //支付宝授权登录
    @JavascriptInterface
    public void alipayAuth2(){
        AliPayUtil.getInstance(activity).authV2("", new AliPayUtil.AliPayAuth2CallBack() {
            @Override
            public void authSuccess(String result) {
                webView.loadUrl("javascript:aliPayAuthSuccess("+result+")");
            }
        });
    }

    //二维码扫描
    @JavascriptInterface
    public void callQRCodeScan(){
        PermissionUtils.requestPermission(activity,PermissionUtils.CODE_CAMERA,mPermissionGrant);

    }



    @JavascriptInterface
    public void unReadMessageCount(String msg) {
        try {
            JSONObject jsonObject=new JSONObject(msg);
            int unReadCount=jsonObject.getInt("unReadCount");
            ShortcutBadger.applyCount(activity,unReadCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void loginSuccess(String msg) {
        try {
            JSONObject jsonObject=new JSONObject(msg);
            String userId=jsonObject.getString("userId");
            //SharedPreferencesUtils.getInstance(activity).saveData(IntentParams.KEY_IS_LOGIN,true);
            //Log.d("aaaaa","用户id---->"+userId);
            String oldUserId= (String) SharedPreferencesUtils.getInstance(activity).getData(IntentParams.KEY_USER_ID,"");
            if(!userId.equals(oldUserId)){
                SharedPreferencesUtils.getInstance(activity).saveData(IntentParams.KEY_USER_ID,userId);
                JPushInterface.setAlias(activity,1,userId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void loginOut(){
        SharedPreferencesUtils.getInstance(activity).saveData(IntentParams.KEY_USER_ID,"");
        JPushInterface.deleteAlias(activity,1);
    }
}
