package com.qidu.jiajie.utils;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.qidu.jiajie.bean.AuthResult;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

//商户登录 https://e.alipay.com，登录后，选择签约管理
public class AliPayUtil {

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2018110261994103";//2016082901817954

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088511117407636";//2088102172281462
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "1829081637@qq.com";//这里代表的是你支付宝的帐户名！！
    /**
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";
    /**
     * 商户私钥，pkcs8格式
     */
    public static final String RSA_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDQusoyqqHEOQar" +
            "+ANgH85VpoD/ZicX7F6CBB6dqU3gNPiISuJ7hQ0aLorTfDBPE43J/E4Ial9fVw3F" +
            "JF4/HOYVsS5NqVowsIEt5tdrmtWZmJkaOCrKlsaPoJTMdBmsR+Zqt3ldGLLB1J2c" +
            "UFiwtvB+9p/r/ExY4BuPDugfLWfke2vKVgApx/Hl9HmPanBMfbgpDGG7VgqWoNRZ" +
            "c+CWf8+8y0zYHf0BfLWu/8ST6GjqgVro+rp1/aRmZtgAPmagraDycv1/Pcd0xO4h" +
            "UJtYU3lhKHh1d+6olxrvpImM5zCsOTZti9ReCxUD1svNVogLABAPg7f5jkQMyRrL" +
            "AgZsPYZjAgMBAAECggEAGSHUhMM81RaUshIbvfA2dOOmdpoDLQM94ePDF/KxuMRI" +
            "BIkXug2sJTHjb1Fts6fEE8T4VFUShLVDqDAX1PuSzJvX2mwAuwoVk8ROvdsXyvWv" +
            "wZNvu5Lp95M7N17nVHIX0VOkHkyCAYqrK44VD7oHPRJO8SggvTbGurPtyQs7jiwC" +
            "ghJjSSb7JM+SsMQKomoUzaduwpkUyulvQNEKeRHmvvy8QPI9sgVSF51Usk74aQPC" +
            "9iw2R/XvsjdA/bl3Rd0fY9dgXFsZZIrm3hFR30hZCTHjr/viuTEwzR5xRvuS/XYe" +
            "dBQoC1qRxqyCPY1dAbc22wqyDunV7K8AfrQGeP+IGQKBgQDr0j+/fRMlVLeHbQUM" +
            "sm2s5sHpGDCx4bPOqHi3XuFdqEiikoedBifw9gv/Cvg44CtKs9AQGdgkx8C1Ke/U" +
            "b8cJtFPQzI3iOLKNMsygxSMIPQrmcWz9UTXr3IXfzM4tsbIEGGVUVwau+lASvlnf" +
            "xBswIWIeemjiRWM/d2aOdz4V3wKBgQDilxax+iL+ur2YOf1224o2rMpYIR+XcyiU" +
            "lx2B+ZwuP1nHxZ65Yiv8M09QG0M3RoK0TTNYX5QKJGi3dGodSq3oBkOiBFx2dO7f" +
            "rwU60rQdIY4VR8NaSetB4YpblXD2fL8xvgQCn1hSZuKZ+iO7Wau5zXG6Z9LazaEO" +
            "wbsoorE3/QKBgQDWNqqJu4gExSvh7PpFSl+uS/+hScQSEcBP3WGq0JlPhmpUo15l" +
            "GQ53KEpFZyWMJLWuqiA6P//DiWBYMLwW8WKQQipQ5T+NW4t2D8spz2m/NhQMuMQU" +
            "aAzfL/a8EULI5z2Z0r9ZGaSMlfzeyeLCDVXVJr4GiZN01ysMn92dVFwlaQKBgQCd" +
            "niP3Ydx9/UBtZ8lcqJKYPdHzQ/dJzp1n30ZzZ0KxGPhG8rzaj+Ow57BANPaouK+q" +
            "5BjXXIUKGRGOq7g08AHQ6KqbRRVcgTK4/nJQ3MjVisXfH7BQWiMADPfFhUqy60vE" +
            "Oj1n5Zf2mgL+wPWiGVpPQQ3fcOFHuspSDYk3VxSXsQKBgQDAE9l9979pd7WLYLmf" +
            "xO4aVcf4inb0QcldC5sEWAvcndHFd2+6PgPX4MvG0302v9meMdvmvpuEc271+2H/" +
            "qGKWRfTbbGRZYqWo5k0LL5zxQ5X1ZQ59ON5yySwEmJhYs9XNCHPbUVKmIhqAPlYs" +
            "RXGQg+HBNU2YzlKwAyMdzpqpsQ==";

