package com.example.harumub_front

import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.HashMap

interface RetrofitInterface {
    // 로그인
    @POST("/login")
    fun executeLogin(@Body map: HashMap<String, String>): Call<LoginResult?>?

    // 이메일 인증
    @POST("/email")
    fun executeEmail(@Body map: HashMap<String, String>): Call<EmailResult?>?

    // 회원가입
    @POST("/signup")
    fun executeSignup(@Body map: HashMap<String, String>): Call<Void?>?

    // 혼자보기 - 감상 시작
    @POST("/watchAloneStart")
    fun executeWatchAloneStart(@Body map: HashMap<String, String>): Call<Void?>?

    // 혼자보기 - 아이트래킹
    @POST("/watchImageCaptureEyetrack")
    fun executeWatchImageCaptureEyetrack(@Body map: HashMap<String, String>): Call<Void?>?

    // 혼자보기 - 감상 종료
    @POST("/watchAloneEnd")
    fun executeWatchAloneEnd(@Body map: HashMap<String, String>): Call<WatchAloneMovie?>?

    // 추가: 리뷰 작성 후 버튼 클릭시 데이터 전달
    @POST("addReview")
    fun executeAddReview(@Body map: HashMap<String, String>): Call<Void?>?

    // 감상 결과
    @POST("/watchresult")
    fun executeWatchResult(@Body map: HashMap<String, String>): Call<WatchResult?>?

    // 영화 검색 페이지 - 전체 영화 목록
    @POST("/getAllMovieList")
    fun executeGetAllMovieList(@Body map: HashMap<String, String>): Call<SearchData?>?

    // 사용자 감상 목록
    @POST("/watchlist")
    fun executeWatchList(@Body map: HashMap<String, String>): Call<WatchListResult?>?

    // 같이보기
    @POST("/makeRoom")
    fun executeMakeRoom(@Body map: HashMap<String, String>): Call<MakeRoomResult?>?

    @POST("/enterroom")
    fun executeEnterRoom(@Body map: HashMap<String, String>): Call<EnterRoomResult?>?

    // 캡처 및 감정 출력
    @POST("/watchTogetherImageCapture")
    fun executeWatchTogetherImageCapture(@Body map: HashMap<String, String>): Call<WatchTogether?>?

    // 같이보기 방 삭제
    @POST("/watchTogetherEnd")
    fun executeWatchTogetherEnd(@Body map: HashMap<String, String>): Call<Void?>?

    // 로그아웃
    @POST("/logout")
    fun executeLogout(@Body map: HashMap<String, String>): Call<Void?>?
}