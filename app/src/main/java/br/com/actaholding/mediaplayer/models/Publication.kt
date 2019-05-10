package br.com.actaholding.mediaplayer.models

import java.util.*

interface Publication {
    val publicationId : String?
    val slug : String?
    val title : String?
    val description : String?
    val thumbnail : String?
    val read : Boolean?
    val released : String?
    val releaseDate : Date?
    val primaryContent : Content?
    val taxonomy: Taxonomy?
    var hasAudioSecundaryContent : Boolean?
}


interface Content {

    val contentId : String?
    val title : String?
    val authors : List<Author>?
    val contentValue : ContentValue?
    val contentType : ContentType?
    val duration : Int?
}

interface Taxonomy {
    val id : String?
    val slug : String?
    val name : String?
    val root : Taxonomy?
    val authors : List<Author>?
    val taxonomies : List<Taxonomy>?
    val type: TaxonomyType?
    val target: String?
    val favorite : Boolean?
}

interface Author {

    val authorId : String?
    val name : String?
    val photoUrl : String?
    val photoLargeUrl : String?
    val email : String?
    val description : String?
}


interface ContentValue {

    val type : ContentValueType?
    val value : String?
}

enum class ContentValueType {

    VIDEO, PUBLICATION, ALERT, UNKNOWN
}



enum class TaxonomyType {
    LIST, PUBLICATION, FEED, DYNAMIC_TABLE
}

enum class ContentType {
    VIDEO, REPORT, ALERT, AUDIO_FILE, PODCAST, DOWNLOAD, UNKNOWN, PDF, EXTERNAL_LINK{
        override fun toString(): String {
            return "Relatório/Newsletter PDF"
        }
    }
}