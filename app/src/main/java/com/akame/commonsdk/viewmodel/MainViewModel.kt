package com.akame.commonsdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.akame.commonsdk.bean.AppResponse
import com.akame.commonsdk.repository.MainRepository
import com.akame.developkit.http.SeverResult
import com.akame.developkit.http.apiRequest
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = MainRepository()

    fun getBanner() = apiRequest({
        repository.loadBanner()
    }, {
    }, {

    })

}