package com.app.base.utils;


import android.content.Context;

import com.common.lib.base.BaseApplication;

import java.io.FileNotFoundException;
import java.security.SecureRandom;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by 7du-28 on 2018/5/22.
 */

public class RealmUtils {
    //private Context context;
    private static RealmUtils mInstance;
    //private String realName = "myRealm.realm";
    private RealmUtils(){
    }
    public static RealmUtils getInstance(){
        if (mInstance == null){
            synchronized (RealmUtils.class){
                if (mInstance == null){
                    mInstance = new RealmUtils();
                }
            }
        }
        return mInstance;
    }
    /**
     * 获得Realm对象
     * @return
     */
    //RealmConfiguration config;
    public Realm getRealm(){
           /*if(config==null){
               realName=context.getPackageName()+".realm";
               byte[] key = new byte[64];
               new SecureRandom().nextBytes(key);
               //deleteRealmIfMigrationNeeded 不再抛异常，直接删除以前的数据构造版本号，会造成数据丢失。
               config = new RealmConfiguration.Builder()
                       .name(realName).deleteRealmIfMigrationNeeded()
                       .schemaVersion(1) //版本号
                       .encryptionKey(key)
                       .build();


                       RealmConfiguration config = new RealmConfiguration.Builder()
            .name("myrealm.realm") //文件名
            .schemaVersion(1)
            .migration(new CustomMigration())//升级数据库
            .build();

           }*/
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(BaseApplication.getInstance().getPackageName()+".realm") //文件名
                .schemaVersion(0) //版本号
                .deleteRealmIfMigrationNeeded()//声明版本冲突时自动删除原数据库，开发时候打开
                //.migration(new CustomMigration())
                .build();
        Realm realm;
        try {
            realm = Realm.getInstance(config);
        } catch (RealmMigrationNeededException e) {
            e.printStackTrace();
            // You can then manually call Realm.migrateRealm().
            try {
                Realm.migrateRealm(config, new CustomMigration());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            realm = Realm.getInstance(config);
        }
        return realm;
    }



    /**
     * 升级数据库
     */
    class CustomMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
            /*if (oldVersion == 0 && newVersion == 1) {
                RealmObjectSchema personSchema = schema.get("User");
                //新增@Required的id
                personSchema
                        .addField("id", String.class, FieldAttribute.REQUIRED)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject obj) {
                                //obj.set("id", "1");//为id设置值
                            }
                        })
                        .removeField("age");//移除age属性
                oldVersion++;
            }*/
        }
    }
}
