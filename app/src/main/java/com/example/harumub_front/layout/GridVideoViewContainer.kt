package com.example.harumub_front.layout

import androidx.recyclerview.widget.RecyclerView
import com.example.harumub_front.propeller.ui.RecyclerItemClickListener
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.harumub_front.propeller.VideoInfoData
import com.example.harumub_front.propeller.UserStatusData
//import org.slf4j.LoggerFactory
import java.util.HashMap

class GridVideoViewContainer : RecyclerView {
    private var mGridVideoViewContainerAdapter: GridVideoViewContainerAdapter? = null

    constructor(context: Context?)
            : super(context!!) {}

    constructor(context: Context?, attrs: AttributeSet?)
            : super(context!!, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int)
            : super(context!!, attrs, defStyle) {}

    fun setItemEventHandler(listener: RecyclerItemClickListener.OnItemClickListener?) {
        //addOnItemTouchListener(RecyclerItemClickListener(context, listener))
        this.addOnItemTouchListener(RecyclerItemClickListener(context, listener))
    }

    private fun initAdapter(activity: Activity, localUid: Int, uids: HashMap<Int?, SurfaceView?>?
    ): Boolean {
        if (mGridVideoViewContainerAdapter == null) {
            mGridVideoViewContainerAdapter = GridVideoViewContainerAdapter(activity, localUid, uids)
            mGridVideoViewContainerAdapter!!.setHasStableIds(true)
            return true
        }
        return false
    }

    fun initViewContainer(activity: Activity, localUid: Int, uids: HashMap<Int?, SurfaceView?>?, isLandscape: Boolean) {
        val newCreated = initAdapter(activity, localUid, uids) // Boolean

        if (!newCreated) {
            mGridVideoViewContainerAdapter!!.setLocalUid(localUid)
            mGridVideoViewContainerAdapter!!.customizedInit(uids, true)
        }
        this.adapter = mGridVideoViewContainerAdapter
        val orientation = if (isLandscape) HORIZONTAL else VERTICAL
        val count = uids!!.size
        if (count <= 2) { // only local full view or or with one peer
            this.layoutManager =
                LinearLayoutManager(activity.applicationContext, orientation, false)
        }
        else if (count > 2) {
            val itemSpanCount = getNearestSqrt(count)
            this.layoutManager =
                GridLayoutManager(activity.applicationContext, itemSpanCount, orientation, false)
        }
        mGridVideoViewContainerAdapter!!.notifyDataSetChanged()
    }

    private fun getNearestSqrt(n: Int): Int {
        return Math.sqrt(n.toDouble()).toInt()
    }

    fun notifyUiChanged(
        uids: HashMap<Int?, SurfaceView?>?,
        localUid: Int,
        status: HashMap<Int?, Int?>?,
        volume: HashMap<Int?, Int?>?
    ) {
        if (mGridVideoViewContainerAdapter == null) {
            return
        }
        mGridVideoViewContainerAdapter!!.notifyUiChanged(uids, localUid, status, volume)
    }

    fun addVideoInfo(uid: Int, video: VideoInfoData?) {
        if (mGridVideoViewContainerAdapter == null) {
            return
        }
        mGridVideoViewContainerAdapter!!.addVideoInfo(uid, video!!)
    }

    fun cleanVideoInfo() {
        if (mGridVideoViewContainerAdapter == null) {
            return
        }
        mGridVideoViewContainerAdapter!!.cleanVideoInfo()
    }

    fun getItem(position: Int): UserStatusData {
        return mGridVideoViewContainerAdapter!!.getItem(position)
    }

    //companion object { private val log = LoggerFactory.getLogger(GridVideoViewContainer::class.java) }
}