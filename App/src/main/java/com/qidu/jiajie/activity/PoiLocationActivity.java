package com.qidu.jiajie.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.app.base.utils.IntentParams;
import com.app.base.widget.LoadMoreView;
import com.common.lib.base.AbsBaseActivity;
import com.common.lib.global.PermissionUtils;
import com.qidu.jiajie.R;
import com.qidu.jiajie.adapter.LocationListAdapter;
import com.qidu.jiajie.fragment.KeyWordSearchAddressFragment;
import com.qidu.jiajie.utils.DataUtils;
import com.qidu.jiajie.utils.MyAMapLocationUtil;

import java.util.List;

/**
 * 周边搜索
 */

public class PoiLocationActivity extends AbsBaseActivity implements  PoiSearch.OnPoiSearchListener,/*LocationSource,AMapLocationListener,*/AMap.OnCameraChangeListener{
    public static int resultCode=0x345;
    private int currentPage=1;//当前页数    0和的数据一样，从一开始
    private int pageSize=10;
    private LocationListAdapter adapter; //周边的适配器
    private LatLonPoint latLonPoint;

    private ListView listView;
    private AMap aMap;
    private MapView mapView;
    private String deepType = "";// poi搜索类型
    private String keyWord = "";// poi搜索字符串
    private String cityCode;
    //private TextView titleCenter;
    //private ClearEditText keyWordEdit;
    private TextView keyWordEdit;
    private View btnSend,btnBack;
    private PoiItem poiSelectedItem;
    private boolean isMoveByHand;//是否是手移动地图
    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_MULTI_PERMISSION:
                    startLocation();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_location);
        //centerIcon=findViewById(R.id.bmap_center_icon);
        btnSend=findViewById(R.id.btn_send);
        keyWordEdit= (TextView) findViewById(R.id.search_edit);
        btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*resetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocation();
            }
        });*/
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(poiSelectedItem==null){
                    Toast.makeText(getApplicationContext(),"请选择地址",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(poiSelectedItem.getCityCode().equals("020")){
                    Intent intent=new Intent();
                    intent.putExtra(IntentParams.KEY_ADDRESS_INFO, DataUtils.getAddressData(poiSelectedItem));
                    setResult(resultCode,intent);
                    finish();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PoiLocationActivity.this);
                    builder.setTitle("温馨提示")
                            .setMessage("目前帮家洁平台只能选择广州市范围地址哦")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create()
                            .show();
                }


            }
        });
        mapView= (MapView) findViewById(R.id.bmap_View);
        mLoadMoreView=new LoadMoreView(getApplicationContext());
        listView = (ListView) findViewById(R.id.bmap_listview);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnCameraChangeListener(this);
            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            //aMap.setLocationSource(this);// 设置定位监听

            aMap.setMyLocationEnabled(false);// 设置默认定位按钮是否可以点击

            aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
                @Override
                public void onTouch(MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN://手放下
                            break;
                        case MotionEvent.ACTION_MOVE:  //手移动
                            isMoveByHand=true;
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                }
            });
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
        }

        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.draggable(false);//可拖放性
        mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
        final Marker mCenterMarker = aMap.addMarker(mMarkerOptions);
        ViewTreeObserver vto = mapView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mCenterMarker.setPositionByPixels(mapView.getWidth() >> 1, mapView.getHeight() >> 1);
                mCenterMarker.showInfoWindow();
            }
        });

        deepType = "餐饮服务|商务住宅|生活服务";
        List<PoiItem> poiItemList=null;
        adapter = new LocationListAdapter(listView,
                poiItemList);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }
            @Override
            public void onScroll(AbsListView absListView,  int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (!mIsLoading && !mIsPageFinished && lastVisibleItem == totalItemCount) {
                    mIsLoading = true;
                    // 显示加载更多布局
                    listView.addFooterView(mLoadMoreView);
                    initSearch();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.getCount()==0){
                    return;
                }
                //miaoshu.setText(itemData.getTitle());

                adapter.setSelection(position);
                PoiItem selected = adapter.getItem(adapter.getSelection());
                poiSelectedItem=selected;
                LatLonPoint latLonPoint = (selected.getLatLonPoint());
                LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));

            }
        });
        keyWordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyWordSearchAddressFragment fragment=new KeyWordSearchAddressFragment();
                fragment.setSendPOIAddressCallback(new KeyWordSearchAddressFragment.SendPOIAddressCallback() {
                    public void onSuccessCallBack(String message) {
                        Intent intent=new Intent();
                        intent.putExtra(IntentParams.KEY_ADDRESS_INFO, message);
                        setResult(resultCode,intent);
                        finish();
                    }
                });
                fragment.show(getSupportFragmentManager(),"KeyWordSearchAddressFragment");
            }
        });
        /*keyWordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWord=s.toString();
                currentPage=1;
                cityCode=null;
                if(adapter!=null){
                    adapter.refresh(null);
                }
                if(!TextUtils.isEmpty(keyWord)){
                    initSearch();
                }
            }
        });*/
        String[] requestPermissions = {
                PermissionUtils.PERMISSION_ACCESS_FINE_LOCATION,
                PermissionUtils.PERMISSION_ACCESS_COARSE_LOCATION
        };
        PermissionUtils.requestMultiPermissions(null,this,requestPermissions,mPermissionGrant);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyAMapLocationUtil.getSingleton().destroyLocation();
        mapView.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    /**
     * 当滑动地图的操作
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        //centerIcon.setVisibility(View.INVISIBLE);
    }


    /**
     * 当滑动地图结束后的操作
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (isMoveByHand) {
            isMoveByHand=false;
            // 定位到当前所在城市 如果需要搜索
            latLonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
            cityCode = null;
            keyWord = "";
            //keyWordEdit.setText("");
            //headLocation.setText(""+cameraPosition.target..getCity());
            currentPage = 1;
            if (adapter != null) {
                adapter.refresh(null);
            }
            initSearch();
        }
    }
    /**
     * 搜索周边的逻辑
     */

    private void initSearch() {//cityCode为城市区号
        if(cityCode==null){
            cityCode="";
        }
        poiSelectedItem=null;
        PoiSearch.Query query = new PoiSearch.Query(keyWord, deepType, ""+cityCode);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(pageSize);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setDistanceSort(true);//距离排序
        PoiSearch poiSearch = new PoiSearch(getApplicationContext(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 50000, true));//周围10公里
        poiSearch.searchPOIAsyn();// 异步搜索
    }

