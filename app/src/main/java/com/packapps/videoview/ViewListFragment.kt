package com.packapps.videoview

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.content_view_list_fragment.view.*
import kotlinx.android.synthetic.main.fragment_view_list.*
import kotlinx.android.synthetic.main.fragment_view_list.view.*


class ViewListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_view_list, container, false)



        mView.card1.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(mView.bottomSheetVideo)
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.peekHeight = 600
        }

        return mView
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            ViewListFragment()

    }
}
