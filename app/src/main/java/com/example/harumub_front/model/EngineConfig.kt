package com.example.harumub_front.model

class EngineConfig {
    //@JvmField
    var mUid = 0
    //var mUid: Int? = null
    //@JvmField
    var mChannel: String? = null
    fun reset() {
        mChannel = null
    }
    fun EngineConfig() {
    }
}