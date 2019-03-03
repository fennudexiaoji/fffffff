package com.qidu.jiajie.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static MessageListener mMessageListener;

    public SMSBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for(Object pdu:pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte [])pdu);
                String sender = smsMessage.getDisplayOriginatingAddress();
                //短信内容
                String content = smsMessage.getDisplayMessageBody();
                //Log.i("aaaaaaa",sender+"短信内容："+content);
                //106598731短信内容：【中国电信】您正在登录“电信营业厅”客户端http://a.189.cn/JJhy2C，本次登录随机验证码：956984，确保本人操作，验证码切勿告知他人，以免隐私泄露，此验证码3分钟有效。
                long date = smsMessage.getTimestampMillis();
                Date tiemDate = new Date(date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(tiemDate);

                //过滤不需要读取的短信的发送号码，博主这里用的是关键词过滤，小伙伴们也可以用电话号去过滤
                /*if (content.contains("***")&&content.contains("****")
                        &&content.contains("***")) {
                    //从短信内容中获取验证码
                    String code=getCode(content,6);
                    Log.i("aaaaaaa",sender+"短信内容："+content);
                    //回调数据
                    mMessageListener.onReceived(code);
                    abortBroadcast();
                }*/
                if (content.contains("【帮家洁】")) {
                    //从短信内容中获取验证码
                    String code=getCode(content,4);
                    //Log.i("aaaaaaa","短信验证码："+code);
                    //回调数据
                    mMessageListener.onReceived(code);
                    abortBroadcast();
                }
            }
        }

    }

    //回调接口
    public interface MessageListener {
        public void onReceived(String message);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }

    public static String getCode(String body, int YZMLENGTH) {
        // 首先([a-zA-Z0-9]{YZMLENGTH})是得到一个连续的YZMLENGTH位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{YZMLENGTH})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{YZMLENGTH})后面不能有数字出现


        // 获得数字字母组合
        //  Pattern p = Pattern  .compile("(?<![0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");

        // 获得纯数字
        Pattern p = Pattern.compile("(?<![0-9])([0-9]{" + YZMLENGTH+ "})(?![0-9])");

        Matcher m = p.matcher(body);
        if (m.find()) {
            System.out.println(m.group());
            return m.group(0);
        }
        return null;
    }
}
