package com.example.harumub_front.propeller

import io.agora.rtc.video.BeautyOptions
import io.agora.rtc.RtcEngine

object Constant {
    var MEDIA_SDK_VERSION: String

    //companion object { //static
    init {
        var sdk = "undefined"
        try {
            //io.agora.propeller.sdk = RtcEngine.getSdkVersion()
            sdk = RtcEngine.getSdkVersion()

        } catch (e: Throwable) {
        }
        //MEDIA_SDK_VERSION = io.agora.propeller.sdk
        MEDIA_SDK_VERSION = sdk
    }

    const val MIX_FILE_PATH = "/assets/qt.mp3" // in assets folder

    @JvmField
    var SHOW_VIDEO_INFO = true
    @JvmField
    var DEBUG_INFO_ENABLED = true // Show debug/log info on screen
    @JvmField
    var BEAUTY_EFFECT_ENABLED = true // Built-in face beautification

    const val BEAUTY_EFFECT_DEFAULT_CONTRAST = 1
    const val BEAUTY_EFFECT_DEFAULT_LIGHTNESS = .7f
    const val BEAUTY_EFFECT_DEFAULT_SMOOTHNESS = .5f
    const val BEAUTY_EFFECT_DEFAULT_REDNESS = .1f
    const val BEAUTY_EFFECT_DEFAULT_SHARPNESS = .3f // 3.7.1 추가

    val BEAUTY_OPTIONS = BeautyOptions(
        BEAUTY_EFFECT_DEFAULT_CONTRAST,
        BEAUTY_EFFECT_DEFAULT_LIGHTNESS,
        BEAUTY_EFFECT_DEFAULT_SMOOTHNESS,
        BEAUTY_EFFECT_DEFAULT_REDNESS,
        BEAUTY_EFFECT_DEFAULT_SHARPNESS // 3.7.1 추가
    )
    const val BEAUTY_EFFECT_MAX_LIGHTNESS = 1.0f
    const val BEAUTY_EFFECT_MAX_SMOOTHNESS = 1.0f
    const val BEAUTY_EFFECT_MAX_REDNESS = 1.0f
    const val BEAUTY_EFFECT_MAX_SHARPNESS = 1.0f // 3.7.1 추가
}