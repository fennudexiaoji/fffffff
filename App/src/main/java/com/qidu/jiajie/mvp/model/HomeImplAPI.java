package com.qidu.jiajie.mvp.model;

import com.app.base.bean.AppInfo;
import com.app.base.bean.BaseResponseBean;
import com.common.lib.retrofit.RetrofitUtils;
import com.common.lib.retrofit.model.BaseResponse;
import com.common.lib.retrofit.rxandroid.FullResponseTransformer;
import com.common.lib.retrofit.rxandroid.ResponseTransformer;
import com.qidu.jiajie.bean.Notice;
import com.qidu.jiajie.bean.ServiceEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 首页
 */

public class HomeImplAPI {
    //获取首页服务列表
    public static Observable<List<ServiceEntity>>  getServiceList(){
        HomeAPI homeAPI = RetrofitUtils.createService(HomeAPI.class);
        String postInfoStr="{\"condition\":{\"parent_id\":0,\"cate_is_show\":1},\"sort\":{\"is_self_support\":\"desc\",\"cate_sort\":\"desc\"},\"is_home_page\":1}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),postInfoStr);
        Observable<BaseResponse<List<ServiceEntity>>> observable = homeAPI.loadServiceList(body);
        //return observable.compose(new ResponseTransformer<>());
        return observable.compose(new ResponseTransformer());
    }


    public static Observable<BaseResponseBean<AppInfo>> checkAppOnLineVersion(String url){
        HomeAPI homeAPI = RetrofitUtils.createService(HomeAPI.class);
        Observable<BaseResponseBean<AppInfo>> observable = homeAPI.checkAppOnLineVersion(url);
        return observable;
    }

    public static Observable<Notice> getNoticeUnRead(){
        HomeAPI homeAPI = RetrofitUtils.createService(HomeAPI.class);
        Observable<BaseResponse<Notice>> observable = homeAPI.getNoticeUnRead("");
        return observable.compose(new ResponseTransformer());
    }



}

