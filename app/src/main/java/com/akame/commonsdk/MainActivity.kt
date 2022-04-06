package com.akame.commonsdk

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.akame.developkit.launchActivity
import com.akame.developkit.loadEngine
import com.akame.developkit.showLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyDialog().show(supportFragmentManager)
        findViewById<ImageView>(R.id.iv_test).loadEngine("https://rumenz.com/static/cimg/img/demo2.jpg").load()
    }

    companion object {
        fun launch(context: Context) {
            context.launchActivity<MainActivity>()
        }
    }
}