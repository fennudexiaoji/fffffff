package com.app.base.api;

import android.util.Log;

import com.app.base.bean.AppInfo;
import com.app.base.bean.NeedServiceEntity;
import com.app.base.bean.OrderEntity;
import com.app.base.bean.OrderStatusBean;
import com.app.base.bean.Store;
import com.app.base.utils.HttpUrl;
import com.common.lib.retrofit.RetrofitUtils;
import com.common.lib.retrofit.model.BaseResponse;
import com.common.lib.retrofit.rxandroid.FullResponseTransformer;
import com.common.lib.retrofit.rxandroid.JsonParesTransformer;
import com.common.lib.retrofit.rxandroid.ResponseTransformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class GoodsImplAPI {
    public static Observable<List<NeedServiceEntity>> getNeedList(int page){
        GoodsAPI goodsAPI = RetrofitUtils.createService(GoodsAPI.class);
        JSONObject jsonObject=new JSONObject();
        try {
            JSONObject conditionJson=new JSONObject();
            conditionJson.put("order_is_pay",1);
            conditionJson.put("b.order_belong_store_id",0);
            jsonObject.put("condition",conditionJson);
            jsonObject.put("page",page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String postInfoStr="{\"condition\":{\"b.order_is_pay\":1,\"b.order_belong_store_id\":0},\"page\":1}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());
        Observable<BaseResponse<List<NeedServiceEntity>>> observable = goodsAPI.getNeedList(body);
        return observable.compose(new ResponseTransformer<>());

    }


    public static Observable<List<OrderEntity>> getUserOrderList(String json){
        GoodsAPI goodsAPI = RetrofitUtils.createService(GoodsAPI.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        Observable<BaseResponse<List<OrderEntity>>> observable = goodsAPI.getUserOrderList(body);
        return observable.compose(new ResponseTransformer<>());

    }
    public static Observable<OrderStatusBean> getUserOrderStatus(){
        GoodsAPI goodsAPI = RetrofitUtils.createService(GoodsAPI.class);
        Observable<BaseResponse<OrderStatusBean>> observable = goodsAPI.getUserOrderStatus("");
        return observable.compose(new ResponseTransformer<>());
    }

    //取消订单
    public static Observable<BaseResponse<List>> cancelOrder(String orderNum){
        GoodsAPI goodsAPI = RetrofitUtils.createService(GoodsAPI.class);
        String url=HttpUrl.API_HOST+"/user.cancel.order-"+orderNum;
        Observable<BaseResponse<List>> observable = goodsAPI.cancelOrder(url,orderNum);
        return observable.compose(new FullResponseTransformer<>());
    }

    //删除订单
    public static Observable<BaseResponse<List>> deleteOrder(String orderNum){
        GoodsAPI goodsAPI = RetrofitUtils.createService(GoodsAPI.class);
        String url=HttpUrl.API_HOST+"/order.delete-"+orderNum;
        Observable<BaseResponse<List>> observable = goodsAPI.deleteOrder(url,orderNum);
        return observable.compose(new FullResponseTransformer<>());
    }

    public static void test(){
        /*GoodsAPI goodsAPI = RetrofitUtils.createService(GoodsAPI.class);
        Observable<String> observable = goodsAPI.stringTest();
        observable.compose(new JsonParesTransformer(AppInfo.class)).subscribe(new Consumer<AppInfo>() {
            @Override
            public void accept(AppInfo appInfo) throws Exception {
                Log.i("aaaaaaa", "" + appInfo.getVersionName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i("aaaaaaa", "" + throwable.getMessage());
            }
        });*/

    }


}