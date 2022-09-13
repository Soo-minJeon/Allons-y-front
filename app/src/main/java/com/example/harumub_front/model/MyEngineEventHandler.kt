package com.example.harumub_front.model

import android.util.Log
import io.agora.rtc.IRtcEngineEventHandler
import com.example.harumub_front.model.AGEventHandler
import com.example.harumub_front.model.DuringCallEventHandler
import io.agora.rtc.IRtcEngineEventHandler.RtcStats
import io.agora.rtc.IRtcEngineEventHandler.RemoteVideoStats
import io.agora.rtc.IRtcEngineEventHandler.AudioVolumeInfo
import com.example.harumub_front.model.BeforeCallEventHandler
import io.agora.rtc.IRtcEngineEventHandler.LastmileProbeResult
import io.agora.rtc.RtcEngine
import com.example.harumub_front.model.ConstantApp
//import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class MyEngineEventHandler : IRtcEngineEventHandler() {
    //private val log = LoggerFactory.getLogger(this.javaClass)
    private val mEventHandlerList = ConcurrentHashMap<AGEventHandler, Int>()
    fun addEventHandler(handler: AGEventHandler) {
        this.mEventHandlerList[handler] = 0
    }

    fun removeEventHandler(handler: AGEventHandler) {
        this.mEventHandlerList.remove(handler)
    }

    /**
     * @param uid User ID of the remote user sending the video streams.
     * @param width Width (pixels) of the video stream.
     * @param height Height (pixels) of the video stream.
     * @param elapsed Time elapsed (ms) from the local user calling the joinChannel method until this callback is triggered.
     */
    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        Log.d("MyEngineEventHandler", "onFirstRemoteVideoDecoded " + (uid and 0xFFFFFFFFL.toInt()) + " " + width + " " + height + " " + elapsed)
        //log.debug("onFirstRemoteVideoDecoded " + (uid and 0xFFFFFFFFL.toInt()) + " " + width + " " + height + " " + elapsed)
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
            }
        }
    }

    /**
     * @param width Width (pixels) of the first local video frame.
     * @param height Height (pixels) of the first local video frame.
     * @param elapsed Time elapsed (ms) from the local user calling joinChannel until this callback is triggered. If startPreview is called before joinChannel, elapsed is the time elapsed (ms) from the local user calling startPreview until this callback is triggered.
     */
    override fun onFirstLocalVideoFrame(width: Int, height: Int, elapsed: Int) {
        Log.d("MyEngineEventHandler", "onFirstLocalVideoFrame $width $height $elapsed")
            //log.debug("onFirstLocalVideoFrame $width $height $elapsed")
    }

    /**
     * @param uid ID of the user or host who joins the channel.
     * @param elapsed Time delay (ms) from the local user calling joinChannel/setClientRole until this callback is triggered.
     */
    override fun onUserJoined(uid: Int, elapsed: Int) {
        Log.d("MyEngineEventHandler", "onUserJoined " + (uid and 0xFFFFFFFFL.toInt()) + elapsed)
            //log.debug("onUserJoined " + (uid and 0xFFFFFFFFL.toInt()) + elapsed)
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onUserJoined(uid)
            }
        }
    }

    /**
     * @param uid ID of the user or host who leaves the channel or goes offline.
     * @param reason Reason why the user goes offline:
     *
     * USER_OFFLINE_QUIT(0): The user left the current channel.
     * USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
     * USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
     */
    override fun onUserOffline(uid: Int, reason: Int) {
        Log.d("MyEngineEventHandler", "onUserOffline " + (uid and 0xFFFFFFFFL.toInt()) + " " + reason)
            //log.debug("onUserOffline " + (uid and 0xFFFFFFFFL.toInt()) + " " + reason)

        // FIXME this callback may return times
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onUserOffline(uid, reason)
            }
        }
    }

    /**
     * @param uid ID of the remote user.
     * @param muted Whether the remote user's video stream playback pauses/resumes.
     */
    override fun onUserMuteVideo(uid: Int, muted: Boolean) {
        Log.d("MyEngineEventHandler", "onUserMuteVideo " + (uid and 0xFFFFFFFFL.toInt()) + " " + muted)
            //log.debug("onUserMuteVideo " + (uid and 0xFFFFFFFFL.toInt()) + " " + muted)
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED, uid, muted)
            }
        }
    }

    /**
     * Reports the statistics of the RtcEngine once every two seconds.
     * @param stats RTC engine statistics: RtcStats.
     */
    override fun onRtcStats(stats: RtcStats) {}

    /**
     * @param stats  of the received remote video streams: RemoteVideoStats.
     */
    override fun onRemoteVideoStats(stats: RemoteVideoStats) {
        Log.d("MyEngineEventHandler", "onRemoteVideoStats " + stats.uid + " " + stats.delay + " " + stats.receivedBitrate + " " + stats.rendererOutputFrameRate + " " + stats.width + " " + stats.height)
            //log.debug("onRemoteVideoStats " + stats.uid + " " + stats.delay + " " + stats.receivedBitrate + " " + stats.rendererOutputFrameRate + " " + stats.width + " " + stats.height)
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS, stats)
            }
        }
    }

    /**
     * @param speakerInfos An array containing the user ID and volume information for each speaker: AudioVolumeInfo.
     * @param totalVolume Total volume after audio mixing. The value ranges between 0 (lowest volume) and 255 (highest volume).
     */
    override fun onAudioVolumeIndication(speakerInfos: Array<AudioVolumeInfo>, totalVolume: Int) {
        if (speakerInfos == null) {
            // quick and dirty fix for crash
            // TODO should reset UI for no sound
            return
        }
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onExtraCallback(
                    AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS,
                    speakerInfos as Any
                )
            }
        }
    }

    /**
     * @param stats Statistics of the call: RtcStats
     */
    override fun onLeaveChannel(stats: RtcStats) {}

    /**
     * @param quality The last mile network quality based on the uplink and dowlink packet loss rate and jitter:
     *
     * QUALITY_UNKNOWN(0): The quality is unknown.
     * QUALITY_EXCELLENT(1): The quality is excellent.
     * QUALITY_GOOD(2): The quality is quite good, but the bitrate may be slightly lower than excellent.
     * QUALITY_POOR(3): Users can feel the communication slightly impaired.
     * QUALITY_BAD(4): Users can communicate not very smoothly.
     * QUALITY_VBAD(5): The quality is so bad that users can barely communicate.
     * QUALITY_DOWN(6): The network is disconnected and users cannot communicate at all.
     * QUALITY_DETECTING(8): The SDK is detecting the network quality.
     */
    override fun onLastmileQuality(quality: Int) {
        Log.d("MyEngineEventHandler","onLastmileQuality $quality")
            //log.debug("onLastmileQuality $quality")
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is BeforeCallEventHandler) {
                handler.onLastmileQuality(quality)
            }
        }
    }

    /**
     * @param result The uplink and downlink last-mile network probe test result. For details, see LastmileProbeResult.
     */
    override fun onLastmileProbeResult(result: LastmileProbeResult) {
        Log.d("MyEngineEventHandler","onLastmileProbeResult $result")
            //log.debug("onLastmileProbeResult $result")
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is BeforeCallEventHandler) {
                handler.onLastmileProbeResult(result)
            }
        }
    }

    /**
     * @param error Error Code
     */
    override fun onError(error: Int) {
        Log.d("MyEngineEventHandler","onError " + error + " " + RtcEngine.getErrorDescription(error))
            //log.debug("onError " + error + " " + RtcEngine.getErrorDescription(error))
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onExtraCallback(
                    AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR,
                    error,
                    RtcEngine.getErrorDescription(error)
                )
            }
        }
    }

    /**
     * @param uid User ID of the remote user sending the data stream.
     * @param streamId Stream ID.
     * @param data Data received by the local user.
     */
    override fun onStreamMessage(uid: Int, streamId: Int, data: ByteArray) {
        Log.d("MyEngineEventHandler",
            //log.debug(
            "onStreamMessage " + (uid and 0xFFFFFFFFL.toInt()) + " " + streamId + " " + Arrays.toString(
                data
            )
        )
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG, uid, data)
            }
        }
    }

    /**
     * @param uid User ID of the remote user sending the data stream.
     * @param streamId Stream ID.
     * @param error Error Code.
     * @param missed The number of lost messages.
     * @param cached The number of incoming cached messages when the data stream is interrupted.
     */
    override fun onStreamMessageError(
        uid: Int,
        streamId: Int,
        error: Int,
        missed: Int,
        cached: Int
    ) {
        Log.d("MyEngineEventHandler","onStreamMessageError " + (uid and 0xFFFFFFFFL.toInt()) + " " + streamId + " " + error + " " + missed + " " + cached)
            //log.warn("onStreamMessageError " + (uid and 0xFFFFFFFFL.toInt()) + " " + streamId + " " + error + " " + missed + " " + cached)
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onExtraCallback(
                    AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR,
                    error,
                    "on stream msg error " + (uid and 0xFFFFFFFFL.toInt()) + " " + missed + " " + cached
                )
            }
        }
    }

    /**
     * Occurs when the SDK cannot reconnect to Agora's edge server 10 seconds after its connection to the server is interrupted.
     * The SDK triggers this callback when it cannot connect to the server 10 seconds after calling joinChannel(), regardless of whether it is in the channel or not.
     * If the SDK fails to rejoin the channel 20 minutes after being disconnected from Agora's edge server, the SDK stops rejoining the channel.
     */
    override fun onConnectionLost() {
        Log.d("MyEngineEventHandler","onConnectionLost")
            //log.debug("onConnectionLost")
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onExtraCallback(
                    AGEventHandler.EVENT_TYPE_ON_APP_ERROR,
                    ConstantApp.AppError.NO_CONNECTION_ERROR
                )
            }
        }
    }

    /**
     * @param channel Channel name.
     * @param uid User ID.
     * @param elapsed Time elapsed (ms) from the user calling joinChannel until this callback is triggered.
     */
    override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
        Log.d("MyEngineEventHandler","onJoinChannelSuccess " + channel + " " + (uid and 0xFFFFFFFFL.toInt()) + "(" + uid + ") " + elapsed)
            //log.debug("onJoinChannelSuccess " + channel + " " + (uid and 0xFFFFFFFFL.toInt()) + "(" + uid + ") " + elapsed)
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onJoinChannelSuccess(channel, uid, elapsed)
            }
        }
    }

    /**
     * @param routing The definition of the routing is listed as follows:
     *
     * AUDIO_ROUTE_DEFAULT(-1): Default audio route.
     * AUDIO_ROUTE_HEADSET(0): Headset.
     * AUDIO_ROUTE_EARPIECE(1): Earpiece.
     * AUDIO_ROUTE_HEADSETNOMIC(2): Headset with no microphone.
     * AUDIO_ROUTE_SPEAKERPHONE(3): Speakerphone.
     * AUDIO_ROUTE_LOUDSPEAKER(4): Loudspeaker.
     * AUDIO_ROUTE_HEADSETBLUETOOTH(5): Bluetooth headset.
     */
    override fun onAudioRouteChanged(routing: Int) {
        Log.d("MyEngineEventHandler","onAudioRouteChanged $routing")
            //log.debug("onAudioRouteChanged $routing")
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED, routing)
            }
        }
    }

    /**
     * @param warn Warning Code
     */
    override fun onWarning(warn: Int) {
        Log.d("MyEngineEventHandler","onWarning $warn")
            //log.debug("onWarning $warn")
        val msg = "Check io.agora.rtc.Constants for details"
        val it: Iterator<AGEventHandler> = mEventHandlerList.keys.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler is DuringCallEventHandler) {
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR, warn, msg)
            }
        }
    }

    /**
     * @param state The state code:
     *
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY(710): the audio mixing file is playing.
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_PAUSED(711): the audio mixing file pauses playing.
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_STOPPED(713): the audio mixing file stops playing.
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_ERROR(714): an exception occurs when playing the audio mixing file. See the errorCode for details.
     *
     * @param errorCode The error code:
     *
     * MEDIA_ENGINE_AUDIO_ERROR_MIXING_OPEN(701): the SDK cannot open the audio mixing file.
     * MEDIA_ENGINE_AUDIO_ERROR_MIXING_TOO_FREQUENT(702): the SDK opens the audio mixing file too frequently.
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_INTERRUPTED_EOF(703): the audio mixing file playback is interrupted.
     */
    override fun onAudioMixingStateChanged(state: Int, errorCode: Int) {
        Log.d("MyEngineEventHandler","onAudioMixingStateChanged() state = [$state], errorCode = [$errorCode]")
            //log.debug("onAudioMixingStateChanged() state = [$state], errorCode = [$errorCode]")
    }
}