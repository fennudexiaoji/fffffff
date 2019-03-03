package com.qidu.jiajie.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class Auth implements Parcelable {
    public String access_token;
    public String expires_in;
    public String refresh_token;
    public String openid;
    public String scope;
    public String unionid;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeString(expires_in);
        dest.writeString(refresh_token);
        dest.writeString(openid);
        dest.writeString(scope);
        dest.writeString(unionid);
    }

    public static final Creator<Auth> CREATOR = new Creator<Auth>() {
        //实现从source中创建出类的实例的功能
        @Override
        public Auth createFromParcel(Parcel source) {
            Auth auth = new Auth();
            auth.access_token = source.readString();
            auth.expires_in = source.readString();
            auth.refresh_token = source.readString();
            auth.openid = source.readString();
            auth.scope = source.readString();
            auth.unionid = source.readString();
            return auth;
        }

        //创建一个类型为T，长度为size的数组
        @Override
        public Auth[] newArray(int size) {
            return new Auth[size];
        }
    };
}
