package com.geofferyj.jsignin.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class CameraViewModel(app:Application) : AndroidViewModel(app) {

//    val onItemSelectedEvent: MutableLiveData<VisionType> = MutableLiveData()
    val onFabButtonEvent: MutableLiveData<Unit?> = MutableLiveData()
//    val onShutterButtonEvent: MutableLiveData<Unit?> = MutableLiveData()

//
    fun onClickFabButton(view: View) {
        onFabButtonEvent.postValue(Unit)
    }

}