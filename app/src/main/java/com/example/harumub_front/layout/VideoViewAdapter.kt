package com.example.harumub_front.layout

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.SurfaceView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.example.harumub_front.propeller.UserStatusData
import com.example.harumub_front.propeller.VideoInfoData
import android.view.ViewGroup
import com.example.harumub_front.R
import android.widget.FrameLayout
//import org.slf4j.LoggerFactory
import java.lang.NullPointerException
import java.util.ArrayList
import java.util.HashMap

abstract class VideoViewAdapter(activity: Activity, localUid: Int, uids: HashMap<Int?, SurfaceView?>? // Int?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val mInflater: LayoutInflater
    @JvmField
    protected val mContext: Context
    @JvmField
    protected val mUsers: ArrayList<UserStatusData>
    @JvmField
    protected var mLocalUid: Int
    @JvmField
    protected var mItemWidth = 0
    @JvmField
    protected var mItemHeight = 0

    private var mDefaultChildItem = 0

    private fun init(uids: HashMap<Int?, SurfaceView?>) {
        mUsers.clear()
        customizedInit(uids, true)
    }

    protected abstract fun customizedInit(uids: HashMap<Int?, SurfaceView?>?, force: Boolean)

    abstract fun notifyUiChanged(
        uids: HashMap<Int?, SurfaceView?>?,
        uidExtra: Int,
        status: HashMap<Int?, Int?>?,
        volume: HashMap<Int?, Int?>?
    )

    @JvmField
    protected var mVideoInfo // left user should removed from this HashMap
            : HashMap<Int?, VideoInfoData?>? = null

    fun addVideoInfo(uid: Int, video: VideoInfoData) {
        if (mVideoInfo == null) {
            mVideoInfo = HashMap()
        }
        mVideoInfo!![uid] = video
    }

    fun cleanVideoInfo() {
        mVideoInfo = null
    }

    fun setLocalUid(uid: Int) {
        mLocalUid = uid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = mInflater.inflate(R.layout.video_view_container, parent, false) as ViewGroup
        v.layoutParams.width = mItemWidth
        v.layoutParams.height = mItemHeight
        mDefaultChildItem = v.childCount
        return VideoUserStatusHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as VideoUserStatusHolder
        val user = mUsers[position]
        if (DEBUG) {
            Log.d("VideoViewAdapter", "onBindViewHolder " + position + " " + user + " " + myHolder + " " + myHolder.itemView + " " + mDefaultChildItem)
            //log.debug("onBindViewHolder " + position + " " + user + " " + myHolder + " " + myHolder.itemView + " " + mDefaultChildItem)
        }
        val holderView = myHolder.itemView as FrameLayout
        if (holderView.childCount == mDefaultChildItem) {
            val target = user.mView
            VideoViewAdapterUtil.stripView(target)
            holderView.addView(
                target,
                0,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
        VideoViewAdapterUtil.renderExtraData(mContext, user, myHolder)
    }

    override fun getItemCount(): Int {
        if (DEBUG) {
            Log.d("VideoViewAdapter", "getItemCount " + mUsers.size)
            //log.debug("getItemCount " + mUsers.size)
        }
        return mUsers.size
    }

    override fun getItemId(position: Int): Long {
        val user = mUsers[position]
        val view = user.mView
            ?: throw NullPointerException("SurfaceView destroyed for user " + user.mUid + " " + user.mStatus + " " + user.mVolume)
        return (user.mUid.toString() + System.identityHashCode(view)).hashCode().toLong()
    }

    companion object {
        //private val log = LoggerFactory.getLogger(VideoViewAdapter::class.java)
        const val DEBUG = false
    }

    init {
        mInflater = activity.layoutInflater
        mContext = activity.applicationContext
        mLocalUid = localUid
        mUsers = ArrayList()
        init(uids!!)
    }
}