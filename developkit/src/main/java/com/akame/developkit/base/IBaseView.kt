package com.akame.developkit.base

import android.view.ViewGroup

interface IBaseView {
    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 绑定UI
     */
    fun subscribeUI()

    /**
     * 初始化监听
     */
    fun initListener()

    /**
     * 显示加载中提示框
     */
    fun showLoadingDialog()

    /**
     * 关闭加载中提示框
     */
    fun dismissLoadingDialog()

    /**
     * 获取页面资源id
     */
    fun initBinding(viewGroup: ViewGroup? = null)
}