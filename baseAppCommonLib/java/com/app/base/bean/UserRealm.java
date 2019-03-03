package com.app.base.bean;


import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.app.base.utils.HttpUrl;
import com.app.base.utils.RealmUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;
import io.realm.annotations.Required;


public class UserRealm extends RealmObject implements Serializable{


    /**
     * token : YyhVa1PFZrGlkjOzdnEfeL7QLPRZeVvo3pTGVxlDqiudhr0og3gfHZNJzsaowlIz62E0jOrk39jhZ%2BnABMkCAIy17taa9WJq4GU%2FMkLvQ0bVJt3n3RlK4byZC3Z25kDSM7HCySI89LtyJ3tcNzgEyzCfPpgTruvJlJjwu4LZ388pUFtC4KkbSInRgKOYG2ILS%2FFVpNknUdQiDCzkHNkwHZT3uNHWmWRkq4CME91g%2BfADddDsS%2FTPQ%2BG5Hz8Mk1nmf%2FKNgIkEvYwlU3eTvWu2%2FVvTA%2FSvsPVpLgNUerdTvTQFlJ21M5QLZyM6nVbQIVmxlVQNLM3%2BN%2Bpmi88lLfPPeA%3D%3D
     * need_bind_phone : false
     * user_id : 117
     */

    @Ignore
    private String token;
    @Ignore
    private boolean need_bind_phone;
    @Required
    private String user_id;
    /**
     * user_pic : uploadfile/20190109110154_.jpeg
     * user_phone : 18594042132
     * user_name : 李小龙
     * user_sex : 女
     * user_nickname : 法国杂碎王
     * user_email :
     * user_score : 1817.55
     * user_balance : 525.40
     * user_regtime : 2018-04-23 10:27:26
     * collect_count : 4
     * user_qrcode : uploadfile/user/qrcode_117.png
     * user_coupon : 0
     */

    private String user_pic;
    private String user_phone;
    private String user_name;
    private String user_sex;
    private String user_nickname;
    private String user_email;
    private String user_score;
    private String user_balance;
    private String user_regtime;
    private String collect_count;
    private String user_qrcode;
    private int user_coupon;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getNeed_bind_phone() {
        return need_bind_phone;
    }

    public void setNeed_bind_phone(boolean need_bind_phone) {
        this.need_bind_phone = need_bind_phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_score() {
        return user_score;
    }

    public void setUser_score(String user_score) {
        this.user_score = user_score;
    }

    public String getUser_balance() {
        return user_balance;
    }

    public void setUser_balance(String user_balance) {
        this.user_balance = user_balance;
    }

    public String getUser_regtime() {
        return user_regtime;
    }

    public void setUser_regtime(String user_regtime) {
        this.user_regtime = user_regtime;
    }

    public String getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }

    public String getUser_qrcode() {
        return user_qrcode;
    }

    public void setUser_qrcode(String user_qrcode) {
        this.user_qrcode = user_qrcode;
    }

    public int getUser_coupon() {
        return user_coupon;
    }

    public void setUser_coupon(int user_coupon) {
        this.user_coupon = user_coupon;
    }


    /*@BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        RequestOptions options =
                new RequestOptions()
                        .centerCrop()
                        .dontAnimate();
        Glide.with(imageView)
                .load(HttpUrl.API_HOST+imageUrl)
                .apply(options)
                .into(imageView);
    }*/

/*
用RealmAsyncTask .cancel();可以取消事务
public void onStop () {
 if (transaction != null && !transaction.isCancelled()) {
     transaction.cancel();
   }
}*/


    //开启事务的时候记得需要close关闭事务
    //异步插入
    public static void insertUserRealm(final UserRealm userRealm, Realm.Transaction.OnSuccess callbackSuccess, Realm.Transaction.OnError onError){
        Realm mRealm= RealmUtils.getInstance().getRealm();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //先清除旧的
                realm.where(UserRealm.class)/*.equalTo("user_id", userRealm.getUser_id())*/.findAll().deleteAllFromRealm();
                realm.copyToRealm(userRealm);
            }
        },callbackSuccess,onError);
    }

    private static boolean isExist(final UserRealm userRealm){
        Realm mRealm=RealmUtils.getInstance().getRealm();
        UserRealm object = mRealm.where(UserRealm.class).equalTo("user_id", userRealm.getUser_id()).findFirst();
        if(object==null){
            return false;
        }
        return true;
    }
    //更新数据
    public static void updateUserRealm(final UserRealm userRealm, Realm.Transaction.OnSuccess callbackSuccess, Realm.Transaction.OnError onError) {
        Realm mRealm=RealmUtils.getInstance().getRealm();
        if(isExist(userRealm)){
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    UserRealm object = realm.where(UserRealm.class).equalTo("user_id", userRealm.getUser_id()).findFirst();
                    object.setCollect_count(userRealm.getCollect_count());
                    object.setUser_balance(userRealm.getUser_balance());
                    object.setUser_qrcode(userRealm.getUser_qrcode());
                    object.setUser_coupon(userRealm.getUser_coupon());
                    object.setUser_email(userRealm.getUser_email());
                    object.setUser_name(userRealm.getUser_name());
                    object.setUser_nickname(userRealm.getUser_nickname());
                    object.setUser_pic(userRealm.getUser_pic());
                    object.setUser_regtime(userRealm.getUser_regtime());
                    object.setUser_score(userRealm.getUser_score());
                    object.setUser_sex(userRealm.getUser_sex());
                    object.setUser_phone(userRealm.getUser_phone());
                    object.setUser_id(userRealm.getUser_id());
                }
            },callbackSuccess,onError);
        }

    }
    //同步删除
    public static void deleteAll() throws SQLException {
        Realm mRealm=RealmUtils.getInstance().getRealm();
        mRealm.beginTransaction();
        mRealm.where(UserRealm.class).findAll().deleteAllFromRealm();
        mRealm.commitTransaction();
        mRealm.close();
    }
    //删除数据
    public static UserRealm queryUserRealmById(String user_id){
        final UserRealm results = RealmUtils.getInstance().getRealm().where(UserRealm.class).findFirst();
        return results;
    }
    //查询所有
    public static void queryAllUserRealm(final QueryDbCallBack<UserRealm> callBack){
        final RealmResults<UserRealm> results = RealmUtils.getInstance().getRealm().where(UserRealm.class).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<UserRealm>>() {
            @Override
            public void onChange(RealmResults<UserRealm> element) {
                //element = element.sort("id");
                List<UserRealm> objects = RealmUtils.getInstance().getRealm().copyFromRealm(element);
                callBack.querySuccess(objects, false);
                results.removeAllChangeListeners();
            }
        });
    }

    public interface QueryDbCallBack<T>{
        void querySuccess(List<T> items, boolean hasMore);
    }
}
