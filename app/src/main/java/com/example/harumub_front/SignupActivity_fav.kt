package com.example.harumub_front

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_signup_fav.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class SignupActivity_fav : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInterface

    lateinit var id : String
    lateinit var pw : String
    lateinit var name : String
    lateinit var email : String
    lateinit var codeAuth : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_fav)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        id = intent.getStringExtra("user_id").toString()
        pw = intent.getStringExtra("user_password").toString()
        name = intent.getStringExtra("user_name").toString()
        email = intent.getStringExtra("user_email").toString().trim()
        codeAuth = intent.getStringExtra("user_codeAuth").toString()


        val btnJoin = findViewById<Button>(R.id.btn_join)

        // 좋아하는 영화 질문 EditText
        val j_like_movie1 = findViewById<EditText>(R.id.like_movie1)
        val j_like_movie2 = findViewById<EditText>(R.id.like_movie2)
        val j_like_movie3 = findViewById<EditText>(R.id.like_movie3)

        // 상단 진행 바
        val progress = findViewById<CardView>(R.id.signup_progress)

        // 선호 장르 스피너
        val spinnerGenre = findViewById<Spinner>(R.id.spinner_genre)
        spinnerGenre.adapter = ArrayAdapter.createFromResource(this, R.array.genre_array, android.R.layout.simple_spinner_item)
        var genre : String? = null

        // 아이디 검사 - 글자수 제한, 특수 문자 금지
        val idLayout = findViewById<TextInputLayout>(R.id.id_layout)

        // 아이디 정규식 검사 - 영문, 숫자, 5~10자
        fun idRegex(id: String) : Boolean {
            val idValidation = "^[a-zA-Z0-9]{5,10}\$"
            return id.matches(idValidation.toRegex()) // 패턴이 맞으면 true 반환
        }


        // 선호 장르 아이템 선택
        spinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                genre = null
                Log.d("선호 장르 : ", "선택된 게 없음")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    // '선호 장르 선택' 문구 선택
                    0 -> {
                        genre = null
                        Log.d("선호 장르 : ", "선택")
                    }

                    // Action 선택
                    1 -> {
                        genre = "Action"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // Adventure 선택
                    2 -> {
                        genre = "Adventure"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // Animation 선택
                    3 -> {
                        genre = "Animation"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // Comedy 선택
                    4 -> {
                        genre = "Comedy"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // Drama 선택
                    5 -> {
                        genre = "Drama"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // Family 선택
                    6 -> {
                        genre = "Family"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // Fantasy 선택
                    7 -> {
                        genre = "Fantasy"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // Horror 선택
                    8 -> {
                        genre = "Horror"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // Romance 선택
                    9 -> {
                        genre = "Romance"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // Science Fiction 선택
                    10 -> {
                        genre = "Science Fiction"
                        Log.d("선호 장르 : ", genre!!)
                    }

                    // 일치하는 게 없는 경우
                    else -> {
                        genre = null
                        Log.d("선호 장르 : ", "없음")
                    }
                }
            }
        }



        // 하단 JOIN 버튼 클릭 - 회원 가입 정보 연동 및 액티비티 종료 ->로그인 페이지 호출
        btnJoin.setOnClickListener {
            // 사용자가 입력한 값들을 String으로 받아오기
            val like_movie1 = j_like_movie1.text.toString()
            val like_movie2 = j_like_movie2.text.toString()
            val like_movie3 = j_like_movie3.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(id.isEmpty() || pw.isEmpty() || name.isEmpty() || email.isEmpty() || like_movie1.isEmpty() || like_movie2.isEmpty() || like_movie3.isEmpty() || genre.isNullOrBlank()) {
                Toast.makeText(this, "입력란을 모두 작성바랍니다.", Toast.LENGTH_SHORT).show()

/*
                if(genre.isNullOrBlank()) {
                    Toast.makeText(this@SignupActivity, "선호하는 장르를 선택해 주세요.", Toast.LENGTH_SHORT).show()
                }
*/
            }

            if (codeAuth == "true") {
                // 회원정보 retrofit 연동
                val map = HashMap<String, String>()
                map.put("id", id)
                map.put("password", pw)
                map.put("name", name)
                map.put("favorite", like_movie1 + "," + like_movie2 + "," + like_movie3)
                map.put("genre", genre!!)

                val call = retrofitInterface.executeSignup(map)

                call!!.enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.code() == 200) {
                            Toast.makeText(
                                this@SignupActivity_fav,
                                "회원가입이 완료되었습니다.", Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                        } else if (response.code() == 400) {
                            Toast.makeText(
                                this@SignupActivity_fav, "이미 가입된 정보입니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        //Toast.makeText(this@SignupActivity, "회원가입에 실패했습니다.", Toast.LENGTH_LONG).show()

                        //Toast.makeText(this@SignupActivity, t.message, Toast.LENGTH_LONG).show()
                        Log.d("회원가입 실패 : ", t.message.toString())
                    }
                })
            }
        }
    }
}