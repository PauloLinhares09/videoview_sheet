package com.packapps.videoview

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment


private const val PLAYBACK_POSITION = "playbackPosition"
private const val CURRENT_WINDOW = "currentWindow"
private const val PLAY_WHEN_READY = "playWhenReady"

class FullscreenVideoFragment : DialogFragment() {
    private var playbackPosition: Long = 0
    private var currentWindow: Long = 0
    private var playWhenReady: Boolean = true
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle()

        arguments?.let {
            playbackPosition  = it.getLong(PLAYBACK_POSITION, 0)
            currentWindow     = it.getLong(CURRENT_WINDOW, 0)
            playWhenReady     = it.getBoolean(PLAY_WHEN_READY, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val mView =  inflater.inflate(R.layout.fragment_fullscreen_video, container, false)


        return mView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

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
        fun newInstance(playbackPosition: Long, currentWindow: Long, playWhenReady : Boolean) =
            FullscreenVideoFragment().apply {
                arguments = Bundle().apply {
                    putLong(PLAYBACK_POSITION, playbackPosition)
                    putLong(CURRENT_WINDOW, currentWindow)
                    putBoolean(PLAY_WHEN_READY, playWhenReady)
                }
            }
    }
}
