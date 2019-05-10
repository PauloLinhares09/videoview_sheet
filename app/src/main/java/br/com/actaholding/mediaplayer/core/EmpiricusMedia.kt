package br.com.actaholding.mediaplayer.core

import android.content.Context
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import br.com.actaholding.mediaplayer.PlayerViewSheetFragment
import br.com.actaholding.mediaplayer.models.Evaluation
import br.com.actaholding.mediaplayer.models.Publication
import br.com.actaholding.mediaplayer.models.PublicationImpl

class EmpiricusMedia{
    private var itemClickedContentView: EmpiricusMediaItemClicked? = null
    private var containerLayout: Int? = null
    private var mediaStateCallback: EmpiricusMediaStateCallback? = null
    private var context : Context? = null
    private var contentLayout : Int? = null
    private var mediaType : MediaType? = null
    private var contentData : ContentData? = null
    private lateinit var evaluation: Evaluation
    private var streamType: StreamType? = null
    private var uri : String? = null
    private var playerHomeFragment : PlayerViewSheetFragment? = null
    private var peekHeight : Int = 550

    private fun execute(){
        //Check the values required TODO

        //Inflate the Fragment in container informed
        playerHomeFragment = PlayerViewSheetFragment.newInstance(uri, peekHeight, streamType = streamType, contentData = contentData)
        val t = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        t.replace(containerLayout!!, playerHomeFragment!!, PlayerViewSheetFragment::class.java.simpleName)
        t.commit()

        //Get observable for listen media callback state
        Handler().postDelayed({
            val viewModelVideoPlayer = playerHomeFragment?.getObservableViewModel()
            //### Observer state Media
            viewModelVideoPlayer?.stateVideo?.observe(context as FragmentActivity, Observer {
                mediaStateCallback?.stateFromMedia(it)

            })
            //### Observer click item on contentView
            viewModelVideoPlayer?.buttonClicked?.observe(context as FragmentActivity, Observer {
                itemClickedContentView?.itemClicked(ActionClick(it, contentData?.id))

            })

            //Observer state bottom Sheet
            viewModelVideoPlayer?.stateBottomSheet?.observe(context as FragmentActivity, Observer {
                itemClickedContentView?.stateSheetEmpiricusMedia(it)
            })

            //Observer to click in item from playlist
            viewModelVideoPlayer?.itemPlayList?.observe(context as FragmentActivity, Observer {
                itemClickedContentView?.itemFromPlayList(it)
            })



        }, 500)





    }

    fun setMediaType(mediaType: MediaType, streamType: StreamType = StreamType.HLS) {
        this.mediaType = mediaType
        this.streamType = streamType
    }

    fun setUri(uri: String?) {
        this.uri = uri
    }

