package com.packapps.videoview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelVideoPlayer() : ViewModel(){

    var stateVideo : MutableLiveData<Int> = MutableLiveData()
    var buttonClicked : MutableLiveData<Int> = MutableLiveData()

}