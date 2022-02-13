package com.example.harumub_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecommendAdapter1.ViewHolder>? = null

    private var layoutManager2: RecyclerView.LayoutManager? = null
    private var adapter2: RecyclerView.Adapter<RecommendAdapter2.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var recent = findViewById<ImageButton>(R.id.recent)
        var text = findViewById<TextView>(R.id.textView3)

        layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        adapter = RecommendAdapter1()
        recyclerView.adapter = adapter

        layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.layoutManager = layoutManager2
        adapter2 = RecommendAdapter2()
        recyclerView2.adapter = adapter2

        recent.setOnClickListener { // recent 이미지 버튼 클릭 시 나의 감상기록 페이지로 이동
            val intent = Intent(this, MyMovieListActivity::class.java) // 메인2
            startActivity(intent)
        }

        // 원래는 이미지에서 넘어가야 함
        text.setOnClickListener { // textView3 클릭 시 다른 사용자 감상기록 페이지로 이동
            supportFragmentManager.beginTransaction()
                .replace(R.id.main2, UserMovieListFragment())
                .commit()
            Log.d("text : ", "선택")
        }
    }
}