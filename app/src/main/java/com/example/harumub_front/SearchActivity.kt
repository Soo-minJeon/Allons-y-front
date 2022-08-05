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
    private lateinit var retrofitInterface: RetrofitInterface

    var recyclerView: RecyclerView? = null
    var editText: EditText? = null
    var adapter: SearchAdapter? = null
//    var items = ArrayList<String>()
//    var poster = arrayOf(R.drawable.about, R.drawable.gucci, R.drawable.spider)
//    var title = arrayOf("About Times", "Gucci", "Spider Man3")
    var items_title = ArrayList<String>()
    var items_poster = ArrayList<String>()
    var items_runningTime = ArrayList<Int>()

    // 현재 로그인하고 있는 사용자 아이디
    lateinit var id : String
//    lateinit var movie_title : String

    // 추천 정보
    lateinit var reco1_titleArray : java.util.ArrayList<String>
    lateinit var reco1_posterArray : java.util.ArrayList<String>

    lateinit var reco2_1_userId : String
    lateinit var reco2_2_userId : String
    lateinit var reco2_3_userId : String
    lateinit var reco2_4_userId : String
    lateinit var reco2_5_userId : String

    lateinit var reco2_1_title : java.util.ArrayList<String>
    lateinit var reco2_2_title : java.util.ArrayList<String>
    lateinit var reco2_3_title : java.util.ArrayList<String>
    lateinit var reco2_4_title : java.util.ArrayList<String>
    lateinit var reco2_5_title : java.util.ArrayList<String>

    lateinit var reco2_1_poster : java.util.ArrayList<String>
    lateinit var reco2_2_poster : java.util.ArrayList<String>
    lateinit var reco2_3_poster : java.util.ArrayList<String>
    lateinit var reco2_4_poster : java.util.ArrayList<String>
    lateinit var reco2_5_poster : java.util.ArrayList<String>

    lateinit var reco3_titleArray : java.util.ArrayList<String>
    lateinit var reco3_posterArray : java.util.ArrayList<String>

    lateinit var reco4_year : String
    lateinit var reco4_titleArray : ArrayList<String>
    lateinit var reco4_posterArray : ArrayList<String>

    lateinit var reco6_titleArray : ArrayList<String>
    lateinit var reco6_posterArray : ArrayList<String>

    var movieList = ArrayList<MovieModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        id = intent.getStringExtra("user_id").toString()

        reco1_titleArray = intent.getSerializableExtra("reco1_titleArray") as java.util.ArrayList<String>
        reco1_posterArray = intent.getSerializableExtra("reco1_posterArray") as java.util.ArrayList<String>

        reco2_1_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_2_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_3_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_4_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_5_userId = intent.getStringExtra("reco2_1_userId").toString()

        reco2_1_title = intent.getSerializableExtra("reco2_1_title") as java.util.ArrayList<String>
        reco2_2_title = intent.getSerializableExtra("reco2_2_title") as java.util.ArrayList<String>
        reco2_3_title = intent.getSerializableExtra("reco2_3_title") as java.util.ArrayList<String>
        reco2_4_title = intent.getSerializableExtra("reco2_4_title") as java.util.ArrayList<String>
        reco2_5_title = intent.getSerializableExtra("reco2_5_title") as java.util.ArrayList<String>

        reco2_1_poster = intent.getSerializableExtra("reco2_1_poster") as java.util.ArrayList<String>
        reco2_2_poster = intent.getSerializableExtra("reco2_2_poster") as java.util.ArrayList<String>
        reco2_3_poster = intent.getSerializableExtra("reco2_3_poster") as java.util.ArrayList<String>
        reco2_4_poster = intent.getSerializableExtra("reco2_4_poster") as java.util.ArrayList<String>
        reco2_5_poster = intent.getSerializableExtra("reco2_5_poster") as java.util.ArrayList<String>

        reco3_titleArray = intent.getSerializableExtra("reco3_titleArray") as java.util.ArrayList<String>
        reco3_posterArray = intent.getSerializableExtra("reco3_posterArray") as java.util.ArrayList<String>

        reco4_year = intent.getStringExtra("reco4_year").toString()
        reco4_titleArray = intent.getSerializableExtra("reco4_titleArray") as ArrayList<String>
        reco4_posterArray = intent.getSerializableExtra("reco4_posterArray") as ArrayList<String>

        reco6_titleArray = intent.getSerializableExtra("reco6_titleArray") as ArrayList<String>
        reco6_posterArray = intent.getSerializableExtra("reco6_posterArray") as ArrayList<String>

        // 메인 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id")) {
            Log.d("SearchActivity", "메인에서 받아온 id : $id")
        } else {
            Log.e("SearchActivity", "가져온 데이터 없음")
        }

        recyclerView = findViewById<View>(R.id.recylcerview) as RecyclerView
        editText = findViewById<View>(R.id.search_edt) as EditText
        editText!!.addTextChangedListener(this)

        var map = HashMap<String, String>()
        val call = retrofitInterface.executeGetAllMovieList(map)
        call!!.enqueue(object : Callback<SearchData?> {
            override fun onResponse(call: Call<SearchData?>, response: Response<SearchData?>) {
                if (response.code() == 200) {
                    val result = response.body()

                    var title = result?.title
                    var poster_url = result?.poster
                    var running_time = result?.runningTime

                    // items_title 배열에 영화 제목 넣기 // items_poster 배열에 영화 포스터 링크 넣기
                    for (i: Int in 0..title!!.size - 1) {
                        items_title.add(title[i])
                        items_poster.add(poster_url!![i])
                        items_runningTime.add(running_time!![i])
                    }

                    for (i in 0..(items_title.size - 1)) {
                        movieList.add(MovieModel(items_title[i], items_poster[i], items_runningTime[i]))
                        Log.d("movieList : ", movieList[i].movieTitle + " " + movieList[i].moviePoster + " " + movieList[i].movieRunningTime)
                    }

//                    adapter = SearchAdapter(applicationContext, id, movieList, items_title, items_poster, items_runningTime)
                    adapter = SearchAdapter(applicationContext, id, movieList, items_title, items_poster, items_runningTime,
                        reco1_titleArray, reco1_posterArray, reco2_1_userId, reco2_2_userId, reco2_3_userId, reco2_4_userId, reco2_5_userId,
                        reco2_1_title, reco2_2_title, reco2_3_title, reco2_4_title, reco2_5_title,
                        reco2_1_poster, reco2_2_poster, reco2_3_poster, reco2_4_poster, reco2_5_poster,
                        reco3_titleArray, reco3_posterArray, reco4_year, reco4_titleArray, reco4_posterArray,
                        reco6_titleArray, reco6_posterArray)
                    recyclerView!!.layoutManager =
                        LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                    recyclerView!!.adapter = adapter
/*
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
                    //Toast.makeText(this@SearchActivity, "영화 정보 출력 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SearchData?>, t: Throwable) {
                //Toast.makeText(this@SearchActivity, t.message, Toast.LENGTH_SHORT).show()
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