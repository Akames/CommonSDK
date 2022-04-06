package com.akame.developkit.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setCanceledOnTouchOutside(true)
        return super.onCreateDialog(savedInstanceState).apply {
            setContentView(getDialogView())
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            setWindowSize(window, getWindowWidth(), getWindowHeight())
            init()
        }
    }

    abstract fun getDialogView(): View

    abstract fun init()

    private fun setWindowSize(window: Window?, width: Int, height: Int) {
        val layoutParams = window?.attributes
        window?.setGravity(gravity())
        layoutParams?.width = width
        layoutParams?.height = height
        window?.attributes = layoutParams
    }

    open fun getWindowWidth(): Int = ViewGroup.LayoutParams.MATCH_PARENT

    open fun getWindowHeight(): Int = ViewGroup.LayoutParams.WRAP_CONTENT

    open fun show(manager: FragmentManager) {
        show(manager, this.javaClass.simpleName)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        manager.beginTransaction().remove(this).commit();
        super.show(manager, tag)
    }

    open fun gravity() = Gravity.CENTER
}