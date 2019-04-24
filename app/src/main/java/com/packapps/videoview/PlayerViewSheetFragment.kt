package com.packapps.videoview

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.packapps.videoview.core.ContentData
import com.packapps.videoview.core.StreamType
import com.packapps.videoview.models.EmpiricusVideoBusiness
import com.packapps.videoview.utils.Utils
import kotlinx.android.synthetic.main.area_video_expanded.*
import kotlinx.android.synthetic.main.area_video_expanded.view.*
import kotlinx.android.synthetic.main.content_video_bottomsheet_emp.view.*
import kotlinx.android.synthetic.main.fragment_view_list.view.*
import kotlinx.android.synthetic.main.layout_controllers_videoplayer.view.*
import java.lang.Exception


class PlayerViewSheetFragment : Fragment(){

    private val URI_MEDIA : String = "uri_media"
    private val PEEK_HEIGHT : String = "peek_eight"
    private val EMPIRICUS_VIDEO_BUSINESS : String = "empiricus_video_business"
    private val STREAM_TYPE : String = "stream_type"
    private val CONTENT_DATA : String = "content_data"

    private var viewModelVideoPlayer: ViewModelVideoPlayer? = null
    lateinit var mView : View
    lateinit var bottomSheetBehavior : BottomSheetBehavior<View>

    //ExoPlayer flow
    lateinit var playerView : PlayerView
    private var player: SimpleExoPlayer? = null
    private var playWhenReady : Boolean = true
    private var playbackPosition : Long = 0
    private var currentWindow : Int = 0
    private lateinit var playerListener : MyComponentPlayerListener
    private var uriMedia : String? = null
    private lateinit var streamType: StreamType
    private var contentData: ContentData? = null
    private var peekHeight : Int = 550
    private var empiricusVideoBusiness: EmpiricusVideoBusiness? = null
    private var currentStateBottomSheet : Int = BottomSheetBehavior.STATE_EXPANDED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.apply {
            uriMedia = this?.getString(URI_MEDIA)
            peekHeight = this?.getInt(PEEK_HEIGHT)?:peekHeight
            empiricusVideoBusiness = this?.getParcelable(EMPIRICUS_VIDEO_BUSINESS)
            streamType = this?.getSerializable(STREAM_TYPE) as StreamType
            contentData = this?.getParcelable<ContentData>(CONTENT_DATA)
        }

        playerListener = MyComponentPlayerListener()
    }



    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mView = inflater.inflate(R.layout.fragment_view_list, container, false)

