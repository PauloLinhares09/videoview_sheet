package br.com.actaholding.mediaplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelFullscreenObservable() : ViewModel(){

    var objectVideo : MutableLiveData<ObjectVideo> = MutableLiveData()

}


class ObjectVideo (val playbackPosition : Long, val currentWindow : Int)