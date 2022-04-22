package com.example.harumub_front


data class LoginResult(var id: String, var name: String, var record: Boolean)

data class MakeRoomResult(var roomCode: String)

data class EmailResult(var code: String)

data class WatchListResult(var title: String, var poster: String)

data class WatchResult(var title: String, var poster : String, var genres: String, var concentration: String,
                       var emotion_array : List<Emotion>, var highlight_array: List<Highlight>,
                       var rating : Float, var comment : String, var sleepingCount: Int)

data class Emotion(val HAPPY : Int, val SAD : Int, val ANGRY : Int, val CONFUSED : Int,
                   val DISGUSTED : Int, val SURPRISED : Int, val FEAR : Int)

data class Highlight(val time: Int, val emotion: String, val emotion_diff: Int)

data class Recommend2Result(var userId: String, var title: String, var poster: String)

data class EyeTrackResult(var userid: String, var movieTitle: String, var time: String, var concentration: Number)

data class MovieCollection(var title: String, var runningTime: Number, var genres: String, var poster: String)