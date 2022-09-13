package com.example.harumub_front.propeller

import kotlin.jvm.JvmOverloads
import android.view.SurfaceView
import com.example.harumub_front.propeller.VideoInfoData
import com.example.harumub_front.propeller.UserStatusData

class UserStatusData constructor( // 인자 생성자
    var mUid: Int, var mView: SurfaceView, // if status is null, do nothing
    var mStatus: Int, var mVolume: Int, var videoInfoData: VideoInfoData? = null) {

    fun setVideoInfo(video: VideoInfoData?) {
        videoInfoData = video
    }

    override fun toString(): String {
        return "UserStatusData{" +
                "mUid=" + (mUid and 0XFFFFFFFFL.toInt()) +
                ", mView=" + mView +
                ", mStatus=" + mStatus +
                ", mVolume=" + mVolume +
                '}'
    }

    companion object { // static final
        const val DEFAULT_STATUS = 0
        const val VIDEO_MUTED = 1
        const val AUDIO_MUTED = VIDEO_MUTED shl 1
        const val DEFAULT_VOLUME = 0
    }
}