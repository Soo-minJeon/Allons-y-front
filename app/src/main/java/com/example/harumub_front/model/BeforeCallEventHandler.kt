package com.example.harumub_front.model

import com.example.harumub_front.model.AGEventHandler
import io.agora.rtc.IRtcEngineEventHandler.LastmileProbeResult

interface BeforeCallEventHandler : AGEventHandler {
    fun onLastmileQuality(quality: Int)
    fun onLastmileProbeResult(result: LastmileProbeResult?)
}