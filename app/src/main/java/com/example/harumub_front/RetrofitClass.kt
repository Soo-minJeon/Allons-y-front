package com.example.harumub_front


data class LoginResult(var id: String, var password : String, var name: String)//, var number : String, var birth : String)

data class MakeRoomResult(var roomCode: String)

data class EmailResult(var code: String)

data class WatchListResult(var title: String, var poster: String)

data class WatchResult(var title:String, var poster : String, var genres: String, var concentration: String,
                       var emotion : String, var highlight: String, var rating : Int, var comment : String)

data class Recommend2Result(var userId: String, var title: String, var poster: String) {
}