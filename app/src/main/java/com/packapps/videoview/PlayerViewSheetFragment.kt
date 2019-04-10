package com.packapps.videoview

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.area_video_expanded.*
import kotlinx.android.synthetic.main.area_video_expanded.view.*
import kotlinx.android.synthetic.main.content_video_bottomsheet_emp.view.*
import kotlinx.android.synthetic.main.content_view_list_fragment.view.*
import kotlinx.android.synthetic.main.fragment_view_list.view.*






class PlayerViewSheetFragment : Fragment() {
    lateinit var mView : View
    lateinit var bottomSheetBehavior : BottomSheetBehavior<View>
    lateinit var playerView : PlayerView
    var LONG_DELAY_CONTROLLERS : Long = 3000
    var SMALL_DELAY_CONTROLLERS : Long = 1000

    private var player: SimpleExoPlayer? = null
    private var playWhenReady : Boolean = true
    private var playbackPosition : Long = 0
    private var currentWindow : Int = 0



    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_view_list, container, false)



        mView.card1.setOnClickListener {
            bottomSheetBehavior = BottomSheetBehavior.from(mView.bottomSheetVideo)
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.peekHeight = 500

            bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onSlide(view: View, positionFloat: Float) {
                    Log.i("TAG", "positionFloat: " + positionFloat)
                    if (positionFloat < 0.45)
                        animateConstraint(BottomSheetBehavior.STATE_COLLAPSED)
                    else if (positionFloat > 0.46)
                        animateConstraint(BottomSheetBehavior.STATE_EXPANDED)
                }

                override fun onStateChanged(view: View, positionState: Int) {
                    Log.i("TAG", "state: " + positionState)
                    if (positionState == BottomSheetBehavior.STATE_COLLAPSED){
                        animateConstraint(positionState)
                    }else if (positionState == BottomSheetBehavior.STATE_EXPANDED) {
                        animateConstraint(positionState)
                    }
                }

            })

////            playerView.setMediaController(MediaController(context))
//            val parse = Uri.parse("android.resource://" + context?.applicationContext?.packageName +"/"+ R.raw.video_iron_man)
//            playerView.setVideoURI(parse)
//            playerView.start()


//            goneAfterSometime(LONG_DELAY_CONTROLLERS)
//            managerClickOnFrameControllers()
//            managerClickPlayPause()
//            managerClickIbClose()


            //Adapter list playlist on sheet
            mView.rvPlaylist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mView.rvPlaylist.adapter = PlaylistAdapter()


        }

        playerView = mView.playerView



        return mView
    }




    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(context!!),
            DefaultTrackSelector(), DefaultLoadControl())

        playerView.setPlayer(player)

        player?.setPlayWhenReady(playWhenReady)
        player?.seekTo(currentWindow, playbackPosition)

        //File media
        val uri = Uri.parse("http://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
        var mediaSource : MediaSource = buildMediaSource(uri)
    }

    private fun buildMediaSource(uri: Uri?): MediaSource {
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri)
    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player?.getCurrentPosition()!!
            currentWindow = player?.getCurrentWindowIndex()!!
            playWhenReady = player?.getPlayWhenReady()!!
            player?.release()
            player = null
        }
    }


    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

//    private fun managerClickIbClose() {
//        mView.imageView.setOnClickListener {
//            playerView.stopPlayback()
//            playerView.clearFocus()
//            bottomSheetBehavior.isHideable = true
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        }
//    }
//
//    private fun managerClickPlayPause() {
//        mView.ibPlayPause.setOnClickListener {
//            if (playerView.isPlaying) {
//                handler?.removeCallbacks(runnable)//Make play controller is alweys visible
//                playerView.pause()
//                mView.ibPlayPause.setImageDrawable(
//                    resources.getDrawable(
//                        android.R.drawable.ic_media_play,
//                        activity?.theme
//                    )
//                )
//            } else {
//                goneAfterSometime(SMALL_DELAY_CONTROLLERS)
//                playerView.start()
//                mView.ibPlayPause.setImageDrawable(
//                    resources.getDrawable(
//                        android.R.drawable.ic_media_pause,
//                        activity?.theme
//                    )
//                )
//            }
//        }
//    }

//    private fun managerClickOnFrameControllers() {
//        mView.frameVideoView.setOnClickListener {
//            if (mView.constraintControllers.isVisible) {
//                mView.constraintControllers.visibility = View.GONE
//            } else {
//                mView.constraintControllers.visibility = View.VISIBLE
//                goneAfterSometime(LONG_DELAY_CONTROLLERS)
//            }
//        }
//    }

//    var handler : Handler? = null
//    var runnable : Runnable? = null
//    private fun goneAfterSometime(delay : Long) {
//        if (handler == null)
//            handler =  Handler()
//        else
//            handler?.removeCallbacks(runnable)
//        runnable = Runnable {
//            mView.constraintControllers.visibility = View.GONE
//        }
//        handler?.postDelayed(runnable, delay)
//    }

    private var set: Boolean = false
    private fun animateConstraint(state : Int) {
        if (state == BottomSheetBehavior.STATE_EXPANDED && set == true)
            return
        if (state == BottomSheetBehavior.STATE_COLLAPSED && set == false)
            return

        val constraintSet1 = ConstraintSet()
        constraintSet1.load(context, R.layout.area_video_collapsed)
        val constraintSet2 = ConstraintSet()
        constraintSet2.clone(context, R.layout.area_video_expanded)
        var constraint = if (set) constraintSet1 else constraintSet2
        TransitionManager.beginDelayedTransition(constraintViewAreExpanded)
        constraint.applyTo(mView.constraintViewAreExpanded)
        set = !set
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            PlayerViewSheetFragment()

    }
}