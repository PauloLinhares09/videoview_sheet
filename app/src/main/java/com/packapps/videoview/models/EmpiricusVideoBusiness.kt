package com.packapps.videoview.models

import android.os.Parcel
import android.os.Parcelable

class EmpiricusVideoBusiness() : Parcelable{
    var title : String? = null
    var description : String? = null
    var uri : String = ""
    var author_image : String? = null
    var serie : String? = null
    var timeAgo : String? = null
    var thumbSmall : String? = null
    var itemOfplayList : MutableList<ItemOfPlayList>? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        description = parcel.readString()
        uri = parcel.readString()
        author_image = parcel.readString()
        serie = parcel.readString()
        timeAgo = parcel.readString()
        thumbSmall = parcel.readString()
    }


    class ItemOfPlayList() :  Parcelable{
        var id : Long? = null
        var description : String? = null
        var thumbnails : String? = null
        var timeLenght : String? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readValue(Long::class.java.classLoader) as? Long
            description = parcel.readString()
            thumbnails = parcel.readString()
            timeLenght = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(id)
            parcel.writeString(description)
            parcel.writeString(thumbnails)
            parcel.writeString(timeLenght)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ItemOfPlayList> {
            override fun createFromParcel(parcel: Parcel): ItemOfPlayList {
                return ItemOfPlayList(parcel)
            }

            override fun newArray(size: Int): Array<ItemOfPlayList?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(uri)
        parcel.writeString(author_image)
        parcel.writeString(serie)
        parcel.writeString(timeAgo)
        parcel.writeString(thumbSmall)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EmpiricusVideoBusiness> {
        override fun createFromParcel(parcel: Parcel): EmpiricusVideoBusiness {
            return EmpiricusVideoBusiness(parcel)
        }

        override fun newArray(size: Int): Array<EmpiricusVideoBusiness?> {
            return arrayOfNulls(size)
        }
    }
}


data class Evaluation(
    val comment: String,
    val taxonomySlug: String,
    val thumbs: String
) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun toString(): String {
        return "Evaluation(comment='$comment', taxonomySlug='$taxonomySlug', thumbs='$thumbs')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(comment)
        parcel.writeString(taxonomySlug)
        parcel.writeString(thumbs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Evaluation> {
        override fun createFromParcel(parcel: Parcel): Evaluation {
            return Evaluation(parcel)
        }

        override fun newArray(size: Int): Array<Evaluation?> {
            return arrayOfNulls(size)
        }
    }
}