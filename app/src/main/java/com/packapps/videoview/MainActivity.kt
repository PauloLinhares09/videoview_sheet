package com.packapps.videoview

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.packapps.videoview.core.*
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

        val contentData = ContentData(
            "id-here",
            "Title dynamic",
            "Description",
            mutableListOf(ContentData.Author(
                "",
                "Felipe Miranda",
                "https://photo",
                "https://photoLarge",
                "email@empiricus.com.br",
                "Author desta serie" )), "https://thumbnails",
            "11/01/2019 17h00",
            mutableListOf(
                ContentData.NextMedia(
                    "id-item",
                    "Some media: Video/Podcast",
                    "9:11",
                    "https://thumbnails",
                    MediaType.VIDEO.toString()
                )))

        EmpiricusMedia.Builder(this)
            .containerShowMedia(R.id.containerMedia)
            .setMediaType(MediaType.VIDEO, StreamType.HLS)
//            .setUri(getString(R.string.media_url_mp4))
            .setUri("https://player.vimeo.com/external/310118574.m3u8?s=916d76157199bfe02c7daa8a735a4b84a9c9a038&oauth2_token_id=1018475342")
            .setContentLayout(R.layout.layout_video_view)
            .setContentData(contentData)
            .callBackMediaState(object : EmpiricusMediaStateCallback {
                override fun stateFromMedia(state : Int) {
                    Log.i("TAG", "state from callback: " + state)
                }
            })
            .callbackItemFromContentViewClicked(object : EmpiricusMediaItemClicked{
                override fun itemClicked(actionClick: ActionClick) {
                    Log.i("TAG", "item clicked: " + actionClick.toString())
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
