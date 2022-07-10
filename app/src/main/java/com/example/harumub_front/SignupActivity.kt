package com.example.harumub_front

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        val j_id = findViewById<EditText>(R.id.id)
        val j_pw = findViewById<EditText>(R.id.pw)
        val j_name = findViewById<EditText>(R.id.name)
        val j_email = findViewById<EditText>(R.id.email)
        val j_code = findViewById<EditText>(R.id.code)

        val btnReq = findViewById<Button>(R.id.btn_request)
        val btnJoin = findViewById<Button>(R.id.btn_join)
        val btnAuth = findViewById<Button>(R.id.btn_auth)

        // 좋아하는 영화 질문 EditText
        val j_like_movie1 = findViewById<EditText>(R.id.like_movie1)
        val j_like_movie2 = findViewById<EditText>(R.id.like_movie2)
        val j_like_movie3 = findViewById<EditText>(R.id.like_movie3)

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

        j_id.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { // text가 변경된 후 호출. s = 변경 후의 문자열
                if(s != null){
                    if(s.isEmpty() || !idRegex(s.toString()) ) { // 정규식 = false
                        idLayout.error = "올바르게 입력해주세요!"
                    } else {
                        idLayout.error = null
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {} // text가 변경되기 전 호출. s = 변경 전 문자열
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} // text가 바뀔 때마다 호출
        })

        // 비밀번호 검사
        val pwLayout = findViewById<TextInputLayout>(R.id.pw_layout)

        // 비밀번호 정규식 검사 - 문자 및 숫자 필수, 8~20자
        // 여기서 에러 나면 토글 버튼이 안 보여서 일단 정규식 사용 안 했음
//        fun pwRegex(password: String) : Boolean {
//            val pwValidation = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}\$" // >> 특수문자 넣으면 에러 발생
//            return password.matches(pwValidation.toRegex()) // 패턴이 맞으면 true 반환
//        }

        j_pw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { // text가 변경된 후 호출. s = 변경 후의 문자열
                if(s != null){
                    // if(s.isEmpty() || !pwRegex(s.toString())) { // 정규식 = false
                    if(s.isEmpty() || s.toString().length < 8 || s.toString().length > 20) {
                        pwLayout.error = "올바르게 입력해주세요!"
                    } else {
                        pwLayout.error = null
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {} // text가 변경되기 전 호출. s = 변경 전 문자열
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} // text가 바뀔 때마다 호출
        })

        // 이름 검사 - 글자수 제한
        val nameLayout = findViewById<TextInputLayout>(R.id.name_layout)

        j_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { // text가 변경된 후 호출. s = 변경 후의 문자열
                if(s != null) {
                    if (s.isEmpty() || s.toString().length > 10) {
                        nameLayout.error = "올바르게 입력해주세요!"
                    } else {
                        nameLayout.error = null
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {} // text가 변경되기 전 호출. s = 변경 전 문자열
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} // text가 바뀔 때마다 호출
        })

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

        // 이메일 인증 및 검사
        var result : EmailResult?
        var getCode : String? = null
        val emailLayout = findViewById<TextInputLayout>(R.id.email_layout)

        // 이메일 정규식 검사 - 이메일 형식
        fun emailRegex(email: String) : Boolean {
            val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            return email.matches(emailValidation.toRegex()) // 패턴이 맞으면 true 반환

//            val pattern = Pattern.matches(emailValidation, e_mail) // 서로 패턴이 맞는지 확인
//            if (pattern) {
//                //이메일 형태가 정상일 경우
//                j_email.setTextColor(R.color.black.toInt())
//                btnReq.setEnabled(true)
//                return true
//            } else { // 이메일 형태가 정상이 아닐 경우 - 빨간 텍스트로 표시
//                j_email.setTextColor(-65536) // R.color.red.toInt()
//                return false
//            }
        }

        j_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { // text가 변경된 후 호출. s = 변경 후의 문자열
                if(s != null) {
                    if (s.isEmpty() || !emailRegex(s.toString()) ) { // 정규식 = false
                        emailLayout.error = "올바르게 입력해 주세요!"
//                        btnReq.setEnabled(true)
                    } else {
                        emailLayout.error = null
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {} // text가 변경되기 전 호출. s = 변경 전 문자열
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} // text가 바뀔 때마다 호출
        })

        var emailCode : Boolean? = false

        // 이메일 (인증코드) 요청 버튼 누르면 -> 노드에 map 전송
        btnReq.setOnClickListener(object : View.OnClickListener{

            var result : EmailResult? = null
            var getcode : String? = null

            override fun onClick(v: View?) {
                var map = HashMap<String, String>()
                map.put("email", j_email.text.toString().trim()) // 'paramId' of 백 코드

                var call = retrofitInterface.executeEmail(map)

                call!!.enqueue(object : Callback<EmailResult?>{
                    override fun onResponse( call: Call<EmailResult?>, response: Response<EmailResult?>) {
                        if(response.code() == 200){
                            result = response.body()
                            getCode = result?.code
                            //Toast.makeText(this@SignupActivity, "getCode : " + getCode, Toast.LENGTH_SHORT).show()
                            //btnAuth.setEnabled(true)
                            emailCode = true

                            Toast.makeText(this@SignupActivity, "email send successfully", Toast.LENGTH_SHORT).show()
                        }
                        else if (response.code() == 400){
                            //Toast.makeText(this@SignupActivity, "email send error", Toast.LENGTH_SHORT).show()
                            emailCode = false
                        }
                    }

                    override fun onFailure(call: Call<EmailResult?>, t: Throwable) {
                        //Toast.makeText(this@SignupActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })

        var codeAuth : Boolean? = false

        // 사용자가 받은 이메일에서 '인증 코드' 입력 후 -> 인증코드 확인 버튼 누르면
        btnAuth.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(j_code.getText().toString() == getCode){
                    Toast.makeText(this@SignupActivity, "email code 확인 완료", Toast.LENGTH_SHORT).show()
                    // btnJoin.setEnabled(true)
                    codeAuth = true
                }
                else {
                    Toast.makeText(this@SignupActivity, getCode + "email code wrong", Toast.LENGTH_SHORT).show()
                    codeAuth = false
                }
            }
        })

        // 하단 JOIN 버튼 클릭 - 회원 가입 정보 연동 및 액티비티 종료 ->로그인 페이지 호출
        btnJoin.setOnClickListener {
            // 사용자가 입력한 값들을 String으로 받아오기
            val id = j_id.text.toString()
            val pw = j_pw.text.toString()
            val name = j_name.text.toString()
            val email = j_email.text.toString().trim() // 공백 제거
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
            else { // 유저가 모든 항목을 다 채웠을 경우
                when {
                    emailLayout.error != null -> {
                        Toast.makeText(this@SignupActivity, "이메일을 올바르게 입력해 주세요!", Toast.LENGTH_SHORT).show()
                    }
                    emailCode == false -> {
                        Toast.makeText(this@SignupActivity, "이메일 인증 코드를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                    codeAuth == false -> {
                        Toast.makeText(this@SignupActivity, "이메일 인증 코드를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                if (codeAuth == true) {
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
                                    this@SignupActivity,
                                    "회원가입이 완료되었습니다.", Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                startActivity(intent)
                            } else if (response.code() == 400) {
                                Toast.makeText(
                                    this@SignupActivity, "이미 가입된 정보입니다.",
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
}