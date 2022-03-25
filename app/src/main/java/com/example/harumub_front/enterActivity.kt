package com.example.harumub_front

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_enter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class enterActivity: AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        createNewroom.setOnClickListener{
            val map = HashMap<String, String>()
            val call = retrofitInterface.executeMakeRoom(map)
            call!!.enqueue(object : Callback<MakeRoomResult?> {
                override fun onResponse(call: Call<MakeRoomResult?>, response: Response<MakeRoomResult?>) {
                    if (response.code() == 200) {
                        val result = response.body()
                        val builder1 = androidx.appcompat.app.AlertDialog.Builder(this@enterActivity)
                        builder1.setTitle("방 생성 성공, 초대코드 : " + result!!.roomCode)
                        builder1.show()

                        var intent = Intent(applicationContext, togetherActivity::class.java) // 두번째 인자에 이동할 액티비티
                        intent.putExtra("roomCode", result.roomCode)
                        startActivityForResult(intent, 0)
                    }
                    else if (response.code() == 404) {
                        Toast.makeText(this@enterActivity, "404 오류", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<MakeRoomResult?>, t: Throwable) {
                    Toast.makeText(this@enterActivity, t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
            var intent = Intent(applicationContext, togetherActivity::class.java)
            startActivityForResult(intent, 0)
        }

        writeCode.setOnClickListener() {
            var dig = AlertDialog.Builder(this)

            var dialogView = View.inflate(this, R.layout.dialog_entercode, null)
            dig.setView(dialogView)
            dig.setPositiveButton("확인") { dialog, which ->
                var intent = Intent(applicationContext, togetherActivity::class.java)
                startActivityForResult(intent, 0)
            }
            dig.setNegativeButton("취소") { dialog, which ->
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show()
            }
            dig.show()
        }
    }
}