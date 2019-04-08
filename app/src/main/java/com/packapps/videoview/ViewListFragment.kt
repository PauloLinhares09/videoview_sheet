package com.packapps.videoview

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.area_video_expanded.*
import kotlinx.android.synthetic.main.area_video_expanded.view.*
import kotlinx.android.synthetic.main.content_view_list_fragment.view.*
import kotlinx.android.synthetic.main.fragment_view_list.view.*


class ViewListFragment : Fragment() {
    lateinit var mView : View
    lateinit var bottomSheetBehavior : BottomSheetBehavior<View>
    lateinit var v : VideoView


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

            //Video
            v = mView.videoView
//            v.setMediaController(MediaController(context))
            val parse = Uri.parse("android.resource://" + context?.applicationContext?.packageName +"/"+ R.raw.video_iron_man)
            v.setVideoURI(parse)
            v.start()
            goneAfterSometime()

            mView.frameVideoView.setOnClickListener {
                if (mView.constraintControllers.isVisible) {
                    mView.constraintControllers.visibility = View.GONE
                } else {
                    mView.constraintControllers.visibility = View.VISIBLE
                    goneAfterSometime()
                }
            }
        }

        mView.ibPlayPause.setOnClickListener {
            if (v.isPlaying){
                v.pause()
                mView.ibPlayPause.setImageDrawable(resources.getDrawable(android.R.drawable.ic_media_play, activity?.theme))
            }else{
                v.start()
                mView.ibPlayPause.setImageDrawable(resources.getDrawable(android.R.drawable.ic_media_pause, activity?.theme))
            }
        }

        mView.imageView.setOnClickListener {
            v.stopPlayback()
            v.clearFocus()
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.state =BottomSheetBehavior.STATE_HIDDEN
        }


        return mView
    }

    var handler : Handler? = null
    var runnable : Runnable? = null
    private fun goneAfterSometime() {
        if (handler == null)
            handler =  Handler()
        else
            handler?.removeCallbacks(runnable)
        runnable = Runnable {
            mView.constraintControllers.visibility = View.GONE
        }
        handler?.postDelayed(runnable, 3000)
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
            ViewListFragment()

    }
}
