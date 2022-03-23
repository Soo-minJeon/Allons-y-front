package com.example.harumub_front

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class SignupActivity : AppCompatActivity() {
    var isExistBlank = false
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

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
                val map = HashMap<String, String>()
                map.put("id", id)
                map.put("password", pw)
                map.put("name", name)
                //map.put("number", number)
                //map.put("birth", addr)

                val call = retrofitInterface.executeSignup(map)

                call!!.enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.code() == 200) {
                            Toast.makeText(this@SignupActivity,
                                "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                        } else if (response.code() == 400) {
                            Toast.makeText(this@SignupActivity, "이미 가입된 정보입니다.",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Toast.makeText(this@SignupActivity, t.message,
                            Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}