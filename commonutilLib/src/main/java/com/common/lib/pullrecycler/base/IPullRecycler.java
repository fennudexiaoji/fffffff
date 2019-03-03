package com.common.lib.pullrecycler.base;

import com.common.lib.pullrecycler.loadmore.ILoadMoreHandler;
import com.common.lib.pullrecycler.loadmore.ILoadMoreUIHandler;
import com.common.lib.pullrecycler.refresh.IRefreshHandler;
import com.common.lib.pullrecycler.refresh.IRefreshUIHandler;

/**
 * Created by hello on 2017/3/11.
 */

public interface IPullRecycler {

  /**
   * 刷新成功
   *
   * @param hasMore true 表示还有更多数据
   */
  void onRefreshSucceed(boolean hasMore);

  /**
   * 刷新失败
   *
   * @param refreshUIHandler 刷新失败的回调接口
   */
  void onRefreshFailed(IRefreshUIHandler refreshUIHandler);

  /**
   * 加载更多成功
   *
   * @param hasMore true 表示还有更多数据
   */
  void onLoadMoreSucceed(boolean hasMore);

  /**
   * 加载更多失败
   *
   * @param errorMsg 错误信息
   */
  void onLoadMoreFailed(String errorMsg);

  interface IHandlerProvider {

    void setLoadMoreUIHandler(ILoadMoreUIHandler loadMoreUIHandler);

    void setLoadMoreHandler(ILoadMoreHandler loadMoreHandler);

    void setRefreshHandler(IRefreshHandler refreshHandler);
  }
}
