package com.example.harumub_front.model

interface AGEventHandler {
    companion object {
        const val EVENT_TYPE_ON_DATA_CHANNEL_MSG = 3
        const val EVENT_TYPE_ON_USER_VIDEO_MUTED = 6
        const val EVENT_TYPE_ON_USER_AUDIO_MUTED = 7
        const val EVENT_TYPE_ON_SPEAKER_STATS = 8
        const val EVENT_TYPE_ON_AGORA_MEDIA_ERROR = 9
        const val EVENT_TYPE_ON_USER_VIDEO_STATS = 10
        const val EVENT_TYPE_ON_APP_ERROR = 13
        const val EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED = 18
    }
}