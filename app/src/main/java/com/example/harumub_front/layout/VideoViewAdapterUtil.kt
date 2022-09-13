package com.example.harumub_front.layout

import android.content.Context
import android.graphics.Color
import android.util.Log
//import com.example.harumub_front.propeller.UserStatusData.setVideoInfo
//import com.example.harumub_front.propeller.UserStatusData.videoInfoData
import com.example.harumub_front.propeller.UserStatusData
import android.view.SurfaceView
import android.view.View
import com.example.harumub_front.propeller.VideoInfoData
import kotlin.jvm.JvmOverloads
import com.example.harumub_front.R
import com.example.harumub_front.propeller.ui.ViewUtil
import android.widget.FrameLayout
import com.example.harumub_front.propeller.Constant
//import org.slf4j.LoggerFactory
import java.util.ArrayList
import java.util.HashMap

object VideoViewAdapterUtil {
    //private val log = LoggerFactory.getLogger(VideoViewAdapterUtil::class.java)
    private const val DEBUG = false

    fun composeDataItem1(users: ArrayList<UserStatusData>, uids: HashMap<Int?, SurfaceView?>?, localUid: Int) {
        for (entry in uids!!.entries) {
            if (DEBUG) {
                Log.d("VideoViewAdapterUtil",
                    "composeDataItem1 " + (entry.key?.and(0xFFFFFFFFL.toInt())) + " "
                            + (localUid and 0xFFFFFFFFL.toInt()) + " " + users.size + " " + entry.value
                )
                //log.debug("composeDataItem1 " + (entry.key?.and(0xFFFFFFFFL.toInt())) + " " + (localUid and 0xFFFFFFFFL.toInt()) + " " + users.size + " " + entry.value)
            }
            val surfaceV = entry.value
            surfaceV!!.setZOrderOnTop(false)
            surfaceV.setZOrderMediaOverlay(false)
            searchUidsAndAppend(
                users,
                entry,
                localUid,
                UserStatusData.DEFAULT_STATUS,
                UserStatusData.DEFAULT_VOLUME,
                null
            )
        }
        removeNotExisted(users, uids, localUid)
    }

    private fun removeNotExisted(users: ArrayList<UserStatusData>, uids: HashMap<Int?, SurfaceView?>?, localUid: Int) {
        if (DEBUG) {
            Log.d("VideoViewAdapterUtil", "removeNotExisted all " + uids + " " + users.size)
            //log.debug("removeNotExisted all " + uids + " " + users.size)
        }
        val it = users.iterator()
        while (it.hasNext()) {
            val user = it.next()
            if (DEBUG) {
                Log.d("VideoViewAdapterUtil","removeNotExisted $user $localUid")
                    //log.debug("removeNotExisted $user $localUid")
            }
            if (uids?.get(user.mUid) == null && user.mUid != localUid) {
                it.remove()
            }
        }
    }

    private fun searchUidsAndAppend(
        users: ArrayList<UserStatusData>, entry: MutableMap.MutableEntry<Int?, SurfaceView?>, //Map.Entry<Int, SurfaceView?>,
        localUid: Int, status: Int?, volume: Int, i: VideoInfoData?
    ) {
        if (entry.key == 0 || entry.key == localUid) {
            var found = false
            for (user in users) {
                if (user.mUid == entry.key && user.mUid == 0 || user.mUid == localUid) { // first time
                    user.mUid = localUid
                    if (status != null) {
                        user.mStatus = status
                    }
                    user.mVolume = volume
                    user.setVideoInfo(i)
                    found = true
                    break
                }
            }
            if (!found) {
                users.add(0, UserStatusData(localUid, entry.value!!, status!!, volume, i))
            }
        } else {
            var found = false
            for (user in users) {
                if (user.mUid == entry.key) {
                    if (status != null) {
                        user.mStatus = status
                    }
                    user.mVolume = volume
                    user.setVideoInfo(i)
                    found = true
                    break
                }
            }
            if (!found) {
                users.add(UserStatusData(entry.key!!, entry.value!!, status!!, volume, i))
            }
        }
    }

