package com.qidu.jiajie.mvp.contract;

import com.common.lib.base.view.IUIView;
import com.qidu.jiajie.bean.ServiceEntity;

import java.util.List;

/**
 * Created by 7du-28 on 2018/4/18.
 */

public class HomeContract {
    public interface Presenter{
        void loadServiceList(int page,String jsonParam);
        void checkAppOnLineVersion(String url);
    }


    public interface View extends IUIView {
        void getServiceList(List<ServiceEntity> serviceEntityList);
    }
}
