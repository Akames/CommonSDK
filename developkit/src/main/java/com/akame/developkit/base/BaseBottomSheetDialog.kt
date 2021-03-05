package com.akame.developkit.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.akame.developkit.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

abstract class BaseBottomSheetDialog() : BottomSheetDialogFragment() {
    private lateinit var contentView: View
    private val jobList: ArrayList<Job> by lazy { arrayListOf<Job>() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetStyle)
        contentView = LayoutInflater.from(context).inflate(getLayoutId(), null)
        contentView.layoutParams = ViewGroup.LayoutParams(getWindowWidth(), getWindowHeight())
        createView(contentView)
        return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.dialog_style
        (contentView.parent as View).setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
    }

    abstract fun getLayoutId(): Int

    abstract fun createView(view: View)

    abstract fun getWindowWidth(): Int

    abstract fun getWindowHeight(): Int

    override fun dismiss() {
        super.dismiss()
        jobList.forEach {
            if (!it.isCancelled) {
                it.cancel()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        if (dialog != null) {
            val bottomSheet =
                dialog.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet!!.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT  //自定义高度
        }
        val view = view
        view!!.post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = view.measuredHeight
        }
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        super.showNow(manager, tag)
        showAnimation()
    }

    private fun showAnimation(){
        contentView?.apply {
            val am = AnimationUtils.loadAnimation(contentView.context, R.anim.enter_anim)
            contentView.startAnimation(am)
        }
    }
}