package com.packapps.videoview

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_view_list_fragment.view.*


class ViewListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_view_list, container, false)

        mView.card1.setOnClickListener {
            VideoBottomSheetFragment().show(activity?.supportFragmentManager, "VIDEO_FLAG")
        }

        return mView
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            ViewListFragment()

    }
}
