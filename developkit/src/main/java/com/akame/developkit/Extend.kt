package com.akame.developkit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.akame.developkit.image.ImageLoader
import com.akame.developkit.image.ImageOptions
import com.akame.developkit.util.LogUtil

fun Activity.showMsg(msg: Any) {
    Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}

fun Fragment.showMsg(msg: Any) {
    if (activity?.isFinishing == false) {
        Toast.makeText(activity, msg.toString(), Toast.LENGTH_SHORT).show()
    }
}

fun showMsg(context: Context = BaseApp.app.applicationContext, msg: Any) {
    Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
}

fun Any.showLog(msg: String) {
    LogUtil.showLog(msg)
}

inline fun <reified T> Activity.newIndexIntent(): Intent = Intent(this, T::class.java)

inline fun <reified T> Fragment.newIndexIntent(): Intent = Intent(requireContext(), T::class.java)

fun ImageView.displayImage(): ImageOptions.Builder = ImageLoader.with(this.context)

var itemTime = 0L
fun View.setOnClickListen(method: () -> Unit) {
    setOnClickListener {
        if (System.currentTimeMillis() - itemTime > 300) {
            itemTime = System.currentTimeMillis()
            method.invoke()
        }
    }
}





