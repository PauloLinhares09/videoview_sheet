package br.com.actaholding.mediaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.actaholding.mediaplayer.core.EmpiricusMedia
import br.com.actaholding.mediaplayer.core.EmpiricusMediaStateCallback
import br.com.actaholding.mediaplayer.core.MediaType
import br.com.actaholding.mediaplayer.core.StreamType
import br.com.actaholding.mediaplayer.R

class TestActivity : AppCompatActivity() {

    val empiricusMedia : EmpiricusMedia? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        EmpiricusMedia.Builder(this)
            .containerShowMedia(R.id.container)
            .setMediaType(MediaType.VIDEO, StreamType.HLS)
//            .setUri(Uri.parse("https://google.com/video"))
            .setContentLayout(R.layout.layout_video_view)
            .callBackMediaState(object : EmpiricusMediaStateCallback {
                override fun stateFromMedia(state : Int) {
                    Log.i("TAG", "state from callback: " + state)
                }
            })
            .build()



    }


    override fun onStop() {
        super.onStop()
        empiricusMedia?.onStop()
    }


    override fun onPause() {
        super.onPause()
        empiricusMedia?.onPause()
    }
}