    @JvmStatic
    @JvmOverloads
    fun composeDataItem(
        users: ArrayList<UserStatusData>, uids: HashMap<Int?, SurfaceView?>?,
        localUid: Int,
        status: HashMap<Int?, Int?>?,
        volume: HashMap<Int?, Int?>?,
        video: HashMap<Int?, VideoInfoData?>?, uidExcepted: Int = 0
    ) {
        for (entry in uids!!.entries) {
            val uid = entry.key
            if (uid == uidExcepted && uidExcepted != 0) {
                continue
            }
            val local = uid == 0 || uid == localUid
            var s: Int? = null
            if (status != null) {
                s = status[uid]
                if (local && s == null) { // check again
                    s = status[if (uid == 0) localUid else 0]
                }
            }
            var v: Int? = null
            if (volume != null) {
                v = volume[uid]
                if (local && v == null) { // check again
                    v = volume[if (uid == 0) localUid else 0]
                }
            }
            if (v == null) {
                v = UserStatusData.DEFAULT_VOLUME
            }
            var i: VideoInfoData?
            if (video != null) {
                i = video[uid]
                if (local && i == null) { // check again
                    i = video[if (uid == 0) localUid else 0]
                }
            } else {
                i = null
            }
            if (DEBUG) {
                Log.d("VideoViewAdapterUtil",
                    "composeDataItem " + users + " " + entry + " "
                        + (localUid and 0XFFFFFFFFL.toInt()) + " "
                        + s + " " + v + " " + i + " " + local + " "
                        + (uid!! and 0XFFFFFFFFL.toInt()) + " "
                        + (uidExcepted and 0XFFFFFFFFL.toInt())
                )
                /* log.debug("composeDataItem " + users + " " + entry + " "
                        + (localUid and 0XFFFFFFFFL.toInt()) + " "
                        + s + " " + v + " " + i + " " + local + " "
                        + (uid!! and 0XFFFFFFFFL.toInt()) + " "
                        + (uidExcepted and 0XFFFFFFFFL.toInt()))
                 */
            }
            searchUidsAndAppend(users, entry, localUid, s, v, i)
        }
        removeNotExisted(users, uids, localUid)
    }

    fun renderExtraData(context: Context, user: UserStatusData, myHolder: VideoUserStatusHolder) {
        if (DEBUG) {
            Log.d("VideoViewAdapterUtil", "renderExtraData $user $myHolder")
                //log.debug("renderExtraData $user $myHolder")
        }
        if (user.mStatus != null) {
            if (user.mStatus and UserStatusData.VIDEO_MUTED != 0) {
                myHolder.mAvatar.visibility = View.VISIBLE
                myHolder.mMaskView.setBackgroundColor(context.resources.getColor(android.R.color.darker_gray))
            } else {
                myHolder.mAvatar.visibility = View.GONE
                myHolder.mMaskView.setBackgroundColor(Color.TRANSPARENT)
            }
            if (user.mStatus and UserStatusData.AUDIO_MUTED != 0) {
                myHolder.mIndicator.setImageResource(R.drawable.icon_muted)
                myHolder.mIndicator.visibility = View.VISIBLE
                myHolder.mIndicator.tag = System.currentTimeMillis()
                return
            } else {
                myHolder.mIndicator.tag = null
                myHolder.mIndicator.visibility = View.INVISIBLE
            }
        }
        val tag = myHolder.mIndicator.tag as? Long
        if (tag != null && System.currentTimeMillis() - tag < 1500) { // workaround for audio volume comes just later than mute
            return
        }
        val volume = user.mVolume
        if (volume > 0) {
            myHolder.mIndicator.setImageResource(R.drawable.icon_speaker)
            myHolder.mIndicator.visibility = View.VISIBLE
        } else {
            myHolder.mIndicator.visibility = View.INVISIBLE
        }
        if (Constant.SHOW_VIDEO_INFO && user.videoInfoData != null) {
            val videoInfo = user.videoInfoData
            myHolder.mMetaData.text = ViewUtil.composeVideoInfoString(context, videoInfo!!)
            myHolder.mVideoInfo.visibility = View.VISIBLE
        } else {
            myHolder.mVideoInfo.visibility = View.GONE
        }
    }

    fun stripView(view: SurfaceView) {
        val parent = view.parent
        if (parent != null) {
            (parent as FrameLayout).removeView(view)
        }
    }
}