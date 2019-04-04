package com.packapps.videoview


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class VideoBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_video_view, container, false)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var d = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        d?.setOnShowListener(object : DialogInterface.OnShowListener{
            override fun onShow(dialog: DialogInterface?) {

                val bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?

                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        })

        return d
    }


}
