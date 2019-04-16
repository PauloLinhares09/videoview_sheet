package com.packapps.videoview

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.util.Util
import com.packapps.videoview.core.EmpiricusMedia
import com.packapps.videoview.core.EmpiricusMediaStateCallback
import com.packapps.videoview.core.MediaType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.area_video_expanded.*

class MainActivity : AppCompatActivity() {
    lateinit var playerHomeFragment : PlayerViewSheetFragment

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                testOpenBlankFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                testOpenBlankFragment()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                testOpenBlankFragment()
                openMediaPlayer()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun openMediaPlayer(){

        EmpiricusMedia.Builder(this)
            .containerShowMedia(R.id.containerMedia)
            .setMediaType(MediaType.VIDEO)
            .putUri(Uri.parse("https://google.com/video"))
            .setContentLayout(R.layout.layout_video_view)
            .callBackMediaState(object : EmpiricusMediaStateCallback {
                override fun stateFromMedia(state : Int) {
                    Log.i("TAG", "state from callback: " + state)
                }
            })
            .build()
    }

    private fun openHome(playerHomeFragment: PlayerViewSheetFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, playerHomeFragment)
        transaction.commit()
    }

    private fun testOpenBlankFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, BlankFragment.newInstance())
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerHomeFragment = PlayerViewSheetFragment.newInstance()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        openHome(playerHomeFragment)

    }

    override fun onUserLeaveHint() {
        if(Util.SDK_INT >= Build.VERSION_CODES.O){
            enterPictureInPictureMode(with(PictureInPictureParams.Builder()){
                val width = 16
                val height = 9
                setAspectRatio(Rational(width, height))
                build()
            })
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        playerHomeFragment.playerView.useController = !isInPictureInPictureMode
    }

}
