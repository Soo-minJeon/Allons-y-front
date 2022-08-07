package com.example.harumub_front

import java.io.Serializable

// 회원가입
data class EmailResult(var code: String)

// 로그인
data class LoginResult(var id: String, var name: String, var record: Boolean, var reco1: Recommend1,
                       var reco2_1: Recommend2, var reco2_2: Recommend2,
                       var reco2_3: Recommend2, var reco2_4: Recommend2,
                       var reco2_5: Recommend2, var reco3: Recommend3,
                       var reco4: Recommend4, var reco6: Recommend6)

// 사용자 선호도 기반 추천
data class Recommend1(var titleArray: ArrayList<String>, var posterArray: ArrayList<String>)

// 유사 사용자 추천
data class Recommend2(var userId: String, var title: ArrayList<String>, var poster: ArrayList<String>)

// 선호 배우 출연 영화 추천
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
                       var emotion_count_array : ArrayList<Emotion>, var highlight_array: ArrayList<Highlight>,
                       var rating : Float, var comment : String, var sleepingCount: Int,
                       var remake : Boolean, var remakeTitle : String, var remakePoster : String)

data class Emotion(val HAPPY : Int, val SAD : Int, val ANGRY : Int, val CONFUSED : Int,
                   val DISGUSTED : Int, val SURPRISED : Int, val FEAR : Int) : Serializable

data class Highlight(val time: String, val emotion_diff: Float) : Serializable

data class WatchListResult(var title: ArrayList<String>, var poster: ArrayList<String>)

// 같이보기
data class MakeRoomResult(var roomCode: String, var roomToken: String)

data class EnterRoomResult(var roomToken: String)

// 같이보기 - 실시간 감정
data class WatchTogether(var emotion_count_array : List<Emotion>)

// 영화 검색 페이지 - 전체 영화 목록
data class SearchData(var title : ArrayList<String>, var poster : ArrayList<String>,
                      var runningTime : ArrayList<Int>)

// 영화 검색 페이지 - 개별 영화 형식
data class MovieModel (var movieTitle: String, var moviePoster : String, var movieRunningTime : Int)