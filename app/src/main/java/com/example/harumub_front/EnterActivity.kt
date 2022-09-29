package com.example.harumub_front

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_enter.*
import kotlinx.android.synthetic.main.dialog_entercode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.HashMap

class EnterActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInterface

    lateinit var enter_this : androidx.drawerlayout.widget.DrawerLayout
    lateinit var drawer_button : ImageButton
    lateinit var recent_button: ImageButton
    lateinit var drawer_view : NavigationView
    lateinit var message : TextView

    // 현재 로그인하고 있는 사용자 아이디, 이름
    lateinit var id : String

    // 추천 정보
    lateinit var reco1_titleArray : ArrayList<String>
    lateinit var reco1_posterArray : ArrayList<String>
    lateinit var reco2_1_userId : String
    lateinit var reco2_2_userId : String
    lateinit var reco2_3_userId : String
    lateinit var reco2_4_userId : String
    lateinit var reco2_5_userId : String
    lateinit var reco2_1_title : ArrayList<String>
    lateinit var reco2_2_title : ArrayList<String>
    lateinit var reco2_3_title : ArrayList<String>
    lateinit var reco2_4_title : ArrayList<String>
    lateinit var reco2_5_title : ArrayList<String>
    lateinit var reco2_1_poster : ArrayList<String>
    lateinit var reco2_2_poster : ArrayList<String>
    lateinit var reco2_3_poster : ArrayList<String>
    lateinit var reco2_4_poster : ArrayList<String>
    lateinit var reco2_5_poster : ArrayList<String>
    lateinit var reco3_titleArray : ArrayList<String>
    lateinit var reco3_posterArray : ArrayList<String>
    lateinit var reco4_year : String
    lateinit var reco4_titleArray : ArrayList<String>
    lateinit var reco4_posterArray : ArrayList<String>
    lateinit var reco6_titleArray : ArrayList<String>
    lateinit var reco6_posterArray : ArrayList<String>

    lateinit var noBtn : Button
    lateinit var yesBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        id = intent.getStringExtra("user_id").toString()

        reco1_titleArray = intent.getSerializableExtra("reco1_titleArray") as ArrayList<String>
        reco1_posterArray = intent.getSerializableExtra("reco1_posterArray") as ArrayList<String>
        reco2_1_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_2_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_3_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_4_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_5_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_1_title = intent.getSerializableExtra("reco2_1_title") as ArrayList<String>
        reco2_2_title = intent.getSerializableExtra("reco2_2_title") as ArrayList<String>
        reco2_3_title = intent.getSerializableExtra("reco2_3_title") as ArrayList<String>
        reco2_4_title = intent.getSerializableExtra("reco2_4_title") as ArrayList<String>
        reco2_5_title = intent.getSerializableExtra("reco2_5_title") as ArrayList<String>
        reco2_1_poster = intent.getSerializableExtra("reco2_1_poster") as ArrayList<String>
        reco2_2_poster = intent.getSerializableExtra("reco2_2_poster") as ArrayList<String>
        reco2_3_poster = intent.getSerializableExtra("reco2_3_poster") as ArrayList<String>
        reco2_4_poster = intent.getSerializableExtra("reco2_4_poster") as ArrayList<String>
        reco2_5_poster = intent.getSerializableExtra("reco2_5_poster") as ArrayList<String>
        reco3_titleArray = intent.getSerializableExtra("reco3_titleArray") as ArrayList<String>
        reco3_posterArray = intent.getSerializableExtra("reco3_posterArray") as ArrayList<String>
        reco4_year = intent.getStringExtra("reco4_year").toString()
        reco4_titleArray = intent.getSerializableExtra("reco4_titleArray") as ArrayList<String>
        reco4_posterArray = intent.getSerializableExtra("reco4_posterArray") as ArrayList<String>
        reco6_titleArray = intent.getSerializableExtra("reco6_titleArray") as ArrayList<String>
        reco6_posterArray = intent.getSerializableExtra("reco6_posterArray") as ArrayList<String>

        // 메인 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id")) {
            Log.e("EnterActivity", "메인에서 받아온 id : $id")
        } else {
            Log.e("EnterActivity", "가져온 데이터 없음")
        }

        enter_this = findViewById(R.id.enter_drawer)
        drawer_button = findViewById(R.id.drawer_button) // 드로어 열기(메뉴버튼)
        drawer_view = findViewById(R.id.drawer_view) // 드로어
        val drawerHeader = drawer_view.getHeaderView(0) // 드로어 헤더
        recent_button = findViewById(R.id.recent) // 최근 감상기록 버튼

        // 드로어 버튼 클릭 -> 드로어 메뉴 열기
        drawer_button.setOnClickListener{
            enter_this.openDrawer(GravityCompat.START) // START = left, END : right (드로어가 나오는 방향지정)
        }
        // 네비게이션 메뉴 아이템에 클릭 속성 부여
        drawer_view.setNavigationItemSelectedListener(this)

        // 최근 감상 기록 (시계) 버튼 클릭 -> 페이지 이동
        recent_button.setOnClickListener{
            val intent = Intent(this, WatchListActivity::class.java)
            intent.putExtra("user_id", id)

            intent.putExtra("reco1_titleArray", reco1_titleArray)
            intent.putExtra("reco1_posterArray", reco1_posterArray)

            intent.putExtra("reco2_1_userId", reco2_1_userId)
            intent.putExtra("reco2_2_userId", reco2_2_userId)
            intent.putExtra("reco2_3_userId", reco2_3_userId)
            intent.putExtra("reco2_4_userId", reco2_4_userId)
            intent.putExtra("reco2_5_userId", reco2_5_userId)

            intent.putExtra("reco2_1_title", reco2_1_title)
            intent.putExtra("reco2_2_title", reco2_2_title)
            intent.putExtra("reco2_3_title", reco2_3_title)
            intent.putExtra("reco2_4_title", reco2_4_title)
            intent.putExtra("reco2_5_title", reco2_5_title)

            intent.putExtra("reco2_1_poster", reco2_1_poster)
            intent.putExtra("reco2_2_poster", reco2_2_poster)
            intent.putExtra("reco2_3_poster", reco2_3_poster)
            intent.putExtra("reco2_4_poster", reco2_4_poster)
            intent.putExtra("reco2_5_poster", reco2_5_poster)

            intent.putExtra("reco3_titleArray", reco3_titleArray)
            intent.putExtra("reco3_posterArray", reco3_posterArray)

            intent.putExtra("reco4_year", reco4_year)
            intent.putExtra("reco4_titleArray", reco4_titleArray)
            intent.putExtra("reco4_posterArray", reco4_posterArray)

            intent.putExtra("reco6_titleArray", reco6_titleArray)
            intent.putExtra("reco6_posterArray", reco6_posterArray)
            startActivity(intent)
        }

        // #1. 방 생성 버튼 - 방 코드 생성 및 자동 복사 > 방 입장
        createNewroom.setOnClickListener {
            val map = HashMap<String, String>()
            map.put("id", id) // map["id"] = id
            map.put("role", "publisher")

            val call = retrofitInterface.executeMakeRoom(map)
            call!!.enqueue(object : Callback<MakeRoomResult?> {
                override fun onResponse(call: Call<MakeRoomResult?>, response: Response<MakeRoomResult?>) {
                    if (response.code() == 200) {
                        val result = response.body()

                        // 알림창 다이얼로그 띄우기
                        val dig = AlertDialog.Builder(this@EnterActivity)
                        val dialogView =
                            View.inflate(this@EnterActivity, R.layout.dialog_entercode_copy, null)
                        message = dialogView.findViewById(R.id.myEnterCode)
                        message.text = result!!.roomCode
                        dig.setView(dialogView)

                        // 다이얼로그 확인 버튼 클릭
                        dig.setPositiveButton("확인") { dialog, which ->
                            // 초대코드 클립보드에 자동 복사
                            val clipboardManager =
                                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager // 클립보드 매니저 호출
                            val clip: ClipData = ClipData.newPlainText(
                                "roomCode",
                                result.roomCode
                            ) // roomCode 이름표로 값 복사하여 저장
                            clipboardManager.setPrimaryClip(clip)

                            Toast.makeText(
                                this@EnterActivity,
                                "초대 코드 클립보드에 복사 완료!",
                                Toast.LENGTH_SHORT
                            ).show()

                            // 호스트로 방 입장
                            val intent = Intent(applicationContext, TogetherActivity::class.java)
                            intent.putExtra("user_id", id)
                            //intent.putExtra("roomCode", result.roomCode)
                            //intent.putExtra("roomToken", result.roomToken)
                            intent.putExtra("roomCode", getString(R.string.CHANNELNAME))
                            intent.putExtra("roomToken", getString(R.string.RTC_TOKEN))

                            intent.putExtra("reco1_titleArray", reco1_titleArray)
                            intent.putExtra("reco1_posterArray", reco1_posterArray)
                            intent.putExtra("reco2_1_userId", reco2_1_userId)
                            intent.putExtra("reco2_2_userId", reco2_2_userId)
                            intent.putExtra("reco2_3_userId", reco2_3_userId)
                            intent.putExtra("reco2_4_userId", reco2_4_userId)
                            intent.putExtra("reco2_5_userId", reco2_5_userId)
                            intent.putExtra("reco2_1_title", reco2_1_title)
                            intent.putExtra("reco2_2_title", reco2_2_title)
                            intent.putExtra("reco2_3_title", reco2_3_title)
                            intent.putExtra("reco2_4_title", reco2_4_title)
                            intent.putExtra("reco2_5_title", reco2_5_title)
                            intent.putExtra("reco2_1_poster", reco2_1_poster)
                            intent.putExtra("reco2_2_poster", reco2_2_poster)
                            intent.putExtra("reco2_3_poster", reco2_3_poster)
                            intent.putExtra("reco2_4_poster", reco2_4_poster)
                            intent.putExtra("reco2_5_poster", reco2_5_poster)
                            intent.putExtra("reco3_titleArray", reco3_titleArray)
                            intent.putExtra("reco3_posterArray", reco3_posterArray)
                            intent.putExtra("reco4_year", reco4_year)
                            intent.putExtra("reco4_titleArray", reco4_titleArray)
                            intent.putExtra("reco4_posterArray", reco4_posterArray)
                            intent.putExtra("reco6_titleArray", reco6_titleArray)
                            intent.putExtra("reco6_posterArray", reco6_posterArray)
                            startActivity(intent)
                            Toast.makeText(
                                this@EnterActivity,
                                "방 코드 [" + result.roomCode + "]에 HOST로 입장합니다.", Toast.LENGTH_SHORT
                            ).show()
                        }
                        dig.show()
                    } else if (response.code() == 400) {
                        Log.e("EnterActivity", "방 생성 중 오류 발생")
                        //Toast.makeText(this@EnterActivity, "방 생성 중 오류 발생", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<MakeRoomResult?>, t: Throwable) {
                    Log.e("EnterActivity", t.message!!)

//                    Toast.makeText(
//                        this@EnterActivity, t.message,
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            })
        }

        // #2. 방 입장 버튼 - 참가자가 다이얼로그에 방 코드 입력 후 방 입장
        writeCode.setOnClickListener() { // 초대코드 입장 버튼 클릭 시 다이얼로그 띄워 줌
            val dig = AlertDialog.Builder(this)
            val dialogView = View.inflate(this, R.layout.dialog_entercode, null)
            dig.setView(dialogView)

            noBtn = dialogView.findViewById<Button>(R.id.noBtn)
            yesBtn = dialogView.findViewById<Button>(R.id.yesBtn)

            // 확인 버튼 클릭 - 같이 보기 페이지로 이동
            yesBtn.setOnClickListener {
                //Toast.makeText(this@EnterActivity, "확인 누름", Toast.LENGTH_LONG).show()
                val codeEdit = dialogView.findViewById<EditText>(R.id.code_edittext)
                val getroomCode = codeEdit.text.toString()

                val map = HashMap<String, String>()
                map.put("id", id)
                map.put("role", "subscriber")
                map.put("roomCode", getroomCode)

                val call = retrofitInterface.executeEnterRoom(map)
                call!!.enqueue(object : Callback<EnterRoomResult?> {
                    override fun onResponse(call: Call<EnterRoomResult?>, response: Response<EnterRoomResult?>) {
                        if (response.code() == 200) {
                            val result = response.body()

                            Toast.makeText(this@EnterActivity,
                                "방 코드 [" + getroomCode + "]에 참가자로 입장합니다.", Toast.LENGTH_LONG).show()

                            val intent = Intent(applicationContext, TogetherActivity::class.java)
                            intent.putExtra("user_id", id)
                            intent.putExtra("roomCode", getroomCode)
                            //intent.putExtra("roomToken", result?.roomToken)
                            intent.putExtra("roomToken", getString(R.string.RTC_TOKEN))

                            intent.putExtra("reco1_titleArray", reco1_titleArray)
                            intent.putExtra("reco1_posterArray", reco1_posterArray)
                            intent.putExtra("reco2_1_userId", reco2_1_userId)
                            intent.putExtra("reco2_2_userId", reco2_2_userId)
                            intent.putExtra("reco2_3_userId", reco2_3_userId)
                            intent.putExtra("reco2_4_userId", reco2_4_userId)
                            intent.putExtra("reco2_5_userId", reco2_5_userId)
                            intent.putExtra("reco2_1_title", reco2_1_title)
                            intent.putExtra("reco2_2_title", reco2_2_title)
                            intent.putExtra("reco2_3_title", reco2_3_title)
                            intent.putExtra("reco2_4_title", reco2_4_title)
                            intent.putExtra("reco2_5_title", reco2_5_title)
                            intent.putExtra("reco2_1_poster", reco2_1_poster)
                            intent.putExtra("reco2_2_poster", reco2_2_poster)
                            intent.putExtra("reco2_3_poster", reco2_3_poster)
                            intent.putExtra("reco2_4_poster", reco2_4_poster)
                            intent.putExtra("reco2_5_poster", reco2_5_poster)
                            intent.putExtra("reco3_titleArray", reco3_titleArray)
                            intent.putExtra("reco3_posterArray", reco3_posterArray)
                            intent.putExtra("reco4_year", reco4_year)
                            intent.putExtra("reco4_titleArray", reco4_titleArray)
                            intent.putExtra("reco4_posterArray", reco4_posterArray)
                            intent.putExtra("reco6_titleArray", reco6_titleArray)
                            intent.putExtra("reco6_posterArray", reco6_posterArray)

                            // startActivityForResult(intent, 0)
                            startActivity(intent)
                        }
                        else if (response.code() == 400) {
                            Toast.makeText(this@EnterActivity, "잘못된 방 코드 입니다.",
                                Toast.LENGTH_LONG).show()

                            Log.e("EnterActivity", "방 코드 존재X / 토큰이 유효하지 않음")
                        }
                    }
                    override fun onFailure(call: Call<EnterRoomResult?>, t: Throwable) {
                        Log.e("EnterActivity", t.message!!)
//                        Toast.makeText(this@EnterActivity, t.message,
//                            Toast.LENGTH_LONG).show()
                    }
                })
            } // 취소 버튼 클릭 시 취소되었다는 토스트 메세지를 보여 줌
            noBtn.setOnClickListener {
                //Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show()
            }
            dig.show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {// 네비게이션 메뉴 아이템 클릭 시 수행
        when(item.itemId){ // 드로어 메뉴 눌렸을 시 수행. 수정 필요
            R.id.drawer_UserRecord -> {
                // fragment manager 가져와서 fragment transaction 생성
                with(supportFragmentManager.beginTransaction()) {
                    //Toast.makeText(applicationContext, "사용자 기록보기", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, WatchListActivity::class.java)
                    intent.putExtra("user_id", id)
                    intent.putExtra("reco1_titleArray", reco1_titleArray)
                    intent.putExtra("reco1_posterArray", reco1_posterArray)
                    intent.putExtra("reco2_1_userId", reco2_1_userId)
                    intent.putExtra("reco2_2_userId", reco2_2_userId)
                    intent.putExtra("reco2_3_userId", reco2_3_userId)
                    intent.putExtra("reco2_4_userId", reco2_4_userId)
                    intent.putExtra("reco2_5_userId", reco2_5_userId)
                    intent.putExtra("reco2_1_title", reco2_1_title)
                    intent.putExtra("reco2_2_title", reco2_2_title)
                    intent.putExtra("reco2_3_title", reco2_3_title)
                    intent.putExtra("reco2_4_title", reco2_4_title)
                    intent.putExtra("reco2_5_title", reco2_5_title)
                    intent.putExtra("reco2_1_poster", reco2_1_poster)
                    intent.putExtra("reco2_2_poster", reco2_2_poster)
                    intent.putExtra("reco2_3_poster", reco2_3_poster)
                    intent.putExtra("reco2_4_poster", reco2_4_poster)
                    intent.putExtra("reco2_5_poster", reco2_5_poster)
                    intent.putExtra("reco3_titleArray", reco3_titleArray)
                    intent.putExtra("reco3_posterArray", reco3_posterArray)
                    intent.putExtra("reco4_year", reco4_year)
                    intent.putExtra("reco4_titleArray", reco4_titleArray)
                    intent.putExtra("reco4_posterArray", reco4_posterArray)
                    intent.putExtra("reco6_titleArray", reco6_titleArray)
                    intent.putExtra("reco6_posterArray", reco6_posterArray)
                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_WatchAlone -> {
                with(supportFragmentManager.beginTransaction()) {
                    //Toast.makeText(applicationContext, "혼자 보기", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, SearchActivity::class.java)
                    intent.putExtra("user_id", id)
                    intent.putExtra("reco1_titleArray", reco1_titleArray)
                    intent.putExtra("reco1_posterArray", reco1_posterArray)
                    intent.putExtra("reco2_1_userId", reco2_1_userId)
                    intent.putExtra("reco2_2_userId", reco2_2_userId)
                    intent.putExtra("reco2_3_userId", reco2_3_userId)
                    intent.putExtra("reco2_4_userId", reco2_4_userId)
                    intent.putExtra("reco2_5_userId", reco2_5_userId)
                    intent.putExtra("reco2_1_title", reco2_1_title)
                    intent.putExtra("reco2_2_title", reco2_2_title)
                    intent.putExtra("reco2_3_title", reco2_3_title)
                    intent.putExtra("reco2_4_title", reco2_4_title)
                    intent.putExtra("reco2_5_title", reco2_5_title)
                    intent.putExtra("reco2_1_poster", reco2_1_poster)
                    intent.putExtra("reco2_2_poster", reco2_2_poster)
                    intent.putExtra("reco2_3_poster", reco2_3_poster)
                    intent.putExtra("reco2_4_poster", reco2_4_poster)
                    intent.putExtra("reco2_5_poster", reco2_5_poster)
                    intent.putExtra("reco3_titleArray", reco3_titleArray)
                    intent.putExtra("reco3_posterArray", reco3_posterArray)
                    intent.putExtra("reco4_year", reco4_year)
                    intent.putExtra("reco4_titleArray", reco4_titleArray)
                    intent.putExtra("reco4_posterArray", reco4_posterArray)
                    intent.putExtra("reco6_titleArray", reco6_titleArray)
                    intent.putExtra("reco6_posterArray", reco6_posterArray)
                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_WatchTogether -> {
                with(supportFragmentManager.beginTransaction()) {
                    //Toast.makeText(applicationContext, "같이 보기", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, EnterActivity::class.java)
                    intent.putExtra("user_id", id)
                    intent.putExtra("reco1_titleArray", reco1_titleArray)
                    intent.putExtra("reco1_posterArray", reco1_posterArray)
                    intent.putExtra("reco2_1_userId", reco2_1_userId)
                    intent.putExtra("reco2_2_userId", reco2_2_userId)
                    intent.putExtra("reco2_3_userId", reco2_3_userId)
                    intent.putExtra("reco2_4_userId", reco2_4_userId)
                    intent.putExtra("reco2_5_userId", reco2_5_userId)
                    intent.putExtra("reco2_1_title", reco2_1_title)
                    intent.putExtra("reco2_2_title", reco2_2_title)
                    intent.putExtra("reco2_3_title", reco2_3_title)
                    intent.putExtra("reco2_4_title", reco2_4_title)
                    intent.putExtra("reco2_5_title", reco2_5_title)
                    intent.putExtra("reco2_1_poster", reco2_1_poster)
                    intent.putExtra("reco2_2_poster", reco2_2_poster)
                    intent.putExtra("reco2_3_poster", reco2_3_poster)
                    intent.putExtra("reco2_4_poster", reco2_4_poster)
                    intent.putExtra("reco2_5_poster", reco2_5_poster)
                    intent.putExtra("reco3_titleArray", reco3_titleArray)
                    intent.putExtra("reco3_posterArray", reco3_posterArray)
                    intent.putExtra("reco4_year", reco4_year)
                    intent.putExtra("reco4_titleArray", reco4_titleArray)
                    intent.putExtra("reco4_posterArray", reco4_posterArray)
                    intent.putExtra("reco6_titleArray", reco6_titleArray)
                    intent.putExtra("reco6_posterArray", reco6_posterArray)
                    startActivityForResult(intent, 0) // + 결과값 전달 // requestCode: 액티비티 식별값 - 원하는 값
                    commit()
                }
            }
            R.id.drawer_Help -> {
                with(supportFragmentManager.beginTransaction()) {
                    //Toast.makeText(applicationContext, "도움말", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, HelpActivity::class.java)
                    startActivityForResult(intent, 0)
                    commit()
                } // 프래그먼트 트랜잭션 변경 후 commit() 호출해야 변경 내용 적용
            }
            R.id.drawer_Logout -> {
                with(supportFragmentManager.beginTransaction()) {
                    val map = HashMap<String, String>()

                    val call = retrofitInterface.executeLogout(map)
                    call!!.enqueue(object : Callback<Void?> {
                        override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                            if (response.code() == 200) {
                                val intent = Intent(applicationContext, LoginActivity::class.java) // 두번째 인자에 이동할 액티비티

                                Toast.makeText(this@EnterActivity, "로그아웃합니다..", Toast.LENGTH_LONG).show()
                                startActivity(intent)
                            }
                        }

                        override fun onFailure(call: Call<Void?>, t: Throwable) {
                            Log.e("EnterActivity", t.message!!)
//                            Toast.makeText(this@EnterActivity, t.message,
//                                Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }
        enter_this.closeDrawers() // 네비게이션 뷰 닫기
        return false
    }
}