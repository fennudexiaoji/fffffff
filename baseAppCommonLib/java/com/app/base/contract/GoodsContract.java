package com.app.base.contract;

import com.app.base.bean.AppInfo;
import com.app.base.bean.NeedServiceEntity;
import com.app.base.bean.OrderEntity;
import com.common.lib.base.view.IUIView;
import com.common.lib.retrofit.model.BaseResponse;

import java.util.List;


public class GoodsContract {

    //prensent接口省略
    public interface Presenter{
        //赚钱-找活干列表
        void getNeedList(int page);
        //订单列表
        void getOrderList(String json);
    }

    public interface View extends IUIView {
        void getNeedListResult(List<NeedServiceEntity> list);

        void getOrderListResult(List<OrderEntity> orderEntityList);
    }
}
