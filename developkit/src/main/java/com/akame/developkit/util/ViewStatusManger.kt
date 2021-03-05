package com.akame.developkit.util

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

class ViewStatusManger(private var builder: Builder) {
    private var parentLayout: ViewGroup
    private var defaultParams: ViewGroup.LayoutParams?

    private var defaultIndex: Int = 0

    private var currentView: View

    init {
        defaultParams = builder.defaultView.layoutParams
        parentLayout = if (defaultParams == null) {
            builder.defaultView.rootView.findViewById(android.R.id.content)
        } else {
            builder.defaultView.parent as ViewGroup
        }

        for (i in 0..parentLayout.childCount) {
            if (parentLayout.getChildAt(i).id == builder.defaultView.id) {
                defaultIndex = i
                break
            }
        }
        currentView = builder.defaultView
    }

    private fun showStatueView(view: View) {
        if (currentView == view) {
            return
        }
        val viewParent: ViewParent? = view.parent
        if (viewParent != null) {
            (viewParent as ViewGroup).removeView(view)
        }
        if (parentLayout.childCount > defaultIndex && parentLayout.getChildAt(defaultIndex) != null) {
            parentLayout.removeViewAt(defaultIndex)
        }
        parentLayout.addView(view, defaultIndex, defaultParams)
        currentView = view
    }

    fun showDefaultView() {
        showStatueView(builder.defaultView)
    }

    fun showErrorView() {
        showStatueView(builder.errorView)
    }

    fun showEmptyView() {
        showStatueView(builder.emptyView)
    }

    fun showLoadingView() {
        showStatueView(builder.loadingView)
    }

    class Builder {
        lateinit var defaultView: View
        lateinit var errorView: View
        lateinit var emptyView: View
        lateinit var loadingView: View
        lateinit var layoutInflater: LayoutInflater

        fun context(context: Context): Builder {
            layoutInflater = LayoutInflater.from(context)
            return this
        }

        fun defaultView(view: View): Builder {
            this.defaultView = view
            return this
        }

        fun errorViewId(errorId: Int, errorBuilder: ErrorViewBuilder? = null): Builder {
            errorView = layoutInflater.inflate(errorId, null)
            errorBuilder?.errorView(errorView)
            return this
        }

        fun emptyViewId(emptyId: Int, emptyBuild: EmptyViewBuilder? = null): Builder {
            emptyView = layoutInflater.inflate(emptyId, null)
            emptyBuild?.emptyView(emptyView)
            return this
        }

        fun loadingViewId(loadingId: Int): Builder {
            this.loadingView = layoutInflater.inflate(loadingId, null)
            return this
        }

        fun builder() = ViewStatusManger(this)
    }

    interface ErrorViewBuilder {
        fun errorView(view: View)
    }

    interface EmptyViewBuilder {
        fun emptyView(view: View)
    }

}