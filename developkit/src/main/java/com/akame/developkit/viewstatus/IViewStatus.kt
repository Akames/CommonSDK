package com.akame.developkit.viewstatus

import android.view.View

interface IViewStatus {
    /**
     * 获取错误页面资源id
     */
    fun getErrorViewId(): Int

    /**
     * 获取空页面资源id
     */
    fun getEmptyViewId(): Int

    /**
     * 获取加载中的view
     */
    fun getLoadingViewId(): Int

    /**
     * 初始化状态view管理
     */
    fun initViewStatusManage(
        defaultView: View,
        emptyId: Int = getEmptyViewId(),
        errorId: Int = getErrorViewId(),
        loadingId: Int = getLoadingViewId(),
        errorViewBuilder: ViewStatusManger.ErrorViewBuilder? = null,
        emptyViewBuilder: ViewStatusManger.EmptyViewBuilder? = null
    )

    /**
     * 显示原始页面
     */
    fun showDefaultView()

    /**
     * 显示错误页面
     */
    fun showErrorView()

    /**
     * 显示空页面
     */
    fun showEmptyView()

    /**
     * 显示加载中的页面
     */
    fun showLoadingView()
}