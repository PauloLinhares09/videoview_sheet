package com.packapps.videoview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import kotlinx.android.synthetic.main.layout_controllers_videoplayer.view.*


class PlayerViewSheetFragment : Fragment(){

    lateinit var mView : View
    lateinit var bottomSheetBehavior : BottomSheetBehavior<View>

    //ExoPlayer flow
    lateinit var playerView : PlayerView
    private var player: SimpleExoPlayer? = null
    private var playWhenReady : Boolean = true
    private var playbackPosition : Long = 0
    private var currentWindow : Int = 0
    private lateinit var playerListener : MyComponentPlayerListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerListener = MyComponentPlayerListener()
    }



    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_view_list, container, false)

        mView.card1.setOnClickListener {
            initBottomSheetExpirience()
        }

        playerView = mView.playerView

        //Observer to player listener
        observerListenerVideoPlayer()
        managerClicksControllesCollapsed()


        return mView
    }


    private fun managerClicksControllesCollapsed() {
        mView.exo_play_aux.setOnClickListener {
            if (player?.playWhenReady == false) {
                playerView.exo_play.performClick()
                mView.exo_play_aux.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.exo_controls_pause,
                        context?.theme
                    )
                )
            } else {
                playerView.exo_pause.performClick()
                mView.exo_play_aux.setImageDrawable(resources.getDrawable(R.drawable.exo_controls_play, context?.theme))
            }
        }
        mView.exo_prev_aux.setOnClickListener {
            playerView.exo_prev.performClick()
        }
        mView.exo_nex_aux.setOnClickListener {
            playerView.exo_next.performClick()
        }
        playerView.ib_state_bottomsheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        playerView.ibFullscreenEnable.setOnClickListener {
            if (player != null) {
                val bundle = Bundle()
                bundle.putLong(FullscreenVideoActivity.PLAYBACK_POSITION, player?.currentPosition!!)
                bundle.putInt(FullscreenVideoActivity.CURRENT_WINDOW, player?.currentWindowIndex!!)
                bundle.putBoolean(FullscreenVideoActivity.PLAY_WHEN_READY, playWhenReady)
                val intent = Intent(context, FullscreenVideoActivity::class.java)
                intent.putExtras(bundle)
                startActivityForResult(intent, 100)

//                val fullscreenVideoFragment = FullscreenVideoActivity.newInstance(player?.currentPosition!!, player?.currentWindowIndex!!, playWhenReady)
//                fullscreenVideoFragment.show(fragmentManager, "FULLSCREEN_VIDEO")
//                Handler().postDelayed({
//                    val observable = fullscreenVideoFragment.getViewModelObservable()
//                    observable.objectVideo.observe(this, Observer {
//                        Log.i("TAG", "currentPosition= " + it)
//                        playbackPosition = it.playbackPosition
//                        currentWindow = it.currentWindow
//                        initializePlayer()
//
//                    })
//                }, 800)
                releasePlayer()
            }
        }

    }


    private fun managerButtonsControllersByStateBottomSheet(){
        if (player?.playWhenReady == true) {
            mView.exo_play_aux.setImageDrawable(resources.getDrawable(R.drawable.exo_controls_pause, context?.theme))
        } else {
            mView.exo_play_aux.setImageDrawable(resources.getDrawable(R.drawable.exo_controls_play, context?.theme))
        }
    }

    private fun observerListenerVideoPlayer() {
        val viewModelVideoPlayer = playerListener.getObservableViewModel()
        viewModelVideoPlayer.stateVideo.observe(this, Observer {
            Toast.makeText(context, "State: ${it}", Toast.LENGTH_SHORT).show()
            if (it == Player.STATE_IDLE || it == Player.STATE_BUFFERING) {
                mView.playerView.cardProgress.visibility = View.VISIBLE
            } else if (it == Player.STATE_ENDED) {

            } else {
                mView.playerView.cardProgress.visibility = View.GONE
            }

        })
    }

    private fun initBottomSheetExpirience() {
        initializePlayer()
        bottomSheetBehavior = BottomSheetBehavior.from(mView.bottomSheetVideo)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.peekHeight = 450

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(view: View, positionFloat: Float) {
                Log.i("TAG", "positionFloat: " + positionFloat)
                if (positionFloat < 0.45) {
                    animateConstraint(BottomSheetBehavior.STATE_COLLAPSED)
                    playerView.container_controllers_play.visibility = View.GONE
                }else if (positionFloat > 0.46) {
                    animateConstraint(BottomSheetBehavior.STATE_EXPANDED)
                    playerView.container_controllers_play.visibility = View.VISIBLE
                }
            }

            override fun onStateChanged(view: View, positionState: Int) {
                Log.i("TAG", "state: " + positionState)
                if (positionState == BottomSheetBehavior.STATE_COLLAPSED) {
                    playerView.container_controllers_play.visibility = View.GONE
                    animateConstraint(positionState)
                    managerButtonsControllersByStateBottomSheet()
                } else if (positionState == BottomSheetBehavior.STATE_EXPANDED) {
                    animateConstraint(positionState)
                    playerView.container_controllers_play.visibility = View.VISIBLE
                    managerButtonsControllersByStateBottomSheet()
                }
            }

        })

        managerClickIbClose()

        //Adapter list playlist on sheet
        mView.rvPlaylist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mView.rvPlaylist.adapter = PlaylistAdapter()
    }


    private fun initializePlayer() {
        //A Simple instance
        player = ExoPlayerFactory.newSimpleInstance(
            context,
            DefaultRenderersFactory(context!!),
            DefaultTrackSelector(), DefaultLoadControl())

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


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
//            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
//        hideSystemUi()
        if ((Util.SDK_INT <= 23 || player == null)) {
//            initializePlayer()
            if (playbackPosition > 0)
                initBottomSheetExpirience()
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
            //Remove listners
            player?.removeAnalyticsListener(playerListener)
            player?.removeListener(playerListener)
            player?.release()
            player = null
        }
    }




    private fun managerClickIbClose() {
        mView.imageView.setOnClickListener {
            releasePlayer()
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

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
