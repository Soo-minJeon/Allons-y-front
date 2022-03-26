package com.example.harumub_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class WatchAloneActivity : AppCompatActivity() {

    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_alone)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        var btn_start = findViewById<Button>(R.id.watch_start)
        var btn_end = findViewById<Button>(R.id.watch_end)

        // 현재 로그인하고 있는 사용자 아이디 / 영화 아이디 (수정 필요) --수민 작성
        var userid = ""
        var movieid = ""

        // 이메일 전송 버튼 누르면 -> 노드에 map 전송
        btn_start.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {
                var map = HashMap<String, String>()
                map.put("id", userid)
                map.put("movieId", movieid)

                var call = retrofitInterface.executeWatchAloneStart(map)

                call!!.enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if(response.code() == 200){
                            Toast.makeText(this@WatchAloneActivity, "감상시작 신호 보내기 성공", Toast.LENGTH_SHORT).show()
                        }
                        else if (response.code() == 400){
                            Toast.makeText(this@WatchAloneActivity, "감상시작 신호 보내기 실패", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Toast.makeText(this@WatchAloneActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })

        // 이메일 전송 버튼 누르면 -> 노드에 map 전송
        btn_end.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {
                var map = HashMap<String, String>()
                map.put("signal", "end")

                var call = retrofitInterface.executeWatchAloneEnd(map)

                call!!.enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if(response.code() == 200){
                            Toast.makeText(this@WatchAloneActivity, "감상종료 신호 보내기 성공", Toast.LENGTH_SHORT).show()
                        }
                        else if (response.code() == 400){
                            Toast.makeText(this@WatchAloneActivity, "감상종료 신호 보내기 실패", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Toast.makeText(this@WatchAloneActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })


    }
}