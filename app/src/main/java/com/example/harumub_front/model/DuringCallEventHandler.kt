package com.example.harumub_front.model

import com.example.harumub_front.model.AGEventHandler

interface DuringCallEventHandler : AGEventHandler {
    fun onUserJoined(uid: Int) {}

    fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {}

    fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {}

    fun onUserOffline(uid: Int, reason: Int) {}

    fun onExtraCallback(type: Int, vararg data: Any) {} // Object... data
}