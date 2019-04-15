package com.packapps.videoview

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.area_video_expanded.view.*
import kotlinx.android.synthetic.main.layout_controllers_videoplayer.view.*
import kotlinx.android.synthetic.main.layout_controllers_videoplayer.view.cardProgress
import kotlinx.android.synthetic.main.layout_controllers_videoplayer_fullscreen.view.*


private const val PLAYBACK_POSITION = "playbackPosition"
private const val CURRENT_WINDOW = "currentWindow"
private const val PLAY_WHEN_READY = "playWhenReady"

class FullscreenVideoFragment : DialogFragment() {
    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady: Boolean = true
    lateinit var playerView : PlayerView
    private var player: SimpleExoPlayer? = null
    private lateinit var playerListener : MyComponentPlayerListener
    private lateinit var viewModelObservable : ViewModelFullscreenObservable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
        viewModelObservable = ViewModelProvider.NewInstanceFactory().create(ViewModelFullscreenObservable::class.java)

        arguments?.let {
            playbackPosition  = it.getLong(PLAYBACK_POSITION, 0)
            currentWindow     = it.getInt(CURRENT_WINDOW, 0)
            playWhenReady     = it.getBoolean(PLAY_WHEN_READY, true)
        }

        playerListener = MyComponentPlayerListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val mView =  inflater.inflate(R.layout.fragment_fullscreen_video, container, false)

        playerView = mView.findViewById(R.id.playerView)
        playerView.ibFullscreenDisable.setOnClickListener {
            dismiss()
        }

        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideSystemUi()

        //Initialize PlayerView
        initializePlayer()
        observerListenerVideoPlayer()

    }

    fun getViewModelObservable() = viewModelObservable

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
            Toast.makeText(context, "State: ${it}", Toast.LENGTH_SHORT).show()
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
            context,
            DefaultRenderersFactory(context!!),
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

    override fun onDetach() {
        val objectVideo = ObjectVideo(player?.currentPosition?:0, player?.currentWindowIndex?:0)
        viewModelObservable.objectVideo.postValue(objectVideo)
        releasePlayer()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }




    companion object {
        @JvmStatic
        fun newInstance(playbackPosition: Long, currentWindow: Int, playWhenReady : Boolean) =
            FullscreenVideoFragment().apply {
                arguments = Bundle().apply {
                    putLong(PLAYBACK_POSITION, playbackPosition)
                    putInt(CURRENT_WINDOW, currentWindow)
                    putBoolean(PLAY_WHEN_READY, playWhenReady)
                }
            }
    }
}
