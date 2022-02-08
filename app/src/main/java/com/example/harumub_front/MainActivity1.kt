package com.example.harumub_front

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        var btnMenu = findViewById<ImageButton>(R.id.drawer)
        var btnMine = findViewById<ImageButton>(R.id.myrecord)
        var btnRecord = findViewById<Button>(R.id.btnRecord)

        // 감상하기 버튼 클릭 - 혼자 감상하기 페이지로 이동
        btnRecord.setOnClickListener {
            // var intent = Intent(applicationContext, ::class.java)
            // startActivity(intent)
        }

        // 상단바 메뉴 버튼 클릭 - 드로어 열기
        btnMenu.setOnClickListener {
            // var intent = Intent(applicationContext, ::class.java)
            // startActivity(intent)
        }

        // 상단바 시계 버튼 클릭 - 나의 감상 기록 페이지로 이동
        btnMine.setOnClickListener {
            // var intent = Intent(applicationContext, ::class.java)
            // startActivity(intent)
        }
    }
}