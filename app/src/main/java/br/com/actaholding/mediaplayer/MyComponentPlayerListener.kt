package br.com.actaholding.mediaplayer

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.analytics.AnalyticsListener

class MyComponentPlayerListener : Player.EventListener, AnalyticsListener{

    var viewModelVideoPlayer : ViewModelVideoPlayer

    init {
        viewModelVideoPlayer = ViewModelProvider.NewInstanceFactory().create(ViewModelVideoPlayer::class.java)
    }



    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        val stateString: String
        when (playbackState) {
            Player.STATE_IDLE -> {
                stateString = "ExoPlayer.STATE_IDLE      -" //show a load
                viewModelVideoPlayer.stateVideo.postValue(Player.STATE_IDLE)
            }
            Player.STATE_BUFFERING -> {
                stateString = "ExoPlayer.STATE_BUFFERING -" //show a Load after user dragger progress
                viewModelVideoPlayer.stateVideo.postValue(Player.STATE_BUFFERING)
            }
            Player.STATE_READY -> {
                stateString = "ExoPlayer.STATE_READY     -" //hide load
                viewModelVideoPlayer.stateVideo.postValue(Player.STATE_READY)
            }
            Player.STATE_ENDED -> {
                stateString = "ExoPlayer.STATE_ENDED     -" //Video is end
                viewModelVideoPlayer.stateVideo.postValue(Player.STATE_ENDED)
            }
            else -> {
                stateString = "UNKNOWN_STATE             -"
                viewModelVideoPlayer.stateVideo.postValue(-1)
            }
        }
        Log.d("TAG", "changed state to $stateString playWhenReady: $playWhenReady")
    }

    fun getObservableViewModel(): ViewModelVideoPlayer = viewModelVideoPlayer



}