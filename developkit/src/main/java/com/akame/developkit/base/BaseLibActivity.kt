package com.akame.developkit.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.akame.developkit.util.ActivityCollector
import com.akame.developkit.viewstatus.IViewStatus
import com.akame.developkit.viewstatus.ViewStatusManger
import java.lang.reflect.ParameterizedType

abstract class BaseLibActivity<VB : ViewBinding> : AppCompatActivity(), IBaseView, IViewStatus {
    private var statusLayout: ViewStatusManger? = null
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        initBinding()
        initStatueBar()
        initData()
        initListener()
        subscribeUI()
    }

    override fun initViewStatusManage(
        defaultView: View, emptyId: Int,
        errorId: Int, loadingId: Int,
        errorViewBuilder: ViewStatusManger.ErrorViewBuilder?,
        emptyViewBuilder: ViewStatusManger.EmptyViewBuilder?
    ) {
        statusLayout = ViewStatusManger.Builder()
            .context(this)
            .defaultView(defaultView)
            .errorViewId(errorId, errorViewBuilder)
            .emptyViewId(emptyId, emptyViewBuilder)
            .loadingViewId(loadingId)
            .builder()
    }

    override fun showDefaultView() {
        statusLayout?.showDefaultView()
    }

    override fun showErrorView() {
        statusLayout?.showErrorView()
    }

    override fun showEmptyView() {
        statusLayout?.showEmptyView()
    }

    override fun showLoadingView() {
        statusLayout?.showLoadingView()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    abstract fun getTitleBarView(): View?

    abstract fun isLight(): Boolean

    abstract fun initStatueBar()

    override fun initBinding(viewGroup: ViewGroup?) {
        val superClass = javaClass.genericSuperclass
        if (superClass is ParameterizedType && superClass.actualTypeArguments.isNotEmpty()) {
            val clazz = superClass.actualTypeArguments[0] as Class<VB>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            binding = method.invoke(null, layoutInflater) as VB
            setContentView(binding.root)
        }
    }
}
