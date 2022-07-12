package com.example.harumub_front

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
//import com.example.harumub_front.layout.GridVideoViewContainer
//import com.example.harumub_front.layout.RecyclerItemClickListener
//import com.example.harumub_front.model.User
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
// import kotlinx.android.synthetic.main.activity_watch_together.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class TogetherActivity : AppCompatActivity() {

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

    // 현재 로그인하고 있는 사용자 아이디, 이름
    lateinit var id : String
    lateinit var roomCode : String
    lateinit var roomToken : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_together) // 상하 배치
        // setContentView(R.layout.activity_video_chat_view) // 우측상단 작게 배치

        id = intent.getStringExtra("user_id").toString()
        roomCode = intent.getStringExtra("roomCode").toString()
        roomToken = intent.getStringExtra("roomToken").toString()

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
            // initAgoraEngineAndJoinChannel()
            initAgoraEngineAndJoinChannel(roomCode, roomToken)
        }
    }

    // 엔진 초기화 및 채널 참가 과정
    private fun initAgoraEngineAndJoinChannel(roomCode: String, roomToken: String) {
        initializeAgoraEngine()
        setupVideoProfile()
        setupLocalVideo()
        joinChannel(roomCode, roomToken) // joinChannel()
    }
    // #1
    private fun initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, getString(R.string.agora_app_id), mRtcEventHandler)
        } catch (e: Exception) {
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
    }
    // #3
    private fun setupLocalVideo() {
        val container = findViewById(R.id.local_video_view_container) as FrameLayout
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }
    // #4
    private fun joinChannel(roomCode: String, roomToken: String) { // joinChannel() {
                                // token, channelName
        mRtcEngine!!.joinChannel(roomToken, roomCode, "Extra Optional Data", 0) // uid 명시X > 자동 생성
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
                    initAgoraEngineAndJoinChannel(roomCode, roomToken) // initAgoraEngineAndJoinChannel()
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

        leaveChannel(id) // 채널 떠나기 // leaveChannel()

        RtcEngine.destroy()
        mRtcEngine = null
    }
    // 카메라 끄기 버튼
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
        startActivity(intent)
    }

    // 1:1 >> 하나의 상대방(원격 사용자) 비디오만 출력
    private fun setupRemoteVideo(uid: Int) {
        val container = findViewById(R.id.remote_video_view_container) as FrameLayout

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
    private fun leaveChannel(id: String) {
        mRtcEngine!!.leaveChannel()
    }
    // 상대방이 채널을 떠났을 때
    private fun onRemoteUserLeft() {
        val container = findViewById(R.id.remote_video_view_container) as FrameLayout
        container.removeAllViews()

        val tipMsg = findViewById<TextView>(R.id.quick_tips_when_use_agora_sdk) // optional UI
        tipMsg.visibility = View.VISIBLE
    }
    // 상대방의 비디오가 꺼졌을 때
    private fun onRemoteUserVideoMuted(uid: Int, muted: Boolean) {
        val container = findViewById(R.id.remote_video_view_container) as FrameLayout

        val surfaceView = container.getChildAt(0) as SurfaceView

        val tag = surfaceView.tag
        if (tag != null && tag as Int == uid) {
            surfaceView.visibility = if (muted) View.GONE else View.VISIBLE
        }
    }
    // static
    companion object {
        private val LOG_TAG = TogetherActivity::class.java.simpleName
        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1
    }
}