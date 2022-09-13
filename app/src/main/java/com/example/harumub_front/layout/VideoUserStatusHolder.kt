package com.example.harumub_front.layout

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.RelativeLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.example.harumub_front.R

class VideoUserStatusHolder(v: View) : RecyclerView.ViewHolder(v) {
    val mMaskView: RelativeLayout
    val mAvatar: ImageView
    val mIndicator: ImageView
    val mVideoInfo: LinearLayout
    val mMetaData: TextView

    init {
        mMaskView = v.findViewById<View>(R.id.user_control_mask) as RelativeLayout
        mAvatar = v.findViewById<View>(R.id.default_avatar) as ImageView
        mIndicator = v.findViewById<View>(R.id.indicator) as ImageView
        mVideoInfo = v.findViewById<View>(R.id.video_info_container) as LinearLayout
        mMetaData = v.findViewById<View>(R.id.video_info_metadata) as TextView
    }
}