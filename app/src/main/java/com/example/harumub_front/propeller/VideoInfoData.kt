package com.example.harumub_front.propeller

import kotlin.jvm.JvmOverloads

class VideoInfoData constructor(
    var mWidth: Int,
    var mHeight: Int,
    var mDelay: Int,
    var mFrameRate: Int,
    var mBitRate: Int,
    var mCodec: Int = 0 ) {

    override fun toString(): String {
        return "VideoInfoData{" +
                "mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                ", mDelay=" + mDelay +
                ", mFrameRate=" + mFrameRate +
                ", mBitRate=" + mBitRate +
                ", mCodec=" + mCodec +
                '}'
    }
}