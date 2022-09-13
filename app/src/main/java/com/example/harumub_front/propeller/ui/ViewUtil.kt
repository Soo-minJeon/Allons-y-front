package com.example.harumub_front.propeller.ui

import android.content.Context
import com.example.harumub_front.propeller.ui.ViewUtil
import android.view.MotionEvent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.example.harumub_front.propeller.VideoInfoData
import com.example.harumub_front.R
//import org.slf4j.LoggerFactory

object ViewUtil {
    internal const val DEBUG_ENABLED = false
    //private val log = LoggerFactory.getLogger(ViewUtil::class.java)
    private const val DEFAULT_TOUCH_TIMESTAMP = -1 // first time
    private const val TOUCH_COOL_DOWN_TIME = 500 // ms
    private var mLastTouchTime = DEFAULT_TOUCH_TIMESTAMP.toLong()

    /* package */
    @JvmStatic
    fun checkDoubleTouchEvent(event: MotionEvent, view: View): Boolean {
        if (DEBUG_ENABLED) {
            Log.d("ViewUtil", "dispatchTouchEvent " + mLastTouchTime + " " + event)
            //log.debug("dispatchTouchEvent " + mLastTouchTime + " " + event)
        }
        if (event.action == MotionEvent.ACTION_DOWN) { // only check touch down event
            if (mLastTouchTime == DEFAULT_TOUCH_TIMESTAMP.toLong() || SystemClock.elapsedRealtime() - mLastTouchTime >= TOUCH_COOL_DOWN_TIME) {
                mLastTouchTime = SystemClock.elapsedRealtime()
            } else {
                Log.w("ViewUtil","too many touch events " + view + " " + MotionEvent.ACTION_DOWN)
                    //log.warn("too many touch events " + view + " " + MotionEvent.ACTION_DOWN)
                return true
            }
        }
        return false
    }

    /* package */
    @JvmStatic
    fun checkDoubleKeyEvent(event: KeyEvent, view: View): Boolean {
        if (DEBUG_ENABLED) {
            Log.d("ViewUtil","dispatchKeyEvent " + mLastTouchTime + " " + event)
                //log.debug("dispatchKeyEvent " + mLastTouchTime + " " + event)
        }
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
            if (mLastTouchTime != DEFAULT_TOUCH_TIMESTAMP.toLong() && SystemClock.elapsedRealtime() - mLastTouchTime < TOUCH_COOL_DOWN_TIME) {
                Log.w("ViewUtil","too many key events " + view + " " + KeyEvent.ACTION_DOWN)
                    //log.warn("too many key events " + view + " " + KeyEvent.ACTION_DOWN)
                return true
            }
            mLastTouchTime = SystemClock.elapsedRealtime()
        }
        return false
    }

    fun setBackground(view: View, drawable: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
    }

    fun composeVideoInfoString(context: Context, videoMetaData: VideoInfoData): String {
        // so far do not show delay info
        return (videoMetaData.mWidth.toString() + "x" + videoMetaData.mHeight + ", "
                + context.getString(
            R.string.frame_rate_value_with_unit,
            videoMetaData.mFrameRate
        ) + ", "
                + context.getString(R.string.bit_rate_value_with_unit, videoMetaData.mBitRate))
    }
}