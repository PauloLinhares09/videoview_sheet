package com.packapps.videoview.utils

import java.lang.Exception

object Utils{

    fun truncateText(text : String, length : Int) : String{
        try {
            return text.substring(0, length) + "..."
        }catch (e : Exception){
            return text
        }
    }

}