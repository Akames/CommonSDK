package com.akame.commonsdk

import android.content.Context
import android.widget.Toast

object CommonSDK {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}