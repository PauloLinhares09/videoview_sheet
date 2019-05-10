package br.com.actaholding.mediaplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.actaholding.mediaplayer.models.PublicationImpl

class ViewModelVideoPlayer() : ViewModel(){

    var stateVideo : MutableLiveData<Int> = MutableLiveData()
    var buttonClicked : MutableLiveData<Int> = MutableLiveData()
    val itemPlayList : MutableLiveData<PublicationImpl> = MutableLiveData()
    var itemId : MutableLiveData<String> = MutableLiveData()
    var stateBottomSheet : MutableLiveData<Int> = MutableLiveData()

}