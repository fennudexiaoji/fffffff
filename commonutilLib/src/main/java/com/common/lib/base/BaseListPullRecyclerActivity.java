package com.common.lib.base;

import com.common.lib.list.BaseRecyclerAdapter;
import com.common.lib.list.WrapperAdapter;
import com.common.lib.pullrecycler.PullRecycler;
import com.common.lib.pullrecycler.loadmore.ILoadMoreHandler;
import com.common.lib.pullrecycler.refresh.IRefreshHandler;
import com.common.lib.retrofit.exception.APIException;
import com.common.lib.retrofit.exception.ExceptionHandle;
import com.common.lib.widget.StatusViewLayout;

import java.util.List;

/**
 * https://github.com/HelloVass/HVTea/blob/master/app/src/main/java/info/hellovass/hvteademo/pullrecycler/README.md
 * https://blog.csdn.net/u011904605/article/details/53008069
 */

public abstract class BaseListPullRecyclerActivity extends BaseActivity {
  protected int pageSize=10;
  private int pageNum;
  private PullRecycler mPullRecycler;
  private BaseRecyclerAdapter adapter;
  private StatusViewLayout statusViewLayout;

  protected WrapperAdapter<BaseRecyclerAdapter> adapterWrapper;
  /*@Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lib_fragment_base_pullrecycler);
    initPullRecycler();
  }*/
  private void initPullRecycler(StatusViewLayout statusViewLayout,PullRecycler mPullRecycler,BaseRecyclerAdapter adapter) {
    /*statusViewLayout=findViewById(R.id.status_view_layout);
    mPullRecycler=findViewById(R.id.pullrecycler);
    mPullRecycler.setLayoutManager(new MyLinearLayoutManager(this));
    mPullRecycler.addItemDecoration(
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // 设置分割线*/
    this.statusViewLayout=statusViewLayout;
    this.mPullRecycler=mPullRecycler;
    this.adapter=adapter;
    this.mPullRecycler.setEnableRefresh(true); // 启用下拉刷新
    this.mPullRecycler.setRefreshHandler(new IRefreshHandler() { // 设置下拉刷新回调
      @Override public void onRefresh() {
        refreshData();
      }
    });
    /*this.mPullRecycler.onRefreshFailed(new IRefreshUIHandler() {
      @Override public void onFailed() {

      }
    });*/
    this.mPullRecycler.enableLoadMore(true); // 启用 loadmore
    this.mPullRecycler.setLoadMoreHandler(new ILoadMoreHandler() { // 加载更多回调
      @Override public void onLoadMore() {
        loadMore();
      }
    });
    adapterWrapper =
            new WrapperAdapter<>(this.adapter);
    /*adapterWrapper.setHeaderView(
        LayoutInflater.from(this).inflate(R.layout.layout_progress_item, null),111111);*/
    this.mPullRecycler.setAdapter(adapterWrapper);
  }
  public final void refreshData() {
    pageNum = getInitPageIndex();
    statusViewLayout.showContent();
    loadData(pageNum);
  }
  public final void loadMore() {
    loadData(pageNum);
  }
  protected int getInitPageIndex() {
    return 0;
  }
  public abstract void loadData(int pageIndex);

  protected void onDataSuccessReceived(int pageIndex, List items) {
    if (pageIndex == getInitPageIndex() && items.size() == 0) {//无数据
      statusViewLayout.showEmpty();
      refreshSucceed(items);
    }else if (pageIndex == getInitPageIndex()){
      pageNum++;
      refreshSucceed(items);
      if(items.size()==pageSize){////刷新成功，还有下一页
        mPullRecycler.onLoadMoreSucceed(true);
        //自动加载下一页
      }else {//刷新成功，没有下一页
        mPullRecycler.onLoadMoreSucceed(false);
      }
    }else if(pageIndex > getInitPageIndex()){
      if(items.size()==pageSize){
        loadMoreSucceed(items);
        mPullRecycler.onLoadMoreSucceed(true);
      }else if(items.size()<pageSize){
        loadMoreSucceed(items);
        mPullRecycler.onLoadMoreSucceed(false);
      }else if(items.size()==0){
        noMoreData();
      }
    }
  }
  // 模拟刷新成功
  private void refreshSucceed(List items) {
    adapter.refresh(items);
    mPullRecycler.onRefreshSucceed(true);
  }

  // 模拟刷新失败
  private void refreshFailed(Throwable throwable) {
    if(throwable instanceof APIException){
      APIException apiException= (APIException) throwable;
      statusViewLayout.showError(apiException.message);
    }else if(throwable instanceof ExceptionHandle.ResponseThrowable){
      ExceptionHandle.ResponseThrowable responseThrowable= (ExceptionHandle.ResponseThrowable) throwable;
      statusViewLayout.showError(responseThrowable.message);
    }else {
      statusViewLayout.showError();
    }
  }

  // 模拟加载更多成功
  private void loadMoreSucceed(List items) {
    adapter.append(items);
    mPullRecycler.onLoadMoreSucceed(true);
  }

  // 模拟加载失败
  private void loadMoreFailed() {
    mPullRecycler.onLoadMoreFailed("服务器懵逼了，请点击重试...");
  }
  // 模拟没有更多数据
  private void noMoreData() {
    mPullRecycler.onLoadMoreSucceed(false);
  }

  @Override
  public void showError(Throwable throwable) {
    ExceptionHandle.handleException(throwable);
    if(pageNum==getInitPageIndex()){//刷新失败
      refreshFailed(throwable);
    }else if(pageNum>getInitPageIndex()){
      loadMoreFailed();
    }
  }
}
