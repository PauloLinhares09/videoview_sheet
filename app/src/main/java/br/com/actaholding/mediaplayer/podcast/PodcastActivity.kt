package br.com.actaholding.mediaplayer.podcast

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.actaholding.mediaplayer.R
import br.com.actaholding.mediaplayer.utils.Utils
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_podcast.*
import kotlinx.android.synthetic.main.content_podcast.*

class PodcastActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    var mCurrentState = State.EXPANDED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)
        setSupportActionBar(toolbar)

        Log.i("TAG", "densidade: ${Utils.getDensityDPIFromDevice(resources)}")

        if (Utils.getDensityDPIFromDevice(resources)  == 480)
            applyShaderGradient(399F)
        else{
            applyShaderGradient(599F)
        }

        collapsingToolbar.setExpandedTitleColor(ActivityCompat.getColor(this, android.R.color.transparent))
        collapsingToolbar.setCollapsedTitleTextColor(ActivityCompat.getColor(this, android.R.color.white))

        app_bar.addOnOffsetChangedListener(this)

    }




    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        appBarLayout?.let {
            Log.i("TAG", "verticalOffset:L ${verticalOffset}")

            if (verticalOffset == 0) {
                if (mCurrentState != State.EXPANDED) {
                    tvSubTitleCollapsed.visibility = View.GONE
                    applyShaderGradient(599F)
                }
                mCurrentState = State.EXPANDED
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    tvSubTitleCollapsed.visibility = View.VISIBLE
                    applyShaderGradient(null)

                }
                mCurrentState = State.COLLAPSED
            } else {
                if (mCurrentState != State.IDLE) {
//                    toolbar.elevation = 5F
//                    containerControlls.elevation = 0F
                    applyShaderGradient(null)
                }
                mCurrentState = State.IDLE
            }
        }
    }


    private fun applyShaderGradient(y1: Float?) {
        val shader: Shader = LinearGradient(
            0F,
            0F,
            0F,
            y1?:0F,
            intArrayOf(Color.WHITE, if (y1 != null) Color.TRANSPARENT else Color.WHITE),
            floatArrayOf(0F, 1F),
            Shader.TileMode.CLAMP
        )
        tvContentDescription.paint.setShader(shader)
        tvContentDescription.invalidate()
    }



    enum class State{
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}
