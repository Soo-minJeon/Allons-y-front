package com.example.harumub_front


data class LoginResult(var id: String, var password : String, var name: String)//, var number : String, var birth : String)

data class MakeRoomResult(var roomCode: String)

data class EmailResult(var code: String)

data class WatchListResult(var title: String, var poster: String)
/*
data class WatchResult(var title:String, var poster : String, var genres: String, var concentration: String,
                       var emotion : String, var highlight: String, var rating : Int, var comment : String)
*/
data class WatchResult(var userid: String, var movieTitle:String, var poster: String, var genres: String,
                       var concentration: Number, var highlight_time: String,
                       var highlight_array: Array<String>, var emotion_array: Array<Float>,
                       var rating : Float, var comment: String, var sleepingCount: Int)

data class Recommend2Result(var userId: String, var title: String, var poster: String) {
}

data class EyeTrackResult(var userid: String, var movieTitle: String, var time: String, var concentration: Number)

data class MovieCollection(var title: String, var runningTime: Number, var genres: String, var poster: String)