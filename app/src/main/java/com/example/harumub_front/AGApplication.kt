package com.example.harumub_front

import android.app.Application
import com.example.harumub_front.model.CurrentUserSettings
import io.agora.rtc.RtcEngine
import com.example.harumub_front.model.EngineConfig
import com.example.harumub_front.model.MyEngineEventHandler
import com.example.harumub_front.model.AGEventHandler
import com.example.harumub_front.R
import android.text.TextUtils
import android.util.Log
import io.agora.rtc.Constants
//import org.slf4j.LoggerFactory
import java.lang.Exception
import java.lang.RuntimeException

class AGApplication : Application() {
//    private val mVideoSettings = CurrentUserSettings()
    //private val log = LoggerFactory.getLogger(this.javaClass)
    private var mRtcEngine: RtcEngine? = null
    private var mConfig: EngineConfig? = null
    private var mEventHandler: MyEngineEventHandler? = null

    fun rtcEngine(): RtcEngine? {
        return mRtcEngine
    }

    fun config(): EngineConfig? {
        return mConfig
    }

//    fun userSettings(): CurrentUserSettings {
//        return mVideoSettings
//    }

    fun addEventHandler(handler: AGEventHandler?) {
        mEventHandler!!.addEventHandler(handler!!)
    }

    fun remoteEventHandler(handler: AGEventHandler?) {
        mEventHandler!!.removeEventHandler(handler!!)
    }

    override fun onCreate() {
        super.onCreate()
        createRtcEngine()
    }

    private fun createRtcEngine() {
        val context = applicationContext
        val appId = context.getString(R.string.AGORA_APP_ID)
        if (TextUtils.isEmpty(appId)) {
            throw RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/")
        }
        mEventHandler = MyEngineEventHandler()
        mRtcEngine = try {
            // Creates an RtcEngine instance
            RtcEngine.create(context, appId, mEventHandler)
        } catch (e: Exception) {
            Log.getStackTraceString(e) //log.error(Log.getStackTraceString(e))
            throw RuntimeException(
                """
    NEED TO check rtc sdk init fatal error
    ${Log.getStackTraceString(e)}
    """.trimIndent()
            )
        }

        mRtcEngine!!.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
        mRtcEngine!!.enableVideo()
        mRtcEngine!!.enableAudioVolumeIndication(200, 3, false)
        mConfig = EngineConfig()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}