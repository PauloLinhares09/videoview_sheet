package br.com.actaholding.mediaplayer.podcast

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.actaholding.mediaplayer.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_podcast.*

class PodcastActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    var mCurrentState = State.EXPANDED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)
        setSupportActionBar(toolbar)


        collapsingToolbar.setExpandedTitleColor(ActivityCompat.getColor(this, android.R.color.transparent))
        collapsingToolbar.setCollapsedTitleTextColor(ActivityCompat.getColor(this, android.R.color.white))

        app_bar.addOnOffsetChangedListener(this)

    }



    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        appBarLayout?.let {

            if (verticalOffset == 0) {
                if (mCurrentState != State.EXPANDED) {
                    tvSubTitleCollapsed.visibility = View.GONE
                }
                mCurrentState = State.EXPANDED
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    tvSubTitleCollapsed.visibility = View.VISIBLE

                }
                mCurrentState = State.COLLAPSED
            } else {
                if (mCurrentState != State.IDLE) {
//                    toolbar.elevation = 5F
//                    containerControlls.elevation = 0F
                }
                mCurrentState = State.IDLE
            }
        }
    }



    enum class State{
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}
