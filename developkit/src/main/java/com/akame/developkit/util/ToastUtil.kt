package com.akame.developkit.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.MainThread
import java.lang.Exception
import java.lang.ref.WeakReference

object ToastUtil {
    private var toastWeekReference: WeakReference<Toast>? = null

    @MainThread
    fun showToast(context: Context, msg: String) {
        try {
            toastWeekReference?.get()?.cancel()
            Toast.makeText(context.applicationContext, msg, Toast.LENGTH_SHORT).apply {
                toastWeekReference = WeakReference(this)
            }.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}