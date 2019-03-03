package com.common.lib.pullrecycler;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.common.lib.R;
import com.common.lib.list.WrapRecyclerView;
import com.common.lib.list.WrapperAdapter;
import com.common.lib.pullrecycler.base.IPullRecycler;
import com.common.lib.pullrecycler.layoutmanager.ILayoutManager;
import com.common.lib.pullrecycler.loadmore.DefaultLoadMoreView;
import com.common.lib.pullrecycler.loadmore.ILoadMoreHandler;
import com.common.lib.pullrecycler.loadmore.ILoadMoreUIHandler;
import com.common.lib.pullrecycler.refresh.IRefreshHandler;
import com.common.lib.pullrecycler.refresh.IRefreshUIHandler;

/**
 * https://github.com/HelloVass/HVTea
 *
 * 封装下拉刷新和上拉加载更多的组件
 */

public class PullRecycler extends FrameLayout
    implements IPullRecycler, IPullRecycler.IHandlerProvider {
  private int footType=2147483647-1;
  private SwipeRefreshLayout mRefreshLayout;

  private WrapRecyclerView mRecyclerView;

  private ILayoutManager mLayoutManager;

  private ILoadMoreUIHandler mLoadMoreUIHandler;

  private ILoadMoreHandler mLoadMoreHandler;

  private IRefreshHandler mRefreshHandler;

  private WrapperAdapter mLoadMoreAdapterWrapper;

  private boolean mEnableLoadMore = false;

  private boolean mEnableRefresh = false;

  private boolean mIsLoading = false;

  private boolean mLoadError = false;

  private boolean mHasMore = true;

  private int mTouchSlop = 0;

  private int mYDown = 0;

  private int mLastY = 0;

  private SwipeRefreshLayout.OnRefreshListener mSwipeRefreshListener =
      new SwipeRefreshLayout.OnRefreshListener() {

        @Override public void onRefresh() {

          if (mEnableRefresh && mRefreshHandler != null && !mIsLoading) {

            mRefreshHandler.onRefresh();
          }
        }
      };

  private RecyclerView.OnScrollListener mOnReachBottomListener =
      new RecyclerView.OnScrollListener() {

        @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);

          int totalItemCount = mLayoutManager.fetchItemCount();
          int lastVisibleItemPosition = mLayoutManager.fetchLastVisibleItemPosition();

          if (isPullUp()
              && newState == RecyclerView.SCROLL_STATE_IDLE
              && mEnableLoadMore
              && lastVisibleItemPosition >= totalItemCount - 1) {

            onReachBottom();
          }
        }
      };

  public PullRecycler(@NonNull Context context) {
    this(context, null);
  }

  public PullRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PullRecycler(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {

    mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    View.inflate(context, R.layout.layout_pull_recycler, this);

    mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_Layout);
    mRecyclerView = (WrapRecyclerView) findViewById(R.id.recyclerview);

    mRefreshLayout.setOnRefreshListener(mSwipeRefreshListener);
    mRecyclerView.addOnScrollListener(mOnReachBottomListener);
  }

  public void enableLoadMore(boolean enableLoadMore) {

    mEnableLoadMore = enableLoadMore;
  }

  public void setEnableRefresh(boolean enableRefresh) {

    mEnableRefresh = enableRefresh;
    mRefreshLayout.setEnabled(enableRefresh);
  }

  @Override public void setRefreshHandler(IRefreshHandler refreshHandler) {

    if (refreshHandler == null) {

      throw new IllegalArgumentException("refreshHandler can't be null");
    }

    mRefreshHandler = refreshHandler;
  }

  @Override public void setLoadMoreUIHandler(ILoadMoreUIHandler loadMoreUIHandler) {

    if (loadMoreUIHandler == null) {

      throw new IllegalArgumentException("loadMoreUIHandler can't be null");
    }

    if (mLoadMoreAdapterWrapper == null) {

      throw new IllegalStateException("you must call setAdapter method first");
    }

    mLoadMoreUIHandler = loadMoreUIHandler;
    mLoadMoreUIHandler.getConvertView().setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {

        prepareToLoadMore();
      }
    });

    mLoadMoreAdapterWrapper.setFooterView(loadMoreUIHandler.getConvertView(),footType);
  }

  @Override public void setLoadMoreHandler(ILoadMoreHandler loadMoreHandler) {

    if (loadMoreHandler == null) {

      throw new IllegalArgumentException("loadMoreHandler can't be null");
    }

    mLoadMoreHandler = loadMoreHandler;
  }

  public WrapRecyclerView getRecyclerView() {
    return mRecyclerView;
  }

  public void setLayoutManager(ILayoutManager layoutManager) {

    if (layoutManager == null) {
      throw new IllegalArgumentException("layoutManager can't be null");
    }

    mLayoutManager = layoutManager;
    mRecyclerView.setLayoutManager(layoutManager.getLayoutManager());
  }

  public void addItemDecoration(DividerItemDecoration dividerItemDecoration) {

    mRecyclerView.addItemDecoration(dividerItemDecoration);
  }
  public void addItemDecoration(RecyclerView.ItemDecoration dividerItemDecoration) {

    mRecyclerView.addItemDecoration(dividerItemDecoration);
  }

  public void setAdapter(WrapperAdapter adapter) {

    if (adapter == null) {

      throw new IllegalArgumentException("adapter can't be null");
    }

    mLoadMoreAdapterWrapper = adapter;
    setLoadMoreUIHandler(new DefaultLoadMoreView(getContext()));
    mRecyclerView.setAdapter(adapter);
  }

  @Override public void onRefreshSucceed(boolean hasMore) {

    mLoadError = false;
    mHasMore = hasMore;
    setIsLoading(false);
    mRefreshLayout.setRefreshing(false);

    if (mLoadMoreUIHandler != null) {

      mLoadMoreUIHandler.onLoadSucceed(hasMore);
    }
  }

  @Override public void onRefreshFailed(IRefreshUIHandler refreshUIHandler) {

    mLoadError = true;
    setIsLoading(false);
    mRefreshLayout.setRefreshing(false);
    refreshUIHandler.onFailed();
  }

  @Override public void onLoadMoreSucceed(boolean hasMore) {

    mLoadError = false;
    mHasMore = hasMore;
    setIsLoading(false);

    if (mLoadMoreUIHandler != null) {

      mLoadMoreUIHandler.onLoadSucceed(hasMore);
    }
  }

  @Override public void onLoadMoreFailed(String errorMsg) {

    mLoadError = true;
    setIsLoading(false);

    if (mLoadMoreUIHandler != null) {

      mLoadMoreUIHandler.onLoadFailed(errorMsg);
    }
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {

    int action = ev.getAction();

    switch (action) {

      case MotionEvent.ACTION_DOWN:
        mYDown = (int) ev.getRawY();
        break;

      case MotionEvent.ACTION_MOVE:
        mLastY = (int) ev.getRawY();
        break;

      default:
        break;
    }

    return super.dispatchTouchEvent(ev);
  }

  /**
   * 是否为上拉操作
   *
   * @return true 为上拉
   */
  private boolean isPullUp() {

    return mYDown - mLastY >= mTouchSlop;
  }

  /**
   * 设置当前加载状态
   *
   * @param isLoading true 表示正在加载中
   */
  private void setIsLoading(boolean isLoading) {

    mIsLoading = isLoading;

    if (!isLoading) {
      mYDown = 0;
      mLastY = 0;
    }
  }

  private void onReachBottom() {

    if (mLoadError) {

      return;
    }

    prepareToLoadMore();
  }

  private void prepareToLoadMore() {

    if (mIsLoading) {

      return;
    }

    if (!mHasMore) {

      return;
    }

    if (mLoadMoreUIHandler != null) {

      mLoadMoreUIHandler.onLoading();
    }

    if (mLoadMoreHandler != null) {

      mLoadMoreHandler.onLoadMore();
    }
  }

  private int dp2px(float dp) {

    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        getResources().getDisplayMetrics());
  }
}
