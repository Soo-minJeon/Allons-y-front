package com.example.harumub_front

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_signup_auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity_auth : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInterface

    lateinit var progress : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_auth)


        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        val j_id = findViewById<EditText>(R.id.id)
        val j_pw = findViewById<EditText>(R.id.pw)
        val j_name = findViewById<EditText>(R.id.name)
        val j_email = findViewById<EditText>(R.id.email)
        val j_code = findViewById<EditText>(R.id.code)

        // 상단 진행 바
        progress = findViewById<CardView>(R.id.signup_progress)

        val btnReq = findViewById<Button>(R.id.btn_request)
        val btnAuth = findViewById<Button>(R.id.btn_auth)

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

                            Toast.makeText(this@SignupActivity_auth, "email send successfully", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@SignupActivity_auth, "email code 확인 완료", Toast.LENGTH_SHORT).show()
                    // btnJoin.setEnabled(true)
                    codeAuth = true

                    val intent = Intent(this@SignupActivity_auth, SignupActivity_fav::class.java)
                    intent.putExtra("user_id", j_id.text.toString())
                    intent.putExtra("user_password", j_pw.text.toString())
                    intent.putExtra("user_name", j_name.text.toString())
                    intent.putExtra("user_email", j_email.text.toString())
                    intent.putExtra("user_codeAuth", codeAuth.toString())

                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        this@SignupActivity_auth,
                        progress,
                        "signup_progress"
                    )

                    startActivity(intent, options.toBundle())


                }
                else {
                    Toast.makeText(this@SignupActivity_auth, getCode + "email code wrong", Toast.LENGTH_SHORT).show()
                    codeAuth = false
                }
            }
        })


    }
}