package br.com.actaholding.mediaplayer.utils

import java.lang.Exception

object Utils{

    fun truncateText(text : String, length : Int) : String{
        try {
            return text.substring(0, length) + "..."
        }catch (e : Exception){
            return text
        }
    }


    fun formatDuration(duration : Int?) : String{
        var durationStr : String = ""
        duration?.let {
            if (duration <= 60){
                if (duration.toString().length == 1)
                    durationStr = "00:0${duration}"

                durationStr = "00:${duration}"
            }
            else if(duration > 60 && duration <= 3600){
                var minute : String = (duration/60).toInt().toString()
                var second : String = (duration%60).toInt().toString()

                if (minute.length == 1)
                    minute = "0${minute}"

                if (second.length == 1)
                    second = "0${second}"

                durationStr = "${minute}:${second}"

            }else{
                var hour : String = (duration/3600).toInt().toString()
                var minutes : String = ((duration%3600)/(60)).toString()
                if (minutes.length == 1)
                    minutes = "0${minutes}"
                var seconds : String = ((duration%3600)%(60)).toString()
                if (seconds.length == 1)
                    seconds = "0${seconds}"

                durationStr = "${hour}:${minutes}:${seconds}"
            }

        }

        return durationStr
    }

}