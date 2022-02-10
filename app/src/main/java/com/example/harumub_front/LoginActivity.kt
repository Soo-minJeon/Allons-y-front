package com.example.harumub_front

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var l_id = findViewById<EditText>(R.id.myid)
        var l_pw = findViewById<EditText>(R.id.mypw)
        var btnLogin = findViewById<Button>(R.id.btn_login)
        var btnSignup = findViewById<Button>(R.id.btn_signup)

        // 단방향 페이지 이동
        // 로그인 버튼 클릭 - 액티비티 종료 및 메인페이지 호출
        btnLogin.setOnClickListener {
            // 사용자가 입력한 값들을 String으로 받아오기
            val id = l_id.text.toString()
            val pw = l_pw.text.toString()

            /**  id, pw 정보와  MongoDB에 저장된 정보 비교 및 확인
            // 이와 관련한 코드도 다시...
            val savedId = .getString("id", "")
            val savedPw = .getString("pw", "")

            if (id == savedID && pw == savedPw) { // DB에 저장된 회원정보와 일치시
                Toast.makeText(this, "반갑습니다", Toast.LENGTH_SHORT).show()

                // DB에 감상 기록이 있는 경우
                var intent1 = Intent(applicationContext, MainActivity2::class.java)
            **/
                // DB에 감상 기록이 없는 경우
                var intent1 = Intent(applicationContext, MainActivity1::class.java)

                startActivity(intent1)
                finish() // }
        }

        // 양방향 액티비티 (회원가입 <-> 로그인)
        // 회원가입 버튼 클릭 - 액티비티 종료 및 회원가입 페이지 호출
        btnSignup.setOnClickListener {
            var intent2 = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent2)
        }
    }


}