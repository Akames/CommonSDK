package com.akame.developkit.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import kotlinx.coroutines.*

abstract class BaseDialog(context: Context) : Dialog(context), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getDialogLayoutRes())
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        setWindowSize(getWindowWidth(), getWindowHeight())
        init()
    }

    abstract fun getDialogLayoutRes(): Int

    abstract fun init()


    private fun setWindowSize(width: Int, height: Int) {
        val layoutParams = window?.attributes
        window?.setGravity(Gravity.CENTER)
        layoutParams?.width = width
        layoutParams?.height = height
        window?.attributes = layoutParams
    }

    open fun getWindowWidth(): Int = ViewGroup.LayoutParams.MATCH_PARENT

    open fun getWindowHeight(): Int = ViewGroup.LayoutParams.MATCH_PARENT

    override fun dismiss() {
        super.dismiss()
        cancel("")
    }

}