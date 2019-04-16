package com.packapps.videoview

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_fullscreen_video.*
import kotlinx.android.synthetic.main.layout_controllers_videoplayer.view.cardProgress
import kotlinx.android.synthetic.main.layout_controllers_videoplayer_fullscreen.view.*


class FullscreenVideoActivity : AppCompatActivity() {
    companion object{
        const val PLAYBACK_POSITION = "playbackPosition"
        const val CURRENT_WINDOW = "currentWindow"
        const val PLAY_WHEN_READY = "playWhenReady"
    }


    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady: Boolean = true
    private var player: SimpleExoPlayer? = null
    private lateinit var playerListener : MyComponentPlayerListener
//    private lateinit var viewModelObservable : ViewModelFullscreenObservable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_fullscreen_video)
//        viewModelObservable = ViewModelProvider.NewInstanceFactory().create(ViewModelFullscreenObservable::class.java)

        //Initialize PlayerView
        playerListener = MyComponentPlayerListener()
        initializePlayer()
        observerListenerVideoPlayer()
        hideSystemUi()
        playerView.ibFullscreenDisable.setOnClickListener {
            finish()
        }

        intent.extras?.let {
            playbackPosition  = it.getLong(PLAYBACK_POSITION, 0)
            currentWindow     = it.getInt(CURRENT_WINDOW, 0)
            playWhenReady     = it.getBoolean(PLAY_WHEN_READY, true)
        }


    }



//    fun getViewModelObservable() = viewModelObservable

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun observerListenerVideoPlayer() {
        val viewModelVideoPlayer = playerListener.getObservableViewModel()
        viewModelVideoPlayer.stateVideo.observe(this, Observer {
            Toast.makeText(this, "State: ${it}", Toast.LENGTH_SHORT).show()
            if (it == Player.STATE_IDLE || it == Player.STATE_BUFFERING) {
                playerView.cardProgress.visibility = View.VISIBLE
            } else if (it == Player.STATE_ENDED) {

            } else {
                playerView.cardProgress.visibility = View.GONE
            }

        })
    }

    private fun initializePlayer() {
        //A Simple instance
        player = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultRenderersFactory(this),
            DefaultTrackSelector(), DefaultLoadControl()
        )

        //Add listner
        player?.addListener(playerListener)
        player?.addAnalyticsListener(playerListener)

        //Vicule the player with the playerView
        playerView.setPlayer(player)

        //keep ready for the play when its ready
        player?.setPlayWhenReady(playWhenReady)
        player?.seekTo(currentWindow, playbackPosition)

        //File media
        val mediaSource = buildMediaSource(Uri.parse(getString(R.string.media_url_mp4)))

        //Play / prepare
        player?.prepare(mediaSource, false, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-app"))
            .createMediaSource(uri)
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player?.getCurrentPosition()!!
            currentWindow = player?.getCurrentWindowIndex()!!
            playWhenReady = player?.getPlayWhenReady()!!
            //Remove listners
            player?.removeAnalyticsListener(playerListener)
            player?.removeListener(playerListener)
            player?.release()
            player = null
        }
    }


    override fun onStop() {
        releasePlayer()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onStop()

    }

    override fun onStart() {
        super.onStart()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }


    override fun onUserLeaveHint() {
        if(Util.SDK_INT >= Build.VERSION_CODES.O){
            enterPictureInPictureMode(with(PictureInPictureParams.Builder()){
                val width = 16
                val height = 9
                setAspectRatio(Rational(width, height))
                build()
            })
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        playerView.useController = !isInPictureInPictureMode
    }



//
//    companion object {
//        @JvmStatic
//        fun newInstance(playbackPosition: Long, currentWindow: Int, playWhenReady : Boolean) =
//            FullscreenVideoActivity().apply {
//                arguments = Bundle().apply {
//                    putLong(PLAYBACK_POSITION, playbackPosition)
//                    putInt(CURRENT_WINDOW, currentWindow)
//                    putBoolean(PLAY_WHEN_READY, playWhenReady)
//                }
//            }
//    }
}
