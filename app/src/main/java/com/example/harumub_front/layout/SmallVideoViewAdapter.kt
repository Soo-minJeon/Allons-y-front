package com.example.harumub_front.layout

import com.example.harumub_front.layout.VideoViewAdapterUtil.composeDataItem
import android.app.Activity
import android.content.Context
import android.view.SurfaceView
import com.example.harumub_front.layout.VideoViewAdapter
import com.example.harumub_front.layout.VideoViewAdapterUtil
import android.view.WindowManager
import android.util.DisplayMetrics
import android.util.Log
import com.example.harumub_front.layout.SmallVideoViewAdapter
//import org.slf4j.LoggerFactory
import java.util.HashMap

class SmallVideoViewAdapter(
    activity: Activity?,
    localUid: Int,
    var exceptedUid: Int,
    uids: HashMap<Int?, SurfaceView?>?
) : VideoViewAdapter(
    activity!!, localUid, uids
) {
    override fun customizedInit(uids: HashMap<Int?, SurfaceView?>?, force: Boolean) {
        composeDataItem(mUsers, uids, mLocalUid, null, null, mVideoInfo, exceptedUid)
        if (force || mItemWidth == 0 || mItemHeight == 0) {
            val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(outMetrics)
            mItemWidth = outMetrics.widthPixels / 4
            mItemHeight = outMetrics.heightPixels / 4
        }
    }

    override fun notifyUiChanged(
        uids: HashMap<Int?, SurfaceView?>?,
        uidExcepted: Int,
        status: HashMap<Int?, Int?>?,
        volume: HashMap<Int?, Int?>?
    ) {
        mUsers.clear()
        exceptedUid = uidExcepted
        Log.d("SmallVideoViewAdapter", "notifyUiChanged " + (mLocalUid and 0xFFFFFFFFL.toInt()) + " " + (uidExcepted and 0xFFFFFFFFL.toInt()) + " " + uids + " " + status + " " + volume)
        //log.debug("notifyUiChanged " + (mLocalUid and 0xFFFFFFFFL.toInt()) + " " + (uidExcepted and 0xFFFFFFFFL.toInt()) + " " + uids + " " + status + " " + volume)
        composeDataItem(mUsers, uids, mLocalUid, status, volume, mVideoInfo, uidExcepted)
        notifyDataSetChanged()
    }

    //companion object { private val log = LoggerFactory.getLogger(SmallVideoViewAdapter::class.java) }

    init {
        Log.d("SmallVideoViewAdapter", (mLocalUid and 0xFFFFFFFFL.toInt()).toString() + " " + (exceptedUid and 0xFFFFFFFFL.toInt()))
        //log.debug("SmallVideoViewAdapter " + (mLocalUid and 0xFFFFFFFFL.toInt()) + " " + (exceptedUid and 0xFFFFFFFFL.toInt()))
    }
}