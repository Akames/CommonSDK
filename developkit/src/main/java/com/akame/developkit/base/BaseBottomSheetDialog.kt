package com.akame.developkit.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.akame.developkit.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialog() : BottomSheetDialogFragment() {
    private lateinit var contentView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
        contentView = LayoutInflater.from(context).inflate(getLayoutId(), null)
        return super.onCreateDialog(savedInstanceState).apply {
            setContentView(contentView)
            createView(contentView)
        }
    }

    abstract fun getLayoutId(): Int

    abstract fun createView(view: View)

    override fun onStart() {
        super.onStart()
        //拿到系统的 bottom_sheet
        dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)?.let {
            contentView.post {
                //设置view高度
                it.layoutParams.height = contentView.height
                //获取behavior
                val behavior = BottomSheetBehavior.from(it)
                //设置弹出高度
                behavior.peekHeight = contentView.height
                //设置展开状态
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

   open fun showNow(manager: FragmentManager) {
        super.showNow(manager, this.javaClass.simpleName)
        showAnimation()
    }

    private fun showAnimation() {
        contentView.let {
            val am = AnimationUtils.loadAnimation(this.context, R.anim.enter_anim)
            contentView.startAnimation(am)
        }
    }
}