package com.example.harumub_front

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.os.Build
import com.example.harumub_front.model.ConstantApp
import com.example.harumub_front.BaseActivity
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.harumub_front.AGApplication
import io.agora.rtc.RtcEngine
import com.example.harumub_front.model.EngineConfig
import com.example.harumub_front.model.AGEventHandler
import com.example.harumub_front.model.CurrentUserSettings
import android.widget.Toast
import android.util.DisplayMetrics
import android.content.res.TypedArray
import android.os.Handler
import io.agora.rtc.video.VideoCanvas
import com.example.harumub_front.R
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.example.harumub_front.propeller.Constant
import io.agora.rtc.video.VideoEncoderConfiguration.VideoDimensions
import io.agora.rtc.video.VideoEncoderConfiguration.FRAME_RATE
import io.agora.rtc.internal.EncryptionConfig
import io.agora.rtc.video.VideoEncoderConfiguration
//import org.slf4j.LoggerFactory
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = findViewById<View>(Window.ID_ANDROID_CONTENT)
        val vto = layout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                } else {
//                    layout.viewTreeObserver.removeGlobalOnLayoutListener(this)
//                }
                layout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                initUIandEvent()
            }
        })
    }

    protected abstract fun initUIandEvent()
    protected abstract fun deInitUIandEvent()
    protected open fun permissionGranted() {}

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Handler().postDelayed(Runnable {
            if (isFinishing) {
                return@Runnable
            }
            val checkPermissionResult = checkSelfPermissions()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // so far we do not use OnRequestPermissionsResultCallback
            }
        }, 500)
    }

    private fun checkSelfPermissions(): Boolean {
        return checkSelfPermission(
            Manifest.permission.RECORD_AUDIO,
            ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO
        ) &&
                checkSelfPermission(
                    Manifest.permission.CAMERA,
                    ConstantApp.PERMISSION_REQ_ID_CAMERA
                )
    }

    override fun onDestroy() {
        deInitUIandEvent()
        super.onDestroy()
    }

    fun closeIME(v: View) {
        val mgr = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(v.windowToken, 0) // 0 force close IME
        v.clearFocus()
    }

    fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        Log.d("BaseActivity", "checkSelfPermission $permission $requestCode")
        //log.debug("checkSelfPermission $permission $requestCode")
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(permission),
                requestCode
            )
            return false
        }
        if (Manifest.permission.CAMERA == permission) {
            permissionGranted()
        }
        return true
    }

    protected fun application(): AGApplication {
        return application as AGApplication
    }

    protected fun rtcEngine(): RtcEngine {
        return application().rtcEngine()!!
    }

    protected fun config(): EngineConfig {
        return application().config()!!
    }

    protected fun addEventHandler(handler: AGEventHandler?) {
        application().addEventHandler(handler)
    }

    protected fun removeEventHandler(handler: AGEventHandler?) {
        application().remoteEventHandler(handler)
    }

//    protected fun vSettings(): CurrentUserSettings {
//        return application().userSettings()
//    }

    fun showLongToast(msg: String?) {
        runOnUiThread { Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        // 원래는 없었음
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("BaseActivity",
            "onRequestPermissionsResult " + requestCode + " "
                    + Arrays.toString(permissions) + " " + Arrays.toString(grantResults)
        )
            //log.debug("onRequestPermissionsResult " + requestCode + " " + Arrays.toString(permissions) + " " + Arrays.toString(grantResults))
        when (requestCode) {
            ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    checkSelfPermission(
                        Manifest.permission.CAMERA,
                        ConstantApp.PERMISSION_REQ_ID_CAMERA
                    )
                } else {
                    finish()
                }
            }
            ConstantApp.PERMISSION_REQ_ID_CAMERA -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    permissionGranted()
                } else {
                    finish()
                }
            }
        }
    }

    protected fun virtualKeyHeight(): Int {
        val hasPermanentMenuKey = ViewConfiguration.get(application).hasPermanentMenuKey()
        if (hasPermanentMenuKey) {
            return 0
        }

        // Also can use getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        val metrics = DisplayMetrics()
        val display = windowManager.defaultDisplay
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            display.getRealMetrics(metrics)
//        } else {
//            display.getMetrics(metrics)
//        }
        display.getRealMetrics(metrics)

        var fullHeight = metrics.heightPixels
        var fullWidth = metrics.widthPixels
        if (fullHeight < fullWidth) {
            fullHeight = fullHeight xor fullWidth
            fullWidth = fullWidth xor fullHeight
            fullHeight = fullHeight xor fullWidth
        }
        display.getMetrics(metrics)
        var newFullHeight = metrics.heightPixels
        var newFullWidth = metrics.widthPixels
        if (newFullHeight < newFullWidth) {
            newFullHeight = newFullHeight xor newFullWidth
            newFullWidth = newFullWidth xor newFullHeight
            newFullHeight = newFullHeight xor newFullWidth
        }
        var virtualKeyHeight = fullHeight - newFullHeight
        if (virtualKeyHeight > 0) {
            return virtualKeyHeight
        }
        virtualKeyHeight = fullWidth - newFullWidth
        return virtualKeyHeight
    }

    // status bar height
    protected val statusBarHeight: Int
        protected get() {
            // status bar height
            var statusBarHeight = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resourceId)
            }
            if (statusBarHeight == 0) {
                Log.e("BaseActivity","Can not get height of status bar")
                //log.error("Can not get height of status bar")
            }
            return statusBarHeight
        }

    // action bar height
    protected val actionBarHeight: Int
        protected get() {
            // action bar height
            var actionBarHeight = 0
            val styledAttributes =
                this.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
            actionBarHeight = styledAttributes.getDimension(0, 0f).toInt()
            styledAttributes.recycle()
            if (actionBarHeight == 0) {
                Log.e("BaseActivity","Can not get height of action bar")
                //log.error("Can not get height of action bar")
            }
            return actionBarHeight
        }

    /**
     *
     * Starts/Stops the local video preview
     *
     * Before calling this method, you must:
     * Call the enableVideo method to enable the video.
     *
     * @param start Whether to start/stop the local preview
     * @param view The SurfaceView in which to render the preview
     * @param uid User ID.
     */
    protected fun preview(start: Boolean, view: SurfaceView?, uid: Int) {
        if (start) {
            rtcEngine().setupLocalVideo(VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid))
            rtcEngine().startPreview()
        } else {
            rtcEngine().stopPreview()
        }
    }

    /**
     * Allows a user to join a channel.
     *
     * Users in the same channel can talk to each other, and multiple users in the same channel can start a group chat. Users with different App IDs cannot call each other.
     *
     * You must call the leaveChannel method to exit the current call before joining another channel.
     *
     * A successful joinChannel method call triggers the following callbacks:
     *
     * The local client: onJoinChannelSuccess.
     * The remote client: onUserJoined, if the user joining the channel is in the Communication profile, or is a BROADCASTER in the Live Broadcast profile.
     *
     * When the connection between the client and Agora's server is interrupted due to poor
     * network conditions, the SDK tries reconnecting to the server. When the local client
     * successfully rejoins the channel, the SDK triggers the onRejoinChannelSuccess callback
     * on the local client.
     *
     * @param channel The unique channel name for the AgoraRTC session in the string format.
     * @param uid User ID.
     */
    fun afterJoinChannel(channel: String, uid: Int) {
        config().mChannel = channel
        enablePreProcessor()
        Log.d("BaseActivity","joinChannel $channel $uid")
        //log.debug("joinChannel $channel $uid")
    }

    /**
     * Allows a user to leave a channel.
     *
     * After joining a channel, the user must call the leaveChannel method to end the call before
     * joining another channel. This method returns 0 if the user leaves the channel and releases
     * all resources related to the call. This method call is asynchronous, and the user has not
     * exited the channel when the method call returns. Once the user leaves the channel,
     * the SDK triggers the onLeaveChannel callback.
     *
     * A successful leaveChannel method call triggers the following callbacks:
     *
     * The local client: onLeaveChannel.
     * The remote client: onUserOffline, if the user leaving the channel is in the
     * Communication channel, or is a BROADCASTER in the Live Broadcast profile.
     *
     * @param channel Channel Name
     */
    fun leaveChannel(channel: String) {
        Log.d("BaseActivity","leaveChannel $channel")
        //log.debug("leaveChannel $channel")
        config().mChannel = null
        disablePreProcessor()
        rtcEngine().leaveChannel()
        config().reset()
    }

    /**
     * Enables image enhancement and sets the options.
     */
    protected fun enablePreProcessor() {
        if (Constant.BEAUTY_EFFECT_ENABLED) {
            rtcEngine().setBeautyEffectOptions(true, Constant.BEAUTY_OPTIONS)
        }
    }

    fun setBeautyEffectParameters(lightness: Float, smoothness: Float, redness: Float) {
        Constant.BEAUTY_OPTIONS.lighteningLevel = lightness
        Constant.BEAUTY_OPTIONS.smoothnessLevel = smoothness
        Constant.BEAUTY_OPTIONS.rednessLevel = redness
    }

    /**
     * Disables image enhancement.
     */
    protected fun disablePreProcessor() {
        // do not support null when setBeautyEffectOptions to false
        rtcEngine().setBeautyEffectOptions(false, Constant.BEAUTY_OPTIONS)
    }

    protected fun configEngine(
        videoDimension: VideoDimensions,
        fps: FRAME_RATE,
        encryptionKey: String?,
        encryptionMode: String
    ) {
        val config = EncryptionConfig()
        if (!TextUtils.isEmpty(encryptionKey)) {
            config.encryptionKey = encryptionKey
            if (TextUtils.equals(encryptionMode, "AES-128-XTS")) {
                config.encryptionMode = EncryptionConfig.EncryptionMode.AES_128_XTS
            } else if (TextUtils.equals(encryptionMode, "AES-256-XTS")) {
                config.encryptionMode = EncryptionConfig.EncryptionMode.AES_256_XTS
            }
            rtcEngine().enableEncryption(true, config)
        } else {
            rtcEngine().enableEncryption(false, config)
        }
        Log.d("BaseActivity","configEngine $videoDimension $fps $encryptionMode")
        //log.debug("configEngine $videoDimension $fps $encryptionMode")
        // Set the Resolution, FPS. Bitrate and Orientation of the video
        rtcEngine().setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                videoDimension,
                fps,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    //companion object { private val log = LoggerFactory.getLogger(BaseActivity::class.java) }
}