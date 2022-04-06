package com.akame.developkit.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.akame.developkit.viewstatus.IViewStatus
import com.akame.developkit.viewstatus.ViewStatusManger
import java.lang.reflect.ParameterizedType

abstract class BaseLibFragment<VB : ViewBinding> : Fragment(), IBaseView, IViewStatus {
    private var statueLayout: ViewStatusManger? = null
    private var isLoadData = false

    protected lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        if (!isLoadData) {
            isLoadData = true
            lazyData()
            subscribeUI()
        }
    }

    abstract fun lazyData()

    override fun initViewStatusManage(
        defaultView: View,
        emptyId: Int,
        errorId: Int,
        loadingId: Int,
        errorViewBuilder: ViewStatusManger.ErrorViewBuilder?,
        emptyViewBuilder: ViewStatusManger.EmptyViewBuilder?
    ) {
        statueLayout = ViewStatusManger.Builder()
            .context(requireContext())
            .defaultView(defaultView)
            .loadingViewId(loadingId)
            .errorViewId(errorId, errorViewBuilder)
            .emptyViewId(emptyId, emptyViewBuilder)
            .builder()
    }

    override fun showDefaultView() {
        statueLayout?.showDefaultView()
    }

    override fun showErrorView() {
        statueLayout?.showErrorView()
    }

    override fun showEmptyView() {
        statueLayout?.showEmptyView()
    }

    override fun showLoadingView() {
        statueLayout?.showLoadingView()
    }

    override fun initBinding(viewGroup: ViewGroup?) {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        binding = method.invoke(null, layoutInflater, viewGroup, false) as VB
    }
}