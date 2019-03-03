package com.app.base.api;


import com.app.base.bean.AppInfo;
import com.app.base.bean.NeedServiceEntity;
import com.app.base.bean.OrderEntity;
import com.app.base.bean.OrderStatusBean;
import com.app.base.bean.Store;
import com.common.lib.retrofit.model.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by yhz on 2017/12/11.
 */
interface GoodsAPI {
    /*@FormUrlEncoded
    @POST("tasks")
    Observable<BaseResponse<List<Store>>> classify(@Field("describe") String describe);*/


    //https://vdao-mobile.7dugo.com/update/odp/odpupdate.txt
    @Headers({"Content-Type: application/json","Accept: application/json"})
    //去掉@FormUrlEncoded
    @POST("demand.list")
    Observable<BaseResponse<List<NeedServiceEntity>>> getNeedList(@Body RequestBody Body);

    //用户版订单列表
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("user.get.order")
    Observable<BaseResponse<List<OrderEntity>>> getUserOrderList(@Body RequestBody Body);

    //我的--获取订单状态数量
    @FormUrlEncoded
    @POST("user.order.statistics")
    Observable<BaseResponse<OrderStatusBean>> getUserOrderStatus(@Field("user_id") String id);


    //取消订单  user.cancel.order-15475418220000000000001
    @FormUrlEncoded
    @POST
    Observable<BaseResponse<List>> cancelOrder(@Url String cancelUrl,@Field("order_num") String order_num);

    @FormUrlEncoded
    @POST
    Observable<BaseResponse<List>> deleteOrder(@Url String deleteUrl,@Field("order_num") String order_num);

    /*@GET("cartItem/cartList")
    Observable<BaseResponse<List<Store>>> carList();

    @FormUrlEncoded
    @POST("cartItem/changeQuantity")
    Observable<BaseResponse> changeQuantity(@Field("id") String id, @Field("quantity") int quantity);

    @FormUrlEncoded
    @POST("cartItem/delete")
    Observable<BaseResponse> delete(@Field("cartIds") String id);

    @FormUrlEncoded
    @POST("cartItem/buyAgain")
    Observable<BaseResponse> addToCar(@Field("orderId") String orderId, @Field("storeId") String storeId);

    @GET("product/productList")
    Observable<BaseResponse<ItemList<Goods>>> list(@Query("name") String content, @Query("brandId") String brandId,
                                                   @Query("startPrice") String startPrice, @Query("endPrice") String endPrice,
                                                   @Query("categoryId") String categoryId, @Query("orderBy") String orderBy,
                                                   @Query("boothId") String boothId,
                                                   @QueryMap Map<String, String> param,
                                                   @Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    @GET("product/brandList")
    Observable<BaseResponse<List<Brand>>> brand(@Query("categoryId") String classifyId);

    @GET("member/goodsBrowseList")
    Observable<BaseResponse<ItemList<Goods>>> history(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    @GET("member/goodsFollowList")
    Observable<BaseResponse<ItemList<Goods>>> attention(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    @FormUrlEncoded
    @POST("member/delGoodsBrowse")
    Observable<BaseResponse> historyDelete(@Field("browseIds") String id);


    @GET("portal/index/boothList")
    Observable<BaseResponse<BoothList>> boothList();

    @GET("portal/index/navigationList")
    Observable<BaseResponse<HomeBanner>> bannerList();

    @GET("portal/index/indexCommend")
    Observable<BaseResponse<ItemList<Goods>>> commendList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);*/
}