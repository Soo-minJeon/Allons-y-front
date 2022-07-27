package com.example.harumub_front


data class LoginResult(var id: String, var name: String, var record: Boolean, var reco1: Recommend1,
                       var reco2_1: Recommend2, var reco2_2: Recommend2,
                       var reco2_3: Recommend2, var reco2_4: Recommend2,
                       var reco2_5: Recommend2, var reco3: Recommend3,
                       var reco4: Recommend4, var reco6: Recommend6) //var reco5: Recommend5,

data class Recommend1(var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)

data class Recommend2(var userId: String, var title: ArrayList<String>, var poster: ArrayList<String>)

data class Recommend3(var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)

// 연도별 영화 추천
data class Recommend4(var year: String, var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)

// 리메이크 작품 추천
data class Recommend5(var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)

// 고전 TOP 10
data class Recommend6(var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)

data class MakeRoomResult(var roomCode: String, var roomToken: String)

data class EnterRoomResult(var roomToken: String)

data class WatchTogether(var emotion_count_array : List<Emotion>) // 실시간 감정

data class EmailResult(var code: String)

data class WatchListResult(var title: ArrayList<String>, var poster: ArrayList<String>)

data class WatchResult(var title: String, var poster : String, var genres: String,
                       var concentration: String, var highlight_time : String,
                       var emotion_count_array : List<Emotion>, var highlight_array: List<Highlight>,
                       var rating : Float, var comment : String, var sleepingCount: Int)

data class Emotion(val HAPPY : Int, val SAD : Int, val ANGRY : Int, val CONFUSED : Int,
                   val DISGUSTED : Int, val SURPRISED : Int, val FEAR : Int)

data class Highlight(val time: String, val emotion_diff: Float)

data class Recommend2Result(var userId: String, var title: String, var poster: String)

data class EyeTrackResult(var userid: String, var movieTitle: String, var time: String, var concentration: Number)

data class MovieCollection(var title: String, var runningTime: Number, var genres: String, var poster: String)

data class WatchAloneMovie(var genres: String, var poster: String)

//data class SearchData(var title : ArrayList<String>, var poster : ArrayList<String>,
//                      var runningTime : ArrayList<Int>)