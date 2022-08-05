package com.example.harumub_front

// 회원가입
data class EmailResult(var code: String)

// 로그인
data class LoginResult(var id: String, var name: String, var record: Boolean, var reco1: Recommend1,
                       var reco2_1: Recommend2, var reco2_2: Recommend2,
                       var reco2_3: Recommend2, var reco2_4: Recommend2,
                       var reco2_5: Recommend2, var reco3: Recommend3,
                       var reco4: Recommend4, var reco6: Recommend6) //var reco5: Recommend5,

data class Recommend1(var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)

data class Recommend2(var userId: String, var title: ArrayList<String>, var poster: ArrayList<String>)
// 위 아래가 다른 것이 무엇인지 확인 후 정리 바람
data class Recommend2Result(var userId: String, var title: String, var poster: String)

data class Recommend3(var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)

// 연도별 영화 추천
data class Recommend4(var year: String, var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)
// 리메이크 작품 추천
data class Recommend5(var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)
// 고전 TOP 10
data class Recommend6(var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)

// 혼자보기
data class WatchAloneMovie(var genres: String, var poster: String)

data class WatchResult(var date : String, var title: String, var poster : String, var genres: String,
                       var concentration: String, var highlight_time : String,
                       var emotion_count_array : List<Emotion>, var highlight_array: List<Highlight>,
                       var rating : Float, var comment : String, var sleepingCount: Int,
                       var remake : Boolean, var remakeTitle : String, var remakePoster : String)

data class Emotion(val HAPPY : Int, val SAD : Int, val ANGRY : Int, val CONFUSED : Int,
                   val DISGUSTED : Int, val SURPRISED : Int, val FEAR : Int)

data class Highlight(val time: String, val emotion_diff: Float)

data class WatchListResult(var title: ArrayList<String>, var poster: ArrayList<String>)

// 같이보기
data class MakeRoomResult(var roomCode: String, var roomToken: String)

data class EnterRoomResult(var roomToken: String)

data class WatchTogether(var emotion_count_array : List<Emotion>) // 실시간 감정

// 아래 두 데이터 클래스는 사용되지 않음 >> 혼자보기 관련으로 보임 확인 후 삭제 바람
data class EyeTrackResult(var userid: String, var movieTitle: String, var time: String, var concentration: Number)
data class MovieCollection(var title: String, var runningTime: Number, var genres: String, var poster: String)

// SearchData, MovieModel 파일을 여기에 선언해두기
//data class SearchData(var title : ArrayList<String>, var poster : ArrayList<String>,
//                      var runningTime : ArrayList<Int>)

//data class MovieModel (var movieTitle: String, var moviePoster : String, var movieRunningTime : Int)