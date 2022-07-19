package com.example.harumub_front

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amazonaws.util.DateUtils.format
//import com.example.harumub_front.layout.GridVideoViewContainer
//import com.example.harumub_front.layout.RecyclerItemClickListener
//import com.example.harumub_front.model.User
//import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import kotlinx.android.synthetic.main.activity_video_chat_view.*
import okhttp3.internal.http.HttpDate.format
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*

import java.io.File // 캡처
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime

// 1:1 영상통화
class TogetherActivity : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface

    // 현재 로그인하고 있는 사용자 아이디, 이름
    lateinit var id : String
    lateinit var roomCode : String
    lateinit var roomToken : String

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

    private var mRtcEngine: RtcEngine? = null
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread { setupRemoteVideo(uid) }
        }
        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread { onRemoteUserLeft() }
        }
        override fun onUserMuteVideo(uid: Int, muted: Boolean) {
            runOnUiThread { onRemoteUserVideoMuted(uid, muted) }
        }
    }

    lateinit var bitmap : Bitmap
    lateinit var canvas: Canvas
    lateinit var fileScreenshot : File
    var outputStream: FileOutputStream? = null
    lateinit var captureView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_watch_together) // 상하 배치
        setContentView(R.layout.activity_video_chat_view) // 우측상단 작게 배치

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        id = intent.getStringExtra("user_id").toString()
        roomCode = intent.getStringExtra("roomCode").toString()
        roomToken = intent.getStringExtra("roomToken").toString()

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

        // 메인 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id") && intent.hasExtra("roomCode") && intent.hasExtra("roomToken")) {
            Log.e("TogetherActivity", "입장에서 받아온 id : $id , " +
                    "\n방 코드: $roomCode , " +
                    "\n방 토큰: $roomToken")
        } else {
            Log.e("TogetherActivity", "가져온 데이터 없음")
        }
        // 카메라, 오디오 권한 부여 후 엔진 초기화 및 채널 참가
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)
            && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)) {
            Log.e("TogetherActivity", "권한 부여 완료")
            initAgoraEngineAndJoinChannel(roomCode, roomToken)
        } else Log.e("TogetherActivity", "권한 부여 실패")
    }
    // 엔진 초기화 및 채널 참가 과정
    private fun initAgoraEngineAndJoinChannel(roomCode: String, roomToken: String) {
        Log.e("TogetherActivity", "참가 과정 시작")

        initializeAgoraEngine()
        setupVideoProfile()
        setupLocalVideo()
        joinChannel(roomCode, roomToken)
    }
    // #1
    private fun initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, getString(R.string.agora_app_id), mRtcEventHandler)

            Log.e("TogetherActivity", "rtc엔진 생성 완료")
        } catch (e: Exception) { Log.e("TogetherActivity", "rtc엔진 생성 실패")
            Log.e(LOG_TAG, Log.getStackTraceString(e))

            throw RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e))
        }
    }
    // #2
    private fun setupVideoProfile() {
        mRtcEngine!!.enableVideo()
        // mRtcEngine!!.setVideoProfile(Constants.VIDEO_PROFILE_360P, false) // Earlier than 2.3.0

        mRtcEngine!!.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT)
        )

        Log.e("TogetherActivity", "rtc엔진 - 카메라 설정 완료")
    }
    // #3
    private fun setupLocalVideo() {
        val container = findViewById<FrameLayout>(R.id.local_video_view_container)
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))

        Log.e("TogetherActivity", "로컬 사용자 비디오 설정 완료")
    }
    // #4
    // private fun joinChannel() {
    private fun joinChannel(roomCode: String, roomToken: String) {
        // 토큰을 생성하는 데 사용되는 채널 이름에 대해서만 유효한 토큰 사용!!!
        // uid 에러: java.lang.SecurityException: getSerial: The user 10135 does not meet the requirements to access device identifiers.
        // => GUID로 대체도 안 될듯
        // uniqueID = UUID.randomUUID().toString()                     uid: 지정해도 getSerial 해결X
        mRtcEngine!!.joinChannel(roomToken, roomCode, null, 0) // uid 명시X > 자동 생성

        Log.e("TogetherActivity", "채널 참가 완료")
        verifyStoragePermission(this) // 캡처 관련 권한 설정
        // captureScreen() // 임의 화면 캡처 - 수정 필요
    }
    // 화면 캡처 - 수정 필요
    fun captureScreen() {
        // 경로 1
        val now = SimpleDateFormat("yyyyMMdd_hh:mm:ss").format(Date(System.currentTimeMillis()))
        val mPath = cacheDir.absolutePath+"/$now.jpg"
        // 경로 2
        val dirPath : String = Environment.getExternalStorageDirectory().toString()
        var path = dirPath + "/$id-$now.jpeg"

        // var bitmap: Bitmap? = null
        captureView = window.decorView.rootView	//캡처할 뷰

        bitmap = Bitmap.createBitmap(captureView.width, captureView.height, Bitmap.Config.ARGB_8888)
        // val canvas = Canvas(bitmap)
        canvas = Canvas(bitmap)
        captureView.draw(canvas)

        // val imageFile = File(mPath)
        // val outputStream = FileOutputStream(imageFile)
        fileScreenshot = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Calendar.getInstance().getTime().toString()+".jpg"
        )
        outputStream = null

        try {
            outputStream = FileOutputStream(fileScreenshot)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream?.flush()
                // outputStream.close()
            }
            // if(bitmap == null) { return null }
            // Log.d("TogetherActivity", "캡처본 없음")
            // return mPath
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 권한 부여 설정
    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        Log.i(LOG_TAG, "checkSelfPermission $permission $requestCode")
        if (ContextCompat.checkSelfPermission(this,
                permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(permission),
                requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // 원래는 X
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)

        when (requestCode) {
            PERMISSION_REQ_ID_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO)
                    finish()
                }
            }
            PERMISSION_REQ_ID_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // initAgoraEngineAndJoinChannel()
                    initAgoraEngineAndJoinChannel(roomCode, roomToken) // "SeowonChannel"
                } else {
                    showLongToast("No permission for " + Manifest.permission.CAMERA)
                    finish()
                }
            }
        }
    }

    private fun showLongToast(msg: String) {
        this.runOnUiThread { Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show() }
    }

    override fun onDestroy() {
        super.onDestroy()

        leaveChannel() // 채널 떠나기

        RtcEngine.destroy()
        mRtcEngine = null
    }
    // 화면 끄기 버튼
    fun onLocalVideoMuteClicked(view: View) {
        val iv = view as ImageView
        if (iv.isSelected) {
            iv.isSelected = false
            iv.clearColorFilter()
        } else {
            iv.isSelected = true
            iv.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
        }

        mRtcEngine!!.muteLocalVideoStream(iv.isSelected)

        val container = findViewById(R.id.local_video_view_container) as FrameLayout
        val surfaceView = container.getChildAt(0) as SurfaceView
        surfaceView.setZOrderMediaOverlay(!iv.isSelected)
        surfaceView.visibility = if (iv.isSelected) View.GONE else View.VISIBLE
    }
    // 음소거 버튼
    fun onLocalAudioMuteClicked(view: View) {
        val iv = view as ImageView
        if (iv.isSelected) {
            iv.isSelected = false
            iv.clearColorFilter()
        } else {
            iv.isSelected = true
            iv.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
        }

        mRtcEngine!!.muteLocalAudioStream(iv.isSelected)
    }
    // 카메라 방향 전환 버튼
    fun onSwitchCameraClicked(view: View) {
        mRtcEngine!!.switchCamera()
    }
    // 통화 종료 버튼
    fun onEndCallClicked(view: View) {
        finish()

        // 입장 페이지로 다시 돌아가기
        val intent = Intent(applicationContext, EnterActivity::class.java)
        intent.putExtra("user_id", id)
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
        startActivity(intent)
    }

    // 1:1 >> 하나의 상대방(원격 사용자) 비디오만 출력
    private fun setupRemoteVideo(uid: Int) {
        val container = findViewById<FrameLayout>(R.id.remote_video_view_container)

        if (container.childCount >= 1) {
            return
        }
        // video renderer view 생성
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        container.addView(surfaceView)
        // 원격 사용자(상대방)의 비디오 뷰 초기화
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))

        surfaceView.tag = uid // for mark purpose
        val tipMsg = findViewById<TextView>(R.id.quick_tips_when_use_agora_sdk) // optional UI
        tipMsg.visibility = View.GONE
    }
    // 채널을 떠날 때
    private fun leaveChannel() {
        mRtcEngine!!.leaveChannel()
    }
    // 상대방이 채널을 떠났을 때
    private fun onRemoteUserLeft() {
        val container = findViewById<FrameLayout>(R.id.remote_video_view_container)
        container.removeAllViews()

        val tipMsg = findViewById<TextView>(R.id.quick_tips_when_use_agora_sdk) // optional UI
        tipMsg.visibility = View.VISIBLE
    }
    // 상대방의 비디오가 꺼졌을 때
    private fun onRemoteUserVideoMuted(uid: Int, muted: Boolean) {
        val container = findViewById<FrameLayout>(R.id.remote_video_view_container)

        val surfaceView = container.getChildAt(0) as SurfaceView

        val tag = surfaceView.tag
        if (tag != null && tag as Int == uid) {
            surfaceView.visibility = if (muted) View.GONE else View.VISIBLE
        }
    }
    // static -> companion object : 클래스 내부에 정의된 singleton value
    companion object {
        private val LOG_TAG = TogetherActivity::class.java.simpleName
        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1

        private final val REQUEST_EXTERNAL_STORAGE = 1
        private var PERMISSION_STORAGE = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        fun verifyStoragePermission(activity: Activity) {
            val permission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission!=PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE, REQUEST_EXTERNAL_STORAGE)
            }
        }
    }
}