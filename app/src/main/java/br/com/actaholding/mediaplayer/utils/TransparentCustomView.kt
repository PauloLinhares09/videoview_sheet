package br.com.actaholding.mediaplayer.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide.init

class TransparentCustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private var listener : OnOrientationListener? = null


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        listener?.onMeasureCalled()
    }

    fun setListenerToListenOrientation(listener : OnOrientationListener){
        this.listener = listener
    }


    interface OnOrientationListener{
        fun onMeasureCalled()
    }

}