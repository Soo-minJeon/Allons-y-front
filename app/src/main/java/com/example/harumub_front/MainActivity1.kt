package com.example.harumub_front

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main1.*

class MainActivity1 : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener {

    lateinit var main_this : androidx.drawerlayout.widget.DrawerLayout
    lateinit var drawer_button : ImageButton
    lateinit var drawer_view : NavigationView
    lateinit var btnRecord : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
        main_this = findViewById(R.id.main_drawer)

        drawer_button = findViewById(R.id.drawer_button) // 드로어 열기(메뉴버튼)
        drawer_view = findViewById(R.id.drawer_view) // 드로어
        val drawerHeader = drawer_view.getHeaderView(0) // 드로어 헤더

        drawer_button.setOnClickListener{
            main_this.openDrawer(GravityCompat.START) // START = left, END : right (드로어가 나오는 방향지정)
        }
        drawer_view.setNavigationItemSelectedListener(this) // 네비게이션 메뉴 아이템에 클릭 속성 부여

        btnRecord = findViewById(R.id.btnRecord) // 감상하기 버튼
        val intent = Intent(this, MainActivity2::class.java) // 메인2
        // val intent = Intent(this, SearchActivity::class.java) // 영화 검색 페이지

        btnRecord.setOnClickListener { // 감상하기 버튼 클릭 시 메인2로 이동
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { // 네비게이션 메뉴 아이템 클릭 시 수행
        when(item.itemId){ // 드로어 메뉴 눌렸을 시 수행. 수정 필요
            R.id.drawer_UserRecord -> Toast.makeText(applicationContext, "사용자 기록보기", Toast.LENGTH_SHORT).show()
            R.id.drawer_WatchAlone -> Toast.makeText(applicationContext, "혼자 보기", Toast.LENGTH_SHORT).show()
            R.id.drawer_WatchTogether -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "같이 보기", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, enterActivity::class.java)
                    startActivityForResult(intent, 0)
                    commit()
                }
            }
            R.id.drawer_Help -> {
                with(supportFragmentManager.beginTransaction()) {
                    Toast.makeText(applicationContext, "도움말", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, HelpActivity::class.java)
                    startActivityForResult(intent, 0)
                    commit()
                }
            }
            R.id.drawer_Logout -> Toast.makeText(applicationContext, "로그아웃", Toast.LENGTH_SHORT).show()
        }
        main_this.closeDrawers() // 네비게이션 뷰 닫기
        return false
    }
}