    //private String sign;
    private static Activity activity;
    //构造方法私有化，这样外界就不能访问了
    private AliPayUtil(){
    };
    private static AliPayUtil instance;
    public static AliPayUtil getInstance(Activity mActivity){ //这里就是延时加载的意思
        activity=mActivity;
        if (instance == null){
            instance = new AliPayUtil();
        }
        return  instance;
    }

    private static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }


    /**
     * 支付宝账户授权业务示例
     */
    public void authV2(String responseAuthInfo,final AliPayAuth2CallBack callBack) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            showToast(activity, "错误: 需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID");
            return;
        }

		/*
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
		 *
		 * authInfo 的获取必须来自服务端；
		 */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                final Map<String, String> result = authTask.authV2(authInfo, true);

                Log.i("aaaa","授权结果"+result.toString());
                final AuthResult authResult = new AuthResult((Map<String, String>)result, true);
                final String resultStatus = authResult.getResultStatus();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 判断resultStatus 为“9000”且result_code
                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                        if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                            // 获取alipay_open_id，调支付时作为参数extern_token 的value
                            // 传入，则支付账户为该授权账户
                            //showToast(activity, "授权成功: " + authResult);
                            //requestAppAuthToken(authResult.getAuthCode());
                            if(callBack!=null){
                                JSONObject object=new JSONObject();
                                try {
                                    object.put("auth_code",authResult.getAuthCode());
                                    object.put("alipay_open_id",authResult.getAlipayOpenId());
                                    callBack.authSuccess(object.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            // 其他状态值则为授权失败
                            //showToast(activity, "授权失败: " + authResult);
                            showToast(activity, "授权失败");
                        }
                    }
                });
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    public interface AliPayAuth2CallBack{
        void authSuccess(String result);
    }



    //获取签名等参数
    /*public void requestAlipayAuthParams(){
        AndroidNetworking.get("http://api.localhost.com/getAnUser/{userId}")
                .addPathParameter("userId", "1")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }*/
    /*public void requestAppAuthToken(String authCode){
        AndroidNetworking.get("https://openapi.alipay.com/gateway.do")
                .addQueryParameter("app_id",APPID)
                .addQueryParameter("format","JSON")
                .addQueryParameter("charset","utf-8")
                .addQueryParameter("sign_type","RSA")
                .addQueryParameter("sign","")
                .addQueryParameter("timestamp",""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .addQueryParameter("version","1")
                .addQueryParameter("method","alipay.system.oauth.token")
                .addQueryParameter("grant_type", "authorization_code")
                .addQueryParameter("code",authCode)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //{"error_response":{"code":"40001","msg":"Missing Required Arguments","sub_code":"isv.missing-signature","sub_msg":"缺少签名参数"}}
                        Log.i("aaaaa","获取app_auth_token---"+response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("aaaaa","失败"+anError.getErrorBody());
                    }
                });
    }*/
    //获取app_auth_token  https://docs.open.alipay.com/api_9/alipay.system.oauth.token
    /*public void requestAppAuthToken(String authCode,String authResult){
        Log.i("aaaaa","参数----》"+authCode);
        AndroidNetworking.get("https://openapi.alipay.com/gateway.do?"+authResult+)
                //.addQueryParameter("appid", "authorization_code")
                //.addQueryParameter("grant_type", "authorization_code")
                //.addQueryParameter("code",authCode)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaaaa","获取app_auth_token---"+response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("aaaaa","失败"+anError.getErrorBody());
                    }
                });
    }*/

    //获取用户信息 https://docs.open.alipay.com/api_2/alipay.user.info.share
    /*public void requestAppUserInfo(String authCode){
        AndroidNetworking.get("https://openapi.alipay.com/gateway.do/alipay.open.auth.token.app")
                .addQueryParameter("grant_type", "authorization_code")
                //.addQueryParameter("code",authCode)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaaaaa","获取用户信息-->"+response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }*/

}