//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施

    //-------- 定位 Start ------

    private void startLocation() {
        MyAMapLocationUtil.getSingleton().startLocationCurrentPosition(new MyAMapLocationUtil.LocationCallBack() {
            @Override
            public void locationSuccess(final AMapLocation aMapLocation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //toolbar.setTitle(""+aMapLocation.getCity());
                        cityCode=aMapLocation.getCityCode();
                        currentPage=1;
                        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                        setMarkerOptions(latLng);
                        initSearch();
                    }
                });
            }

            @Override
            public void locationFailure() {

            }
        });
    }
    //设置我的位置
    private void setMarkerOptions(LatLng latLng) {
        //aMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(false);//设置Marker可拖动
        markerOptions.position(latLng);
        markerOptions.period(50);
        aMap.addMarker(markerOptions);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));


    }
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<PoiItem> list=poiResult.getPois();
            if(!list.isEmpty()){
                if(currentPage==1){
                    adapter.refresh(list);
                    adapter.setSelection(0);
                    //默认显示
                    PoiItem selected = adapter.getItem(adapter.getSelection());
                    poiSelectedItem=selected;
                    if(!TextUtils.isEmpty(keyWord)) {
                        LatLonPoint latLonPoint = (selected.getLatLonPoint());
                        LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                    }
                }else {
                    adapter.append(list);
                }
                if(list.size()==pageSize){
                    currentPage++;
                    onFinishLoading(false);
                }else {
                    onFinishLoading(true);
                }
                //setData(poiItems.size() >= 10 ? -1 : 0);
            }else {
                noMoreData();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name))
                    return getApplicationContext().getSystemService(name);
                return super.getSystemService(name);
            }
        });
    }
    public void onFinishLoading(boolean isPageFinished) {
        mIsLoading = false;
        setIsPageFinished(isPageFinished);

    }

    private void setIsPageFinished(boolean isPageFinished) {
        mIsPageFinished = isPageFinished;
        listView.removeFooterView(mLoadMoreView);
    }
    public void noMoreData(){
        mIsLoading = true;
        setIsPageFinished(true);
    }
    private boolean mIsLoading = false;
    private boolean mIsPageFinished = false;
    private View mLoadMoreView;


    /*private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setGpsFirst(true);
            mLocationOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
            mLocationOption.setOnceLocationLatest(false);
            mLocationOption.setNeedAddress(true);
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()
            // 方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    *//**
     * 当有数据返回的时候  setLocationListener
     * @param aMapLocation
     *//*
    @Override
    public void onLocationChanged(final AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                Log.i("aaaa", "定位成功--->"+aMapLocation.getAddress());
                cityCode=aMapLocation.getCityCode();
                currentPage=1;
                LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                setMarkerOptions(latLng);
                initSearch();
            }else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.i("aaaa", errText);

            }
        }
    }*/
}