//        if (empiricusVideoBusiness == null){//TODO implement it after
//            //Show View of Error
//
//            return mView
//        }

        playerView = mView.playerView

        initBottomSheetExpirience()

        return mView
    }

    private fun managerClickButtonsContent() {
        //Set item current
        viewModelVideoPlayer?.itemId?.postValue(contentData?.id)

        //Implements events click
        mView.emp_favourite.setOnClickListener {
            viewModelVideoPlayer?.buttonClicked?.postValue(R.id.emp_favourite)
        }
        mView.emp_like.setOnClickListener {
            viewModelVideoPlayer?.buttonClicked?.postValue(R.id.emp_like)
        }
        mView.emp_dont_like.setOnClickListener {
            viewModelVideoPlayer?.buttonClicked?.postValue(R.id.emp_dont_like)
        }
        mView.emp_download.setOnClickListener {
            viewModelVideoPlayer?.buttonClicked?.postValue(R.id.emp_download)
        }
        mView.emp_audio_version.setOnClickListener {
            viewModelVideoPlayer?.buttonClicked?.postValue(R.id.emp_audio_version)
        }
        mView.emp_show_more.setOnClickListener {
            val tag: String = mView.emp_show_more.tag.toString()
            if (tag.equals("0")) {
                mView.emp_show_more.tag = 1
                mView.tvDescription.text = contentData?.description
                mView.emp_show_more.text = resources.getString(R.string.show_less)
            } else {
                mView.tvDescription.text = Utils.truncateText(contentData?.description ?: "", 100)
                mView.emp_show_more.tag = 0
                mView.emp_show_more.text = resources.getString(R.string.show_more)
            }

        }
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
                openActivityFullScreen()
                releasePlayer()
            }
        }

    }

    private fun openActivityFullScreen() {
        val bundle = Bundle()
        bundle.putLong(FullscreenVideoActivity.PLAYBACK_POSITION, player?.currentPosition ?: 0)
        bundle.putInt(FullscreenVideoActivity.CURRENT_WINDOW, player?.currentWindowIndex ?: 0)
        bundle.putBoolean(FullscreenVideoActivity.PLAY_WHEN_READY, playWhenReady)
        bundle.putString(FullscreenVideoActivity.URI, uriMedia)
        val intent = Intent(context, FullscreenVideoActivity::class.java)
        intent.putExtras(bundle)
        releasePlayer()
        startActivityForResult(intent, FullscreenVideoActivity.REQUEST_FULLSCREEN)
    }


    private fun managerButtonsControllersByStateBottomSheet(){
        if (player?.playWhenReady == true) {
            mView.exo_play_aux.setImageDrawable(resources.getDrawable(R.drawable.exo_controls_pause, context?.theme))
        } else {
            mView.exo_play_aux.setImageDrawable(resources.getDrawable(R.drawable.exo_controls_play, context?.theme))
        }
    }

    private fun observerListenerVideoPlayer() {
        viewModelVideoPlayer = playerListener.getObservableViewModel()
        viewModelVideoPlayer?.stateVideo?.observe(this, Observer {
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
        bottomSheetBehavior.state = currentStateBottomSheet
        bottomSheetBehavior.peekHeight = peekHeight

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
                    currentStateBottomSheet = BottomSheetBehavior.STATE_COLLAPSED
                    viewModelVideoPlayer?.stateBottomSheet?.postValue(currentStateBottomSheet)
                } else if (positionState == BottomSheetBehavior.STATE_EXPANDED) {
                    animateConstraint(positionState)
                    playerView.container_controllers_play.visibility = View.VISIBLE
                    managerButtonsControllersByStateBottomSheet()
                    currentStateBottomSheet = BottomSheetBehavior.STATE_EXPANDED
                    viewModelVideoPlayer?.stateBottomSheet?.postValue(currentStateBottomSheet)
                }
            }

        })


        //Observer to player listener
        observerListenerVideoPlayer()

        managerClicksControllesCollapsed()

        managerClickButtonsContent()

        managerClickIbClose()

        //### Populate fields
        populateFieldsContent()

        //Next media / in Adapter
        managerAdapterPlayListNext()

    }

    private fun managerAdapterPlayListNext() {
        if (contentData?.next != null) {
            //Adapter list playlist on sheet
            val adapterPlayList = PlaylistAdapter()
            mView.rvPlaylist.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            mView.rvPlaylist.adapter = adapterPlayList
            adapterPlayList.updateAndNotifyDataSetChanged(contentData?.next!!)
            //Listen events from Adapter list
            adapterPlayList.listenEvents(object : PlaylistAdapter.PlayListListener {
                override fun itemClicked(item: ContentData.NextMedia) {
                    //TODO send to front
                    Log.i("TAG", "click item: " + item.toString())
                }

            })
        } else {
            //Inflate somebody layout
        }
    }

    private fun populateFieldsContent() {
        mView.tvTitle.text = contentData?.title ?: ""
        mView.tvTitleCollapsed.text = Utils.truncateText(contentData?.title!!, 20)
        mView.tvDescription.text = Utils.truncateText(contentData?.description!!, 100)
        mView.tvAuthorName.text = contentData?.authors?.get(0)?.name ?: ""
        mView.ivAuthor.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_account_circle,
                context?.theme
            )
        )//TODO change for Glide
        mView.tvTimeAgo.text = contentData?.timeAgoStr ?: ""
    }


    private fun initializePlayer() {
        //A Simple instance
        try {

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
            var mediaSource: MediaSource? = null
            if (streamType == StreamType.HLS)
                mediaSource = buildHlsMediaSource(Uri.parse(uriMedia))
            else if (streamType == StreamType.MP4)
                mediaSource = buildMediaSource(Uri.parse(uriMedia))

            //Play / prepare
            player?.prepare(mediaSource, false, false)
        }catch (e : Exception){
            e.printStackTrace()
            //TODO send exception to view
        }
    }


    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-app"))
            .createMediaSource(uri)
    }

    private fun buildHlsMediaSource(playListUri : Uri): MediaSource? {
        val defaultHlsExtractorFactory = DefaultHlsExtractorFactory(FLAG_ALLOW_NON_IDR_KEYFRAMES)
        return HlsMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-app"))
        .setExtractorFactory(defaultHlsExtractorFactory)
        .createMediaSource(playListUri)
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

    fun releasePlayer() {
        if (player != null) {
            viewModelVideoPlayer?.stateBottomSheet?.postValue(-1)
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

            //Kill Fragment
            killThisFragment()
        }
    }

    private fun killThisFragment() {
        val m = (activity as FragmentActivity).supportFragmentManager
        val t = m.beginTransaction()
        t.remove(m.findFragmentByTag(PlayerViewSheetFragment::class.java.simpleName)!!)
        t.commit()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                FullscreenVideoActivity.REQUEST_FULLSCREEN -> {
                    val bundle = data?.extras
                    if (bundle != null){
                        playbackPosition = bundle.getLong(FullscreenVideoActivity.PLAYBACK_POSITION)
                        currentWindow = bundle.getInt(FullscreenVideoActivity.CURRENT_WINDOW)
                        playWhenReady = bundle.getBoolean(FullscreenVideoActivity.PLAY_WHEN_READY)

                        initializePlayer()
                    }
                }
            }
        }
    }

    fun getObservableViewModel(): ViewModelVideoPlayer {
        return playerListener.getObservableViewModel()
    }

    fun bottomSheetToCollapsed() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }


    companion object {
        @JvmStatic
        fun newInstance(uriMedia: String?, peekHeight : Int, empiricusVideoBusiness: EmpiricusVideoBusiness? = null, streamType: StreamType, contentData: ContentData?) =
            PlayerViewSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(URI_MEDIA, uriMedia)
                    putInt(PEEK_HEIGHT, peekHeight)
                    putParcelable(EMPIRICUS_VIDEO_BUSINESS, empiricusVideoBusiness)
                    putSerializable(STREAM_TYPE, streamType)
                    putParcelable(CONTENT_DATA, contentData)
                }
            }

    }
}
