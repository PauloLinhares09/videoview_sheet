package com.packapps.videoview

import android.util.Log
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.analytics.AnalyticsListener

class MyComponentPlayerListener : Player.EventListener, AnalyticsListener{

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        val stateString: String
        when (playbackState) {
            Player.STATE_IDLE -> stateString = "ExoPlayer.STATE_IDLE      -" //show a load
            Player.STATE_BUFFERING -> stateString = "ExoPlayer.STATE_BUFFERING -" //show a Load after user dragger progress
            Player.STATE_READY -> stateString = "ExoPlayer.STATE_READY     -" //hide load
            Player.STATE_ENDED -> stateString = "ExoPlayer.STATE_ENDED     -" //Video is end
            else -> stateString = "UNKNOWN_STATE             -"
        }
        Log.d("TAG", "changed state to $stateString playWhenReady: $playWhenReady")
    }

}