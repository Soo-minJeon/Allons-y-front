package com.example.harumub_front

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    var isExistBlank = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        var j_id = findViewById<EditText>(R.id.id)
        var j_pw = findViewById<EditText>(R.id.pw)
        var j_name = findViewById<EditText>(R.id.name)
        var j_num = findViewById<EditText>(R.id.number)
        var j_email = findViewById<EditText>(R.id.email)
        var j_addr = findViewById<EditText>(R.id.address)

        var btnJoin = findViewById<Button>(R.id.btn_join)

        // 하단 JOIN 버튼 클릭 - 액티비티 종료 및 로그인 페이지 호출
        btnJoin.setOnClickListener {
            // 회원가입 정보 -> DB로 전달해야

            // 사용자가 입력한 값들을 String으로 받아오기
            val id = j_id.text.toString()
            val pw = j_pw.text.toString()
            val name = j_name.text.toString()
            val number = j_num.text.toString()
            val e_mail = j_email.text.toString()
            val addr = j_addr.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(id.isEmpty() || pw.isEmpty() || name.isEmpty()
                || number.isEmpty() || e_mail.isEmpty() || addr.isEmpty() ){
                isExistBlank = true
                // 작성 경고 토스트 메세지
                Toast.makeText(this, "입력란을 모두 작성바랍니다.", Toast.LENGTH_SHORT).show()
            }
            // 유저가 모든 항목을 다 채웠을 경우
            //if(!isExistBlank){
            else {
                // 회원가입 성공 토스트 메세지
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                // 유저가 입력한 정보를 MongoDB에 저장해야 함
                // 관련 코드는 다시...


                // 로그인 화면으로 다시 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}