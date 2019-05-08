package com.packapps.videoview

import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.packapps.videoview.core.*
import com.packapps.videoview.models.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_video_bottomsheet_emp.*
import java.util.*

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

    var empiricusMedia : EmpiricusMedia? = null
    fun openMediaPlayer(){

        val contentData = ContentData(
            "id-here",
            "Title dynamic",
            "Description",
            mutableListOf(ContentData.Author(
                "",
                "Felipe Miranda",
                "https://pbs.twimg.com/profile_images/1112802506902568962/1M4O48vX_400x400.png",
                "https://pbs.twimg.com/profile_images/1112802506902568962/1M4O48vX_400x400.png",
                "email@empiricus.com.br",
                "Author desta serie" )), "https://thumbnails",
            "11/01/2019 17h00",
            null)

        empiricusMedia = EmpiricusMedia.Builder(this)
            .containerShowMedia(R.id.containerMedia)
            .setContentLayout(R.layout.layout_video_view)
            .callBackMediaState(object : EmpiricusMediaStateCallback {
                override fun stateFromMedia(state : Int) {
                    Log.i("TAG", "state from callback: " + state)
                }
            })
            .callbackItemFromContentViewClicked(object : EmpiricusMediaItemClicked{
                override fun itemClicked(actionClick: ActionClick) {
                    Log.i("TAG", "item clicked: " + actionClick.toString())
                    if (actionClick.viewId == R.id.emp_like) {
                        Handler().postDelayed({
                            empiricusMedia?.updateEvaluation(Evaluation("", "", "up"))
                        }, 1000)

                    }else if (actionClick.viewId == R.id.emp_dont_like){
                        Handler().postDelayed({
                            empiricusMedia?.updateEvaluation(Evaluation("", "", "down"))
                        }, 1000)
                    }else if (actionClick.viewId == R.id.emp_favourite){
                        Handler().postDelayed({
                            empiricusMedia?.updateFavourite(false)
                        }, 1000)
                    } else if (actionClick.viewId == R.id.emp_audio_version){
                        empiricusMedia?.killEmpiricusMedia()
                    } else if (actionClick.viewId == R.id.emp_item_playlist){
                        Log.i("TAG", "id from playlist clicked: ${actionClick.itemIdCurrent}")
                    }

                }

                override fun itemFromPlayList(publication: Publication) {
                    Log.i("TAG", "MainActivity item from playlist: ${publication.toString()}")
                    openMediaPlayer()
                }
            })
            .build()


        Handler().postDelayed({
            empiricusMedia?.setMediaType(MediaType.VIDEO, StreamType.HLS)
            empiricusMedia?.setUri("https://player.vimeo.com/external/310118574.m3u8?s=916d76157199bfe02c7daa8a735a4b84a9c9a038&oauth2_token_id=1018475342")
            empiricusMedia?.setContentData(contentData)

            //Exute
            empiricusMedia?.executeDelaued()
        }, 1500)



        Handler().postDelayed({
            var listTest : MutableList<PublicationImpl> = mutableListOf()
            listTest.add(PublicationImpl("id", "slug", "Title here for you",
                "O text grande parav ver se vai quebrar a linha no layout e nao ficar cagado.",
                "https://pbs.twimg.com/profile_images/1112802506902568962/1M4O48vX_400x400.png",
                false,
                "11/01/2019", Date(122325454),
                PublicationImpl.ContentImpl("jskdjs",
                    "Title content",
                    mutableListOf(PublicationImpl.AuthorImpl("id", "Paulo Linhares", "", "", "", "Desciption for Author")),
                    PublicationImpl.ContentValueImpl(ContentValueType.VIDEO, "video"),
                    ContentType.VIDEO, 1200
                    ),
                PublicationImpl.TaxonomyImpl(
                    "id-taxonomy",
                    "slug-taxonomy",
                    "name",
                    null,
                    mutableListOf(PublicationImpl.AuthorImpl("id", "Paulo Linhares", "", "", "", "Desciption for Author")),
                    null,
                    TaxonomyType.LIST,
                    null,
                    false
                ),
                false
            )
            )
            listTest.add(PublicationImpl("id", "slug", "Title here for you",
                "O text grande parav ver se vai quebrar a linha no layout e nao ficar cagado.",
                "https://pbs.twimg.com/profile_images/1112802506902568962/1M4O48vX_400x400.png",
                false,
                "11/01/2019", Date(122325454),
                PublicationImpl.ContentImpl("jskdjs",
                    "Title content",
                    mutableListOf(PublicationImpl.AuthorImpl("id", "Paulo Linhares", "", "", "", "Desciption for Author")),
                    PublicationImpl.ContentValueImpl(ContentValueType.VIDEO, "video"),
                    ContentType.VIDEO, 1200
                    ),
                PublicationImpl.TaxonomyImpl(
                    "id-taxonomy",
                    "slug-taxonomy",
                    "name",
                    null,
                    mutableListOf(PublicationImpl.AuthorImpl("id", "Paulo Linhares", "", "", "", "Desciption for Author")),
                    null,
                    TaxonomyType.LIST,
                    null,
                    false
                ),
                false
            )
            )


            empiricusMedia?.replacePlayListAssociated(listTest)

            //icons Evaluatiosn
            empiricusMedia?.updateEvaluation(Evaluation("", "", "up"))

            //Icon Favourite
            empiricusMedia?.updateFavourite(false)

        }, 3900)
    }

    override fun onBackPressed() {
        if (empiricusMedia != null){
            if (empiricusMedia?.stateBottomSheet == BottomSheetBehavior.STATE_EXPANDED){
                empiricusMedia?.stateToCollapsed()
                return
            }
        }
        super.onBackPressed()
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
