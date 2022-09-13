package com.example.harumub_front.model

class CurrentUserSettings {
    @JvmField
    var mEncryptionModeIndex = 0
    @JvmField
    var mEncryptionKey: String? = null
    @JvmField
    var mChannelName: String? = null

    fun reset() {}

    // 기본생성자
    init { reset() }
    //fun CurrentUserSettings() { reset() }
}