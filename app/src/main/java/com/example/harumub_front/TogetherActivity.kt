package com.example.harumub_front

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration

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
    var id = intent.getStringExtra("user_id").toString()
    // 채널 참여를 위해 메소드 밖에 초기화
    var roomCode = intent.getStringExtra("roomCode").toString()
    var roomToken = intent.getStringExtra("roomToken").toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_chat_view)
        // setContentView(R.layout.activity_watch_together)

        // 메인 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id") && intent.hasExtra("roomCode") && intent.hasExtra("roomToken")) {
            Log.e("TogetherActivity", "입장에서 받아온 id : $id , 방 코드: $roomCode , 방 토큰: $roomToken")
        } else {
            Log.e("TogetherActivity", "가져온 데이터 없음")
        }
        // 카메라, 오디오 권한 부여 후 엔진 초기화 및 채널 참가
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)
            && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)) {
            initAgoraEngineAndJoinChannel()
        }
    }
    // 엔진 초기화 및 채널 참가 과정
    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        setupVideoProfile()
        setupLocalVideo()
        joinChannel()
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
                    initAgoraEngineAndJoinChannel()
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
    fun onEncCallClicked(view: View) {
        finish()
    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, getString(R.string.agora_app_id), mRtcEventHandler)
        } catch (e: Exception) {
            Log.e(LOG_TAG, Log.getStackTraceString(e))

            throw RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e))
        }
    }

    private fun setupVideoProfile() {
        mRtcEngine!!.enableVideo()
        // mRtcEngine!!.setVideoProfile(Constants.VIDEO_PROFILE_360P, false) // Earlier than 2.3.0

       mRtcEngine!!.setVideoEncoderConfiguration(VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x360,
            VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
            VideoEncoderConfiguration.STANDARD_BITRATE,
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT))
    }

    private fun setupLocalVideo() {
         val container = findViewById(R.id.local_video_view_container) as FrameLayout
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    private fun joinChannel() {
        // 토큰을 생성하는 데 사용되는 채널 이름에 대해서만 유효한 토큰 사용!!!
                                // token, channelName
        mRtcEngine!!.joinChannel(roomToken, roomCode, "Extra Optional Data", 0) // uid 명시X > 자동 생성
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
    private fun leaveChannel() {
        mRtcEngine!!.leaveChannel()
        // 입장 페이지로 다시 돌아가기
        val intent = Intent(applicationContext, EnterActivity::class.java)
        intent.putExtra("user_id", id)
        startActivity(intent)
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

    companion object {
        private val LOG_TAG = TogetherActivity::class.java.simpleName
        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1
    }
}