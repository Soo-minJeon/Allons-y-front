package com.example.harumub_front

import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.HashMap

interface RetrofitInterface {
    // 로그인 및 회원가입
    @POST("/login")
    fun executeLogin(@Body map: HashMap<String, String>): Call<LoginResult?>?

    @POST("/email")
    fun executeEmail(@Body map: HashMap<String, String>): Call<EmailResult?>?

    @POST("/signup")
    fun executeSignup(@Body map: HashMap<String, String>): Call<Void?>?

    // 아래 3개 확인 후 삭제 바람
    @POST("/recommend1")
    fun executeRecommend1(@Body map: HashMap<String, String>): Call<List<String>?>?

    @POST("/recommend2")
    fun executeRecommend2(@Body map: HashMap<String, String>): Call<List<Recommend2Result?>>?

    @POST("/sceneAnalyze")
    fun executeSceneAnalyze(@Body map: HashMap<String, String>): Call<List<String>?>?

    // 혼자보기
    @POST("/watchAloneStart")
    fun executeWatchAloneStart(@Body map: HashMap<String, String>): Call<Void?>?

    @POST("/watchImageCaptureEyetrack")
    fun executeWatchImageCaptureEyetrack(@Body map: HashMap<String, String>): Call<Void?>?

    @POST("/watchAloneEnd")
    fun executeWatchAloneEnd(@Body map: HashMap<String, String>): Call<WatchAloneMovie?>?

    @POST("addReview") // 추가: 리뷰 작성 후 버튼 클릭시 데이터 전달
    fun executeAddReview(@Body map: HashMap<String, String>): Call<Void?>?

    @POST("/watchresult")
    fun executeWatchResult(@Body map: HashMap<String, String>): Call<WatchResult?>?

    @POST("/getAllMovieList")
    fun executeGetAllMovieList(@Body map: HashMap<String, String>): Call<SearchData?>?

    @POST("/watchlist")
    fun executeWatchList(@Body map: HashMap<String, String>): Call<WatchListResult?>?

    // 같이보기
    @POST("/makeRoom")
    fun executeMakeRoom(@Body map: HashMap<String, String>): Call<MakeRoomResult?>?

    @POST("/enterroom")
    fun executeEnterRoom(@Body map: HashMap<String, String>): Call<EnterRoomResult?>?

    @POST("/watchTogetherImageCapture") // 캡처 및 감정 출력
    fun executeWatchTogetherImageCapture(@Body map: HashMap<String, String>): Call<WatchTogether?>?

    @POST("/watchTogetherEnd") // 같이보기 방 삭제
    fun executeWatchTogetherEnd(@Body map: HashMap<String, String>): Call<Void?>?

    // 로그아웃
    @POST("/logout")
    fun executeLogout(@Body map: HashMap<String, String>): Call<Void?>?
}