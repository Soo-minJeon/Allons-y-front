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

    @POST("/makeRoom")
    fun executeMakeRoom(@Body map: HashMap<String, String>): Call<MakeRoomResult?>?
}