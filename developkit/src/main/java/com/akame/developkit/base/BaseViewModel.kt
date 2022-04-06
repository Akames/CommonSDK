package com.akame.developkit.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope

open class BaseViewModel : ViewModel(), LifecycleObserver {
    var toastMsg = MutableLiveData<String>()

    var error = MutableLiveData<String>()

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {

    }

}