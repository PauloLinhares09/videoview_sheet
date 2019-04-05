package com.packapps.videoview

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.area_video_expanded.*
import kotlinx.android.synthetic.main.area_video_expanded.view.*
import kotlinx.android.synthetic.main.content_view_list_fragment.view.*
import kotlinx.android.synthetic.main.fragment_view_list.view.*
import kotlinx.android.synthetic.main.layout_video_view_expanded.*


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
            bottomSheetBehavior.peekHeight = 670

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
            v.setMediaController(MediaController(context))
            val parse = Uri.parse("android.resource://" + context?.applicationContext?.packageName +"/"+ R.raw.video_iron_man)
            v.setVideoURI(parse)
            v.start()
        }

        mView.imageView.setOnClickListener {
            v.stopPlayback()
            v.clearFocus()
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.state =BottomSheetBehavior.STATE_HIDDEN
        }




        return mView
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
