/*
package app.jiajie.qidu.mvp.model;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.app.base.bean.CityItem;
import com.app.base.utils.CharacterParser;
import com.app.base.utils.CommonKey;
import com.common.lib.base.BaseApplication;
import com.common.lib.utils.NetworkUtil;
import com.common.lib.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.jiajie.qidu.mvp.contract.SearchAddressContract;
import app.jiajie.qidu.utils.MyAMapLocationUtil;
import app.jiajie.qidu.utils.PinyinComparator;

*/
/**
 * Created by 7du-28 on 2018/4/17.
 *//*


public class SearchAddressModelImpl implements SearchAddressContract.Model ,DistrictSearch.OnDistrictSearchListener{


    //行政区划搜索
    private List<DistrictItem> districtList=new ArrayList<>();
    private List<DistrictItem> wholeProvince;
    private boolean isFirst=false;
    private boolean isLoadAllCity;
    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        if(districtResult.getAMapException().getErrorCode()==1000){
            if(!isFirst) {
                isFirst=true;
                wholeProvince = districtResult.getDistrict().get(0).getSubDistrict();
                for (int i = 0; i < wholeProvince.size();i++) {
                    DistrictItem item = wholeProvince.get(i);
                    if (!TextUtils.isEmpty(item.getCitycode())) {
                        districtList.add(item);
                        wholeProvince.remove(item);
                    } */
/*else {
                        final String keyword = item.getName();
                        initSearch(keyword);
                    }*//*

                }
                */
/*if(!isLoadAllCity){
                    //initSortData();
                    if(this.districtSearchQueryListener!=null){
                        districtSearchQueryListener.onDistrictSearchResult(getSortData());
                    }
                }*//*

                if(wholeProvince!=null){
                    if(wholeProvince.size()>0){
                        final String keyword = wholeProvince.get(0).getName();
                        onDistrictByKeyWord(keyword);
                    }
                }
                //initSortData();
            }else {
                DistrictItem districtItem=districtResult.getDistrict().get(0);
                for(int i=0;i<wholeProvince.size();i++){
                    DistrictItem province=wholeProvince.get(i);
                    if(province.getName().equals(districtItem.getName())){
                        wholeProvince.remove(province);
                    }
                }
                List<DistrictItem> cityList = districtResult.getDistrict().get(0).getSubDistrict();
                districtList.addAll(cityList);
                //每获得一个省的城市刷新一次列表  避免单次获取失败或者等待没数据显示
                */
/*if(!isLoadAllCity){
                    //initSortData();
                    if(this.districtSearchQueryListener!=null){
                        districtSearchQueryListener.onDistrictSearchResult(getSortData());
                    }
                }*//*


                if(wholeProvince!=null){
                    if(wholeProvince.size()>0){
                        final String keyword = wholeProvince.get(0).getName();
                        onDistrictByKeyWord(keyword);
                    }else {

                        if(this.districtSearchQueryListener!=null){
                            districtSearchQueryListener.onDistrictSearchResult(getSortData());
                        }
                        Gson gson=new Gson();
                        String response=gson.toJson(districtList);
                        //获取到所有城市的时候
                        SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(CommonKey.KEY_ALL_CHINA_CITY_NAME,response);
                        SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(CommonKey.KEY_LOAD_ALL_CHINA_CITY_FINISH,true);
                        long currentTime = System.currentTimeMillis();
                        SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(CommonKey.KEY_ALL_CITY_REFRESH_TIME,currentTime);

                    }
                }
            }
        }else {
            //Toast.makeText(getActivity(),"获取城市失败",Toast.LENGTH_SHORT).show();
        }
    }

    private PinyinComparator pinyinComparator;
    private CharacterParser characterParser;
    */
/**
     * 数据排序
     * @param list
     * @return
     *//*

    private List<CityItem> filledData(List<DistrictItem> list) {
        List<CityItem> mSortList = new ArrayList<CityItem>();
        for (int i = 0; i < list.size(); i++) {
            CityItem sortModel = new CityItem();
            sortModel.setName(list.get(i).getName());
            sortModel.setCityCode(list.get(i).getCitycode());
            sortModel.setLatLonPoint(list.get(i).getCenter());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(list.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else if (TextUtils.equals(sortString, "定") || TextUtils.equals(sortString, "热"))
                //return pinyin;
                sortModel.setSortLetters(sortString);
            else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }
    public interface DistrictSearchQueryListener{
        void onDistrictSearchResult(List<CityItem> sortModelList);
    }
    private DistrictSearchQueryListener districtSearchQueryListener;
    private Activity activity;
    @Override
    public void onDistrictSearchQuery(Activity activity,DistrictSearchQueryListener districtSearchQueryListener){
        this.activity=activity;
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        this.districtSearchQueryListener=districtSearchQueryListener;
        String cacheCityResponse=(String) SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).getData(CommonKey.KEY_ALL_CHINA_CITY_NAME,"");
        isLoadAllCity=(boolean) SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).getData(CommonKey.KEY_LOAD_ALL_CHINA_CITY_FINISH,false);
        if(!TextUtils.isEmpty(cacheCityResponse)){
            Gson gson=new Gson();
            districtList= gson.fromJson(cacheCityResponse, new TypeToken<List<DistrictItem>>() {}.getType());
            if(this.districtSearchQueryListener!=null){
                districtSearchQueryListener.onDistrictSearchResult(getSortData());
            }
            long defaultTime=0;
            long saveTime=(long) SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).getData(CommonKey.KEY_ALL_CITY_REFRESH_TIME,defaultTime);
            long currentTime = System.currentTimeMillis();
            long s = (currentTime - saveTime) / (1000*60*60*24);
            if(s>7*4){//间隔大于四星期更新一次
                onDistrictByKeyWord("中国");
            }
        }else {
            onDistrictByKeyWord("中国");
        }
    }


    private List<CityItem> getSortData(){
        List<CityItem> sortModelList = filledData(districtList);
        // 根据a-z进行排序源数据
        Collections.sort(sortModelList, pinyinComparator);
        return sortModelList;
    }

    private void onDistrictByKeyWord(String keyword){
        DistrictSearch search = new DistrictSearch(activity);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords(keyword);//传入关键字
        query.setShowChild(true);
        query.setShowBoundary(true);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(this);//绑定监听器
        search.searchDistrictAnsy();//开始搜索
        //search.searchDistrictAsyn();
    }




    @Override
    public void startLocationCurrentPosition(MyAMapLocationUtil.LocationCallBack locationCallBack) {
        MyAMapLocationUtil.getSingleton().startLocationCurrentPosition(locationCallBack);

    }

    @Override
    public void destroyLocation() {
        MyAMapLocationUtil.getSingleton().destroyLocation();
    }

}
*/