    fun setContentData(contentData: ContentData) {
        this.contentData = contentData
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

    fun releasePlayer() { //TODO Cancel Listeners
        playerHomeFragment?.releasePlayer()

    }

    fun killEmpiricusMedia(){
        playerHomeFragment?.killFragment()
    }

    fun stateToCollapsed(paused : Boolean = false) {
        playerHomeFragment?.bottomSheetToCollapsed()
        if (paused == true)
            playerHomeFragment?.pausePlayer()
    }

    fun replacePlayListAssociated(playList: MutableList<PublicationImpl>) {
        playerHomeFragment?.replacePlayListAssociated(playList)
    }

    fun updateEvaluation(evaluation : Evaluation?){
//        playerHomeFragment?.updateEvaluationView(evaluation) //TODO enable when work with buttons
    }

    fun updateFavourite(isFavourite : Boolean){
//        playerHomeFragment?.updateFavouriteView(isFavourite)//TODO enable when work with buttons
    }

    fun executeDelaued() {
        execute()
    }


    companion object


    class Builder{
        private var empiricusMedia : EmpiricusMedia

        constructor(context: Context){
            empiricusMedia = EmpiricusMedia()
            empiricusMedia.context = context
        }

        fun containerShowMedia(containerLayout : Int) : Builder {
            empiricusMedia.containerLayout = containerLayout

            return this
        }


        fun setContentLayout(contentLayout: Int) : Builder {
            empiricusMedia.contentLayout = contentLayout

            return this
        }

        fun setMediaType(mediaType: MediaType, streamType: StreamType = StreamType.HLS) : Builder {
            empiricusMedia.mediaType = mediaType
            empiricusMedia.streamType = streamType

            return this
        }

        fun setUri(uri: String?): Builder {
            empiricusMedia.uri = uri

            return this
        }

        fun setContentData(contentData: ContentData): Builder {
            empiricusMedia.contentData = contentData

            return this
        }

        fun setPeekHeight(peekHeight : Int) : Builder {
            empiricusMedia.peekHeight = peekHeight
            return this
        }

        fun callBackMediaState(empiricusMediaCallback : EmpiricusMediaStateCallback) : Builder {
            empiricusMedia.mediaStateCallback = empiricusMediaCallback

            return this
        }

        fun callbackItemFromContentViewClicked(empiricusMediaItemClicked: EmpiricusMediaItemClicked) : Builder {
            empiricusMedia.itemClickedContentView = empiricusMediaItemClicked

            return this
        }







        fun build() : EmpiricusMedia {
            empiricusMedia.execute()
            return empiricusMedia
        }



    }


}


enum class MediaType{
    VIDEO,
    PODCAST
}

enum class StreamType{
    HLS,
    MP4
}

class ContentData (
    val id : String?,
    val title : String?,
    val description : String?,
    val authors : MutableList<Author>?,
    val thumbnailsFull : String?,
    val timeAgoStr : String?,
    val next : MutableList<PublicationImpl>?
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Author),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(PublicationImpl.CREATOR)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(thumbnailsFull)
        parcel.writeTypedList(authors)
        parcel.writeString(timeAgoStr)
        parcel.writeTypedList(next)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContentData> {
        override fun createFromParcel(parcel: Parcel): ContentData {
            return ContentData(parcel)
        }

        override fun newArray(size: Int): Array<ContentData?> {
            return arrayOfNulls(size)
        }
    }



    class Author(
        val authorId : String?,
        val name : String,
        val photoUrl : String?,
        val photoLargeUrl : String?,
        val email : String?,
        val description : String?
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(authorId)
            parcel.writeString(name)
            parcel.writeString(photoUrl)
            parcel.writeString(photoLargeUrl)
            parcel.writeString(email)
            parcel.writeString(description)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Author> {
            override fun createFromParcel(parcel: Parcel): Author {

                return Author(parcel)
            }

            override fun newArray(size: Int): Array<Author?> {
                return arrayOfNulls(size)
            }
        }

    }


//    class NextMedia(
//        val id : String?,
//        val text : String?,
//        val time : String?,
//        val thumbnails : String?,
//        val mediaType: String?
//    ) : Parcelable {
//        constructor(parcel: Parcel) : this(
//            parcel.readString(),
//            parcel.readString(),
//            parcel.readString(),
//            parcel.readString(),
//            parcel.readString()
//        ) {
//        }
//
//        override fun writeToParcel(parcel: Parcel, flags: Int) {
//            parcel.writeString(id)
//            parcel.writeString(text)
//            parcel.writeString(time)
//            parcel.writeString(thumbnails)
//            parcel.writeString(mediaType)
//        }
//
//        override fun describeContents(): Int {
//            return 0
//        }
//
//        override fun toString(): String {
//            return "NextMedia(id=$id, text=$text, time=$time, thumbnails=$thumbnails, mediaType='$mediaType')"
//        }
//
//
//        companion object CREATOR : Parcelable.Creator<NextMedia> {
//            override fun createFromParcel(parcel: Parcel): NextMedia {
//                return NextMedia(parcel)
//            }
//
//            override fun newArray(size: Int): Array<NextMedia?> {
//                return arrayOfNulls(size)
//            }
//        }
//
//
//
//    }

}


interface EmpiricusMediaStateCallback{
    fun stateFromMedia(state: Int)
}

class ActionClick(val viewId : Int, val itemIdCurrent : String?){
    override fun toString(): String {
        return "ActionClick(viewId=$viewId, itemIdCurrent='$itemIdCurrent')"
    }
}

interface EmpiricusMediaItemClicked{
    fun itemClicked(id : ActionClick)
    fun itemFromPlayList(publication : Publication)
    fun stateSheetEmpiricusMedia(state : Int)
}
