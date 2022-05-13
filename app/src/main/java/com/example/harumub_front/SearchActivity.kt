package com.example.harumub_front

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() , TextWatcher {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface: RetrofitInteface

    var recyclerView: RecyclerView? = null
    var editText: EditText? = null
    var adapter: SearchAdapter? = null
//    var items = ArrayList<String>()
//    var poster = arrayOf(R.drawable.about, R.drawable.gucci, R.drawable.spider)
//    var title = arrayOf("About Times", "Gucci", "Spider Man3")
    var items_title = ArrayList<String>()
    var items_poster = ArrayList<String>()

    // 현재 로그인하고 있는 사용자 아이디
//    private val id = intent.getStringExtra("user_id")
    lateinit var id : String
//    lateinit var movie_title : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        id = intent.getStringExtra("user_id").toString()

        // 메인 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id")) {
            Log.d("SearchActivity", "메인에서 받아온 id : $id")
        } else {
            Log.e("SearchActivity", "가져온 데이터 없음")
        }

        recyclerView = findViewById<View>(R.id.recylcerview) as RecyclerView
        editText = findViewById<View>(R.id.search_edt) as EditText
        editText!!.addTextChangedListener(this)

/*
        // items 배열에 영화 제목 넣기
        for(i: Int in 0..poster.size-1) {
            items.add(title[i]) // item 배열에 영화 제목 추가
        }

        //
        adapter = SearchAdapter(applicationContext, items)
        recyclerView!!.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.adapter = adapter
*/

        var map = HashMap<String, String>()
        val call = retrofitInterface.executeGetAllMovieList(map)
        call!!.enqueue(object : Callback<SearchData?> {
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
                if (response.code() == 200) {
                    val result = response.body()

                    var title = result?.title
                    var poster_url = result?.poster

                    // items_title 배열에 영화 제목 넣기 // items_poster 배열에 영화 포스터 링크 넣기
                    for (i: Int in 0..title!!.size - 1) {
                        items_title.add(title[i])
                        items_poster.add(poster_url!![i])
                    }

                    adapter = SearchAdapter(applicationContext, id, items_title, items_poster)
                    recyclerView!!.layoutManager =
                        LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                    recyclerView!!.adapter = adapter

//                    movie_title = intent.getStringExtra("movie_title").toString()

                    Toast.makeText(this@SearchActivity, "영화 정보 출력 성공", Toast.LENGTH_SHORT).show()

/*  // SearchAdapter 수정 - 영화 클릭 시 혼자 보기 페이지로 이동
                    // 혼자보기 입장 버튼 클릭 시 혼자보기 페이지로 이동
                    watch_alone_enter_button.setOnClickListener {
                        var intent = Intent(applicationContext, WatchAloneActivity::class.java)
                        intent.putExtra("user_id", id)
                        intent.putExtra("movie_title", movie_title) // movie_title은 SearchAdapter에서 전달
                        startActivity(intent)
                    }
*/
                }

                else {
                    Toast.makeText(this@SearchActivity, "영화 정보 출력 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SearchData?>, t: Throwable) {
                Toast.makeText(this@SearchActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })

/*
        // 혼자보기 입장 버튼 클릭 시 혼자보기 페이지로 이동
        watch_alone_enter_button.setOnClickListener {
            var intent = Intent(applicationContext, WatchAloneActivity::class.java)
            intent.putExtra("user_id", id)
            // intent.putExtra("movie_title", movie_title) // movie_title은 SearchAdapter에서 전달
            startActivity(intent)
        }
*/
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        //
        adapter?.getFilter()?.filter(charSequence)
    }

    override fun afterTextChanged(editable: Editable) {}
}