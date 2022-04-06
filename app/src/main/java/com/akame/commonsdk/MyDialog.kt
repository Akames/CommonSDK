package com.akame.commonsdk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.akame.developkit.base.BaseDialog

class MyDialog() : BaseDialog() {
    override fun getDialogView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_my, null)
        return view
    }

    override fun init() {
    }
}