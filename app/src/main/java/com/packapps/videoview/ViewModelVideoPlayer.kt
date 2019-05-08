package com.packapps.videoview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.packapps.videoview.core.ContentData
import com.packapps.videoview.models.PublicationImpl

class ViewModelVideoPlayer() : ViewModel(){

    var stateVideo : MutableLiveData<Int> = MutableLiveData()
    var buttonClicked : MutableLiveData<Int> = MutableLiveData()
    val itemPlayList : MutableLiveData<PublicationImpl> = MutableLiveData()
    var itemId : MutableLiveData<String> = MutableLiveData()
    var stateBottomSheet : MutableLiveData<Int> = MutableLiveData()

}