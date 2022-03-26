package com.example.harumub_front

import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

interface RetrofitInteface {
    @POST("/login")
    fun executeLogin(@Body map: HashMap<String, String>): Call<LoginResult?>?

    @POST("/signup")
    fun executeSignup(@Body map: HashMap<String, String>): Call<Void?>?

    @POST("/email")
    fun executeEmail(@Body map: HashMap<String, String>): Call<EmailResult?>?

    @POST("/makeRoom")
    fun executeMakeRoom(@Body map: HashMap<String, String>): Call<MakeRoomResult?>?

    @POST("/watchList")
    fun executeWatchList(@Body map: HashMap<String, String>): Call<List<WatchListResult?>>?

    @POST("/watchresult")
    fun executeWatchResult(@Body map: HashMap<String, String>): Call<WatchResult?>?

    @POST("/enterroom")
    fun executeEnterRoom(@Body map: HashMap<String, String>): Call<Void?>?

    @POST("/recommend2")
    fun executeRecommend2(@Body map: HashMap<String, String>): Call<List<Recommend2Result?>>?

    @POST("/watchAloneStart")
    fun executeWatchAloneStart(@Body map: HashMap<String, String>): Call<Void?>?

    @POST("/watchAloneEnd")
    fun executeWatchAloneEnd(@Body map: HashMap<String, String>): Call<Void?>?

}