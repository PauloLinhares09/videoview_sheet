package com.packapps.videoview

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.packapps.videoview.core.EmpiricusMedia
import com.packapps.videoview.core.EmpiricusMediaItemClicked
import com.packapps.videoview.core.EmpiricusMediaStateCallback
import com.packapps.videoview.core.MediaType
import kotlinx.android.synthetic.main.activity_main.*

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
            .setUri(getString(R.string.media_url_mp4))
            .setContentLayout(R.layout.layout_video_view)
            .callBackMediaState(object : EmpiricusMediaStateCallback {
                override fun stateFromMedia(state : Int) {
                    Log.i("TAG", "state from callback: " + state)
                }
            })
            .callbackItemFromContentViewClicked(object : EmpiricusMediaItemClicked{
                override fun itemClicked(id: Int) {
                    Log.i("TAG", "item clicked: " + id)
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
        testOpenBlankFragment()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

}
