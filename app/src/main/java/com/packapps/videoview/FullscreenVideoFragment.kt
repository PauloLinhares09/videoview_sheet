package com.packapps.videoview

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.exoplayer2.ui.PlayerView


private const val PLAYBACK_POSITION = "playbackPosition"
private const val CURRENT_WINDOW = "currentWindow"
private const val PLAY_WHEN_READY = "playWhenReady"

class FullscreenVideoFragment : DialogFragment() {
    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady: Boolean = true
    lateinit var playerView : PlayerView
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)

        arguments?.let {
            playbackPosition  = it.getLong(PLAYBACK_POSITION, 0)
            currentWindow     = it.getInt(CURRENT_WINDOW, 0)
            playWhenReady     = it.getBoolean(PLAY_WHEN_READY, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val mView =  inflater.inflate(R.layout.fragment_fullscreen_video, container, false)

        playerView = mView.findViewById(R.id.playerView)

        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideSystemUi()
    }


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

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
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
