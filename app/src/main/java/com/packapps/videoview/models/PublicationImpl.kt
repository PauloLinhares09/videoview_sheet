package com.packapps.videoview.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class PublicationImpl(
    override val publicationId: String?,
    override val slug: String?,
    override val title: String?,
    override val description: String?,
    override val thumbnail: String?,
    override val read: Boolean?,
    override val released: String?,
    override val releaseDate: Date?,
    override val primaryContent: ContentImpl?,
    override val taxonomy: TaxonomyImpl?,
    override var hasAudioSecundaryContent: Boolean?
) : Publication, Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        null,
        parcel.readParcelable(ContentImpl::class.java.classLoader),
        parcel.readParcelable(TaxonomyImpl::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    class ContentImpl(
        override val contentId: String?,
        override val title: String?,
        override val authors: MutableList<AuthorImpl>?,
        override val contentValue: ContentValueImpl?,
        override val contentType: ContentType?,
        override val duration: Int?
    ) : Content, Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(AuthorImpl),
            parcel.readParcelable(ContentValueImpl::class.java.classLoader),
            null,
            parcel.readValue(Int::class.java.classLoader) as? Int
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(contentId)
            parcel.writeString(title)
            parcel.writeTypedList(authors)
            parcel.writeParcelable(contentValue, flags)
            parcel.writeValue(duration)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ContentImpl> {
            override fun createFromParcel(parcel: Parcel): ContentImpl {
                return ContentImpl(parcel)
            }

            override fun newArray(size: Int): Array<ContentImpl?> {
                return arrayOfNulls(size)
            }
        }
    }


    class AuthorImpl(
        override val authorId: String?,
        override val name: String?,
        override val photoUrl: String?,
        override val photoLargeUrl: String?,
        override val email: String?,
        override val description: String?
    ) : Author, Parcelable {
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

        companion object CREATOR : Parcelable.Creator<AuthorImpl> {
            override fun createFromParcel(parcel: Parcel): AuthorImpl {
                return AuthorImpl(parcel)
            }

            override fun newArray(size: Int): Array<AuthorImpl?> {
                return arrayOfNulls(size)
            }
        }
    }


    class ContentValueImpl(override val type: ContentValueType?, override val value: String?) : ContentValue, Parcelable {
        constructor(parcel: Parcel) : this(
            null,
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(value)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ContentValueImpl> {
            override fun createFromParcel(parcel: Parcel): ContentValueImpl {
                return ContentValueImpl(parcel)
            }

            override fun newArray(size: Int): Array<ContentValueImpl?> {
                return arrayOfNulls(size)
            }
        }
    }

    class TaxonomyImpl(
        override val id: String?,
        override val slug: String?,
        override val name: String?,
        override val root: TaxonomyImpl?,
        override val authors: MutableList<AuthorImpl>?,
        override val taxonomies: MutableList<TaxonomyImpl>?,
        override val type: TaxonomyType?,
        override val target: String?,
        override val favorite: Boolean?
    ) : Taxonomy, Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(TaxonomyImpl::class.java.classLoader),
            parcel.createTypedArrayList(AuthorImpl),
            parcel.createTypedArrayList(CREATOR),
            null,
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(slug)
            parcel.writeString(name)
            parcel.writeParcelable(root, flags)
            parcel.writeTypedList(authors)
            parcel.writeTypedList(taxonomies)
            parcel.writeString(target)
            parcel.writeValue(favorite)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<TaxonomyImpl> {
            override fun createFromParcel(parcel: Parcel): TaxonomyImpl {
                return TaxonomyImpl(parcel)
            }

            override fun newArray(size: Int): Array<TaxonomyImpl?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(publicationId)
        parcel.writeString(slug)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(thumbnail)
        parcel.writeValue(read)
        parcel.writeString(released)
        parcel.writeParcelable(primaryContent, flags)
        parcel.writeParcelable(taxonomy, flags)
        parcel.writeValue(hasAudioSecundaryContent)
    }

    override fun describeContents(): Int {
        return 0
    }



    companion object CREATOR : Parcelable.Creator<PublicationImpl> {
        override fun createFromParcel(parcel: Parcel): PublicationImpl {
            return PublicationImpl(parcel)
        }

        override fun newArray(size: Int): Array<PublicationImpl?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "PublicationImpl(publicationId=$publicationId, slug=$slug, title=$title, description=$description, thumbnail=$thumbnail, read=$read, released=$released, releaseDate=$releaseDate, primaryContent=$primaryContent, taxonomy=$taxonomy, hasAudioSecundaryContent=$hasAudioSecundaryContent)"
    }


}







