package com.akame.developkit.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.akame.developkit.util.ActivityCollector
import com.akame.developkit.util.StatusBarUtil
import com.akame.developkit.util.ViewStatusManger
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException

abstract class BaseLibActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private var statusLayout: ViewStatusManger? = null // 状态页面管理，比如加载空数据或者服务器异常
    private var isRegisterEventBus = false //当前页面是否注册了EventBus
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isBinding()) {
            //如果当前不是dataBind直接加载view
            setContentView(getLayoutResourceId())
        } else {
            setContentView(getLayoutResourceView())
        }
        init()
        initListener()
        ActivityCollector.addActivity(this) //将当前Activity加载进队列
        StatusBarUtil.setLightMode(this, isLightStatueBar())// 设置状态栏颜色
        StatusBarUtil.transparentStatusBar(this, getTitleBarView())//设置沉浸式导航栏
    }

    /**
     * 获取界面资源文件
     */
    abstract fun getLayoutResourceId(): Int

    abstract fun getLayoutResourceView(): View?

    /**
     * 初始化数据
     */
    abstract fun init()

    /**
     * 初始化监听
     */
    open fun initListener() {}

    /**
     * 获取错误页面资源id
     */
    abstract fun getErrorViewId(): Int

    /**
     * 获取空页面资源id
     */
    abstract fun getEmptyViewId(): Int

    /**
     * 获取加载中的view
     */
    abstract fun getLoadingViewId(): Int

    /**
     * 显示加载中提示框
     */
    abstract fun showLoadingDialog()

    /**
     * 关闭加载中提示框
     */
    abstract fun dismissLoadingDialog()

    /**
     * 获取titleBar 就是顶部导航栏 有的页面可能没有导航栏 所有后面加了？ 表示可以为空
     */
    abstract fun getTitleBarView(): View?

    /**
     * 当前状态栏的颜色是否为白色
     */
    abstract fun isLightStatueBar(): Boolean

    /**
     * 当前页面是否为dataBinding模式
     */
    open fun isBinding(): Boolean = false

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
    ) {
        statusLayout = ViewStatusManger.Builder()
            .context(this)
            .defaultView(defaultView)
            .errorViewId(errorId, errorViewBuilder)
            .emptyViewId(emptyId, emptyViewBuilder)
            .loadingViewId(loadingId)
            .builder()
    }

    /**
     * 显示原始页面
     */
    fun showDefaultView() {
        statusLayout?.showDefaultView()
    }

    /**
     * 显示错误页面
     */
    fun showErrorView() {
        statusLayout?.showErrorView()
    }

    /**
     * 显示空页面
     */
    fun showEmptyView() {
        statusLayout?.showEmptyView()
    }

    /**
     * 显示加载中的页面
     */
    fun showLoadingView() {
        statusLayout?.showLoadingView()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel() //取消当前页面所有协程
        ActivityCollector.removeActivity(this) //将当前Activity从列表中移除 否则会内存泄露
        if (isRegisterEventBus) EventBus.getDefault().unregister(this) //注销EventBus
    }

    /**
     * 注册EventBus
     */
    fun registerEventBus() {
        EventBus.getDefault().register(this)
        isRegisterEventBus = true
    }
}
