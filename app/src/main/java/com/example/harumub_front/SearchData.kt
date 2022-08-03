package com.example.harumub_front

import android.media.Image

/*
data class SearchData(
    var movieTitle : String,
    var image : String, // Int > String (poster url)
)
*/

data class SearchData(
    var title : ArrayList<String>,
    var poster : ArrayList<String>,
    var runningTime : ArrayList<Int>,
)
