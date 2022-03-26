package com.example.harumub_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class MainActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface

    lateinit var main2_this : androidx.drawerlayout.widget.DrawerLayout
    lateinit var drawer_button : ImageButton
    lateinit var recent_button: ImageButton
    lateinit var drawer_view : NavigationView

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecommendAdapter1.ViewHolder>? = null

    private var layoutManager2: RecyclerView.LayoutManager? = null
    private var adapter2: RecyclerView.Adapter<RecommendAdapter2.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        main2_this = findViewById(R.id.main2_drawer)
        drawer_button = findViewById(R.id.drawer_button) // 드로어 열기(메뉴버튼)
        drawer_view = findViewById(R.id.drawer_view) // 드로어
        val drawerHeader = drawer_view.getHeaderView(0) // 드로어 헤더
        recent_button = findViewById(R.id.recent) // 최근 감상기록 버튼

        // 현재 로그인하고 있는 사용자 아이디 (수정 필요) --수민 작성
        var userid = ""
        var result : List<Recommend2Result>
        var userIds : Array<String> = emptyArray()
        var titles : Array<String> = emptyArray()
        var posters : Array<String> = emptyArray()

        val map = HashMap<String, String>()
        map.put("id", userid)

        val call = retrofitInterface.executeRecommend2(map)
        call!!.enqueue(object : Callback<List<Recommend2Result?>> {
            override fun onResponse(call: Call<List<Recommend2Result?>>, response: Response<List<Recommend2Result?>>) {
                if (response.code() == 200) {
                    val result = response.body()

                    for(i in 0..result?.size!!-1){
                        // (userid) title 과 Poster url 은 배열에 저장. -> 리사이클러뷰에 넣어야 함 -- 수민 작성
                        userIds[i] = result.get(i)!!.userId
                        titles[i] = result.get(i)!!.title
                        posters[i] = result.get(i)!!.poster

                        Toast.makeText(this@MainActivity2, "성공", Toast.LENGTH_SHORT).show()
                    }

                }
                else if (response.code() == 404) {
                    Toast.makeText(this@MainActivity2, "404 오류", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Recommend2Result?>>, t: Throwable) {
                Toast.makeText(this@MainActivity2, t.message,
                    Toast.LENGTH_LONG).show()
            }
        })

        // 드로어 버튼 클릭 -> 드로어 메뉴 열기
        drawer_button.setOnClickListener{
            main2_this.openDrawer(GravityCompat.START) // START = left, END : right (드로어가 나오는 방향지정)
        }
        // 네비게이션 메뉴 아이템에 클릭 속성 부여
        drawer_view.setNavigationItemSelectedListener(this)

        // 최근 감상 기록 (시계) 버튼 클릭 -> 페이지 이동
        recent_button.setOnClickListener{
            val intent = Intent(this, WatchListActivity::class.java)
            startActivity(intent)
        }

        var text = findViewById<TextView>(R.id.textView3) // '다른 사용자가 좋아하는' 텍스트

        // '내가 좋아하는' 영화 목록 RecyclerView와 RecommendAdapter1 연결
        layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        adapter = RecommendAdapter1()
        recyclerView.adapter = adapter

        // '다른 사용자가 좋아하는' 영화 목록 RecyclerView와 RecommendAdapter2 연결
        layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.layoutManager = layoutManager2
        adapter2 = RecommendAdapter2()
        recyclerView2.adapter = adapter2
/*
        var recent = findViewById<ImageButton>(R.id.recent) // 나의 감상기록 이미지 버튼
        recent.setOnClickListener { // recent(나의 감상기록) 이미지 버튼 클릭 시 나의 감상기록 페이지로 이동
            val intent = Intent(this, WatchListActivity::class.java) // 나의 감상기록 페이지
            startActivity(intent)
        }
*/
        // 원래는 이미지에서 넘어가야 함
        text.setOnClickListener { // '다른 사용자가 좋아하는' 텍스트 클릭 시 다른 사용자 감상기록 페이지로 이동. 수정 필요
            supportFragmentManager.beginTransaction()
                .replace(R.id.main2, UserMovieListFragment())
                .commit()
            Log.d("text : ", "선택")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {// 네비게이션 메뉴 아이템 클릭 시 수행
        when(item.itemId){ // 드로어 메뉴 눌렸을 시 수행. 수정 필요
            R.id.drawer_UserRecord -> {
                // fragment manager 가져와서 fragment transaction 생성
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "사용자 기록보기", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, WatchListActivity::class.java)
                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_WatchAlone -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "혼자 보기", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, SearchActivity::class.java)
                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_WatchTogether -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "같이 보기", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, EnterActivity::class.java)
                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_Help -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "도움말", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, HelpActivity::class.java)
                    startActivityForResult(intent, 0)
                    commit()
                } // 프래그먼트 트랜잭션 변경 후 commit() 호출해야 변경 내용 적용
            }
            R.id.drawer_Logout -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "로그아웃", Toast.LENGTH_SHORT).show()

                    // 로그아웃 기능 구현


                    var intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivityForResult(intent, 0)
                    commit()
                }
            }
        }
        main2_this.closeDrawers() // 네비게이션 뷰 닫기
        return false
    }
}