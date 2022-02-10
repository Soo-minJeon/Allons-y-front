package com.example.harumub_front

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_enter.*

class enterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        createNewroom.setOnClickListener{
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