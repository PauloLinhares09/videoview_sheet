package com.packapps.videoview.core

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Handler
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.Util
import com.packapps.videoview.PlayerViewSheetFragment

class EmpiricusMedia{
    private var containerLayout: Int? = null
    private lateinit var mediaStateCallback: EmpiricusMediaStateCallback
    private var context : Context? = null
    private var contentLayout : Int? = null
    private var mediaType : MediaType? = null
    private var uri : Uri? = null
    private var playerHomeFragment : PlayerViewSheetFragment? = null

    private fun execute(){
        //Check the values required TODO



        //Inflate the Fragment in container informed
        playerHomeFragment = PlayerViewSheetFragment.newInstance()
        val t = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        t.replace(containerLayout!!, playerHomeFragment!!)
        t.commit()

        //Get observable for listen media callback state
        Handler().postDelayed({
            val viewModelVideoPlayer = playerHomeFragment?.getObservableViewModel()
            viewModelVideoPlayer?.stateVideo?.observe(context as FragmentActivity, Observer {
                mediaStateCallback?.stateFromMedia(it)

            })
        }, 8000)





    }

    fun onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    fun onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    fun releasePlayer() {
        playerHomeFragment?.releasePlayer()

    }


    companion object class Builder{
        private var empiricusMedia : EmpiricusMedia

        constructor(context: Context){
            empiricusMedia = EmpiricusMedia()
            empiricusMedia.context = context
        }

        fun containerShowMedia(containerLayout : Int) : Builder{
            empiricusMedia.containerLayout = containerLayout

            return this
        }


        fun setContentLayout(contentLayout: Int) : Builder {
            empiricusMedia.contentLayout = contentLayout

            return this
        }

        fun setMediaType(mediaType : MediaType) : Builder{
            empiricusMedia.mediaType = mediaType

            return this
        }

        fun putUri(uri: Uri?): Builder {
            empiricusMedia.uri = uri

            return this
        }

        fun callBackMediaState(empiricusMediaCallback : EmpiricusMediaStateCallback) : Builder{
            empiricusMedia.mediaStateCallback = empiricusMediaCallback

            return this

        }






        fun build() : EmpiricusMedia{
            empiricusMedia.execute()
            return empiricusMedia
        }

    }


}


enum class MediaType{
    VIDEO,
    PODCAST
}


interface EmpiricusMediaStateCallback{
    fun stateFromMedia(state: Int)
}
