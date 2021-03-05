package com.akame.developkit.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akame.developkit.util.ViewStatusManger
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException

abstract class BaseLibFragment : Fragment(), CoroutineScope by MainScope() {
    private var statueLayout: ViewStatusManger? = null
    protected var mContentView: View? = null
    protected var isLoadData = false
    private var isRegisterEventBus = false //当前页面是否注册了EventBus
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mContentView == null && !isBinding()) {
            mContentView = inflater.inflate(getLayoutResourceId(), container, false)
        }
        return mContentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        if (!isLoadData) {
            isLoadData = true
            lazyData()
        }
    }

    abstract fun getLayoutResourceId(): Int

    abstract fun init()

    abstract fun lazyData()

    open fun initListener() {}

    open fun isBinding() = false

    abstract fun getErrorViewId(): Int
    abstract fun getEmptyViewId(): Int
    abstract fun getLoadingViewId(): Int

    fun initViewStatusManage(
        statueView: View,
        emptyId: Int = getEmptyViewId(),
        errorId: Int = getErrorViewId(),
        loadingId: Int = getLoadingViewId(),
        errorViewBuilder: ViewStatusManger.ErrorViewBuilder? = null,
        emptyViewBuilder: ViewStatusManger.EmptyViewBuilder? = null
    ) {
        statueLayout = ViewStatusManger.Builder()
            .context(context!!)
            .defaultView(statueView)
            .loadingViewId(loadingId)
            .errorViewId(errorId, errorViewBuilder)
            .emptyViewId(emptyId, emptyViewBuilder)
            .builder()
    }

    fun showDefaultView() {
        statueLayout?.showDefaultView()
    }

    fun showErrorView() {
        statueLayout?.showErrorView()
    }

    fun showEmptyView() {
        statueLayout?.showEmptyView()
    }

    fun showLoadingView() {
        statueLayout?.showLoadingView()
    }

    /**
     * 显示加载中提示框
     */
    abstract fun showLoadingDialog()

    /**
     * 关闭加载中提示框
     */
    abstract fun dismissLoadingDialog()

    /**
     * 注册EventBus
     */
    fun registerEventBus() {
        EventBus.getDefault().register(this)
        isRegisterEventBus = true
    }

    override fun onDestroy() {
        if (isRegisterEventBus) EventBus.getDefault().unregister(this) //注销EventBus
        cancel() //取消当前页面所有协程
        super.onDestroy()
    }
}