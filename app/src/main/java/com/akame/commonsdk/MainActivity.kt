package com.akame.commonsdk

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import com.akame.commonsdk.viewmodel.MainViewModel
import com.akame.developkit.launchActivity
import com.akame.developkit.loadEngine

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyDialog().show(supportFragmentManager)
        findViewById<ImageView>(R.id.iv_test).loadEngine("https://rumenz.com/static/cimg/img/demo2.jpg").load()
        viewModel.getBanner().observe(this) {

        }
    }


    companion object {
        fun launch(context: Context) {
            context.launchActivity<MainActivity>()
        }
    }
}