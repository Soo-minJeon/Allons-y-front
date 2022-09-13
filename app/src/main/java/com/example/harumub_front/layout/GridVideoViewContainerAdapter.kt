package com.example.harumub_front.layout

import android.app.Activity
import android.content.Context
import android.view.SurfaceView
import android.view.WindowManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harumub_front.propeller.UserStatusData
//import org.slf4j.LoggerFactory
import java.lang.NullPointerException
import java.util.HashMap

class GridVideoViewContainerAdapter(activity: Activity, localUid: Int, uids: HashMap<Int?, SurfaceView?>?)
                                // (activity: Activity?, localUid: Int, uids: HashMap<Int?, SurfaceView?>?)
    : VideoViewAdapter(activity, localUid, uids) {
    //override fun customizedInit(uids: HashMap<Int?, SurfaceView?>?, force: Boolean) {}
    public override fun customizedInit(uids: HashMap<Int?, SurfaceView?>?, force: Boolean) {
        VideoViewAdapterUtil.composeDataItem1(mUsers, uids, mLocalUid) // local uid

        if (force || mItemWidth == 0 || mItemHeight == 0) {
            val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(outMetrics)
            val count = uids!!.size
            var DividerX = 1
            var DividerY = 1
            if (count == 2) {
                DividerY = 2
            } else if (count >= 3) {
                DividerX = getNearestSqrt(count)
                DividerY = Math.ceil((count * 1f / DividerX).toDouble()).toInt()
            }
            val width = outMetrics.widthPixels
            val height = outMetrics.heightPixels
            if (width > height) {
                mItemWidth = width / DividerY
                mItemHeight = height / DividerX
            } else {
                mItemWidth = width / DividerX
                mItemHeight = height / DividerY
            }
        }
    }

    private fun getNearestSqrt(n: Int): Int {
        return Math.sqrt(n.toDouble()).toInt()
    }

    override fun notifyUiChanged(
        uids: HashMap<Int?, SurfaceView?>?,
        localUid: Int,
        status: HashMap<Int?, Int?>?,
        volume: HashMap<Int?, Int?>?
    ) {
        setLocalUid(localUid)
        VideoViewAdapterUtil.composeDataItem(mUsers, uids, localUid, status, volume, mVideoInfo)
        notifyDataSetChanged()
        if (DEBUG) {
            Log.d("GridVVContainerAdapter", "notifyUiChanged "+(mLocalUid and 0xFFFFFFFFL.toInt())+ " " + (localUid and 0xFFFFFFFFL.toInt()) + " " + uids + " " + status + " " + volume)
            //log.debug("notifyUiChanged " + (mLocalUid and 0xFFFFFFFFL.toInt()) + " " + (localUid and 0xFFFFFFFFL.toInt()) + " " + uids + " " + status + " " + volume)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    fun getItem(position: Int): UserStatusData {
        return mUsers[position]
    }

    override fun getItemId(position: Int): Long {
        val user = mUsers[position]
        val view = user.mView
        if (view == null) { //?:
            throw NullPointerException("SurfaceView destroyed for user " + (user.mUid and 0xFFFFFFFFL.toInt()) + " " + user.mStatus + " " + user.mVolume)
        }
        return (user.mUid.toString() + System.identityHashCode(view)).hashCode().toLong()
    }

    //companion object { private val log = LoggerFactory.getLogger(GridVideoViewContainerAdapter::class.java) }

    init {
        Log.d("GridVVContainerAdapter", (mLocalUid and 0xFFFFFFFFL.toInt()).toString())
        //log.debug("GridVideoViewContainerAdapter " + (mLocalUid and 0xFFFFFFFFL.toInt()))
    }
}