package com.qidu.jiajie.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.app.base.utils.IntentParams;
import com.google.gson.Gson;
import com.qidu.jiajie.bean.Auth;
import com.qidu.jiajie.bean.WXAuthEntity;
import com.qidu.jiajie.mvp.model.LoginImplAPI;
import com.qidu.jiajie.utils.DataUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;


	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI msgApi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.entry);

		msgApi = WXAPIFactory.createWXAPI(this, null);
		// 将该app注册到微信
		msgApi.registerApp(DataUtils.WECHAT_APP_ID);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例


		msgApi.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		msgApi.handleIntent(intent, this);
	}



	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		//Toast.makeText(this, "openid -----> " + req.openId, Toast.LENGTH_SHORT).show();

		switch (req.getType()) {
			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
				//goToGetMsg();
				break;
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
				//goToShowMsg((ShowMessageFromWX.Req) req);
				break;
			case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
				//Toast.makeText(this, R.string.launch_from_wx, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp baseResp) {
		//Toast.makeText(this, "openid = " + baseResp.openId, Toast.LENGTH_SHORT).show();
		if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//授权code成功
			onRespAuth(baseResp);//拿到了微信返回的code,立马再去请求access_token
		} else if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {//微信分享成功
			onRespSend(baseResp);
		} else{
			//sendAuthData(null);
			finish();
		}

		/*Log.i("aaaa","开放id--->"+resp.openId);
		test(resp.openId);
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
			//Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code, Toast.LENGTH_SHORT).show();
		}

		int result = 0;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			//result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			//result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			//result = R.string.errcode_deny;
			break;
		default:
			//result = R.string.errcode_unknown;
			break;
		}*/

		//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

	private  void getAccessTokenByCode(String code) {
		HashMap<String,String> hashMap=new HashMap<>();
		hashMap.put("appid",DataUtils.WECHAT_APP_ID);
		hashMap.put("secret",DataUtils.WECHAT_SECRET_ID);
		hashMap.put("code",code);
		hashMap.put("grant_type","authorization_code");
		LoginImplAPI.getAccessTokenByCode(hashMap).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers
						.mainThread())
				.unsubscribeOn(Schedulers.io()).subscribe(new Consumer<Auth>() {
			@Override
			public void accept(Auth auth) throws Exception {
				getUserData(auth.access_token, auth.openid);
			}
		}, new Consumer<Throwable>() {
			@Override
			public void accept(Throwable throwable) throws Exception {

			}
		});
	}

	private  void getUserData(String accessToken, String openId) {
		HashMap<String,String> hashMap=new HashMap<>();
		hashMap.put("access_token",accessToken);
		hashMap.put("openid",openId);
		LoginImplAPI.getUserInfoFromWX(hashMap).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers
						.mainThread())
				.unsubscribeOn(Schedulers.io()).subscribe(new Consumer<WXAuthEntity>() {
			@Override
			public void accept(WXAuthEntity auth) throws Exception {
				Gson gson=new Gson();
				String json=gson.toJson(auth);
				Intent intent=new Intent();
				intent.setAction(IntentParams.ACTION_GET_USER_INFO_BY_WX_LOGIN);
				intent.putExtra(IntentParams.KEY_USER_INFO_BY_WX_LOGIN,json);
				sendBroadcast(intent);
				finish();
			}
		}, new Consumer<Throwable>() {
			@Override
			public void accept(Throwable throwable) throws Exception {

			}
		});
	}


	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	private void onRespSend(BaseResp baseResp) {
		finish();
	}
	// supported during Build.OPENID_SUPPORTED_SDK_INT = 0x22000001
	private void onRespAuth(BaseResp baseResp) {
		SendAuth.Resp resultResp= (SendAuth.Resp) baseResp;

		/*String resultStr = "{code:" + resultResp.code + ",state:" + resultResp.state + ",errCode:" + resultResp.errCode + ",type:" + resultResp.getType()+"}" ;
		Toast.makeText(WXEntryActivity.this, resultStr, Toast.LENGTH_SHORT).show();*/
		//Log.i("hhhhh",resultStr);
//        JSONObject resultObj = null;
//        try {
//            resultObj = new JSONObject(str);
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//        }
        /*Intent resultIntent = new Intent(CommonStatic.ACTION_JMT_BIND_ACCOUNT);
        resultIntent.putExtra(CommonStatic.ACCOUNT_AUTH_RESULT, resultStr);
        sendBroadcast(resultIntent);
        finish();*/

		switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				String code = ((SendAuth.Resp) baseResp).code;
				getAccessTokenByCode(code);
				break;
			default:
				//sendAuthData(null);
				Toast.makeText(WXEntryActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
				finish();
				break;
		}
	}





}