package com.haosen.floating.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.haosen.floating.manager.MagnetViewListener
import com.haosen.floating.utils.SystemUtils.getStatusBarHeight
import kotlin.math.max
import kotlin.math.min

/**
 * FileName: FloatingMagnetView
 * Author: haosen
 * Date: 2024/11/16 20:50
 * Description:
 */
open class FloatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr
) {
    private var mOriginalRawX = 0f
    private var mOriginalRawY = 0f
    private var mOriginalX = 0f
    private var mOriginalY = 0f
    private var mMagnetViewListener: MagnetViewListener? = null
    private var mLastTouchDownTime: Long = 0
    protected var mMoveAnimator: MoveAnimator? = null
    protected var mScreenWidth: Int = 0
    private var mScreenHeight = 0
    private var mStatusBarHeight = 0
    private var isNearestLeft = true
    private var mPortraitY = 0f

    fun setMagnetViewListener(magnetViewListener: MagnetViewListener?) {
        this.mMagnetViewListener = magnetViewListener
    }

    init {
        init()
    }

    private fun init() {
        mMoveAnimator = MoveAnimator()
        mStatusBarHeight = getStatusBarHeight(context)
        isClickable = true
        //        updateSize();
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                changeOriginalTouchParams(event)
                updateSize()
                mMoveAnimator!!.stop()
            }

            MotionEvent.ACTION_MOVE -> updateViewPosition(event)
            MotionEvent.ACTION_UP -> {
                clearPortraitY()
                moveToEdge()
                if (isOnClickEvent) {
                    dealClickEvent()
                }
            }
        }
        return true
    }

    protected fun dealClickEvent() {
        mMagnetViewListener?.onClick(this)
    }

    protected val isOnClickEvent: Boolean
        get() = System.currentTimeMillis() - mLastTouchDownTime < TOUCH_TIME_THRESHOLD

    private fun updateViewPosition(event: MotionEvent) {
        x = mOriginalX + event.rawX - mOriginalRawX
        // 限制不可超出屏幕高度
        var desY = mOriginalY + event.rawY - mOriginalRawY
        if (desY < mStatusBarHeight) {
            desY = mStatusBarHeight.toFloat()
        }
        if (desY > mScreenHeight - height) {
            desY = (mScreenHeight - height).toFloat()
        }
        y = desY
    }

    private fun changeOriginalTouchParams(event: MotionEvent) {
        mOriginalX = x
        mOriginalY = y
        mOriginalRawX = event.rawX
        mOriginalRawY = event.rawY
        mLastTouchDownTime = System.currentTimeMillis()
    }

    protected fun updateSize() {
        val viewGroup = parent as ViewGroup
        mScreenWidth = viewGroup.width - width
        mScreenHeight = viewGroup.height
        //        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
//        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    @JvmOverloads
    fun moveToEdge(isLeft: Boolean = isNearestLeft(), isLandscape: Boolean = false) {
        val moveDistance = (if (isLeft) MARGIN_EDGE else mScreenWidth - MARGIN_EDGE).toFloat()
        var y = y
        if (!isLandscape && mPortraitY != 0f) {
            y = mPortraitY
            clearPortraitY()
        }
        mMoveAnimator!!.start(
            moveDistance, min(max(0.0, y.toDouble()), (mScreenHeight - height).toDouble())
                .toFloat()
        )
    }

    private fun clearPortraitY() {
        mPortraitY = 0f
    }

    protected fun isNearestLeft(): Boolean {
        val middle = mScreenWidth / 2
        isNearestLeft = x < middle
        return isNearestLeft
    }

    fun onRemove() {
        mMagnetViewListener?.onRemove(this)
    }

    protected inner class MoveAnimator : Runnable {
        private val handler = Handler(Looper.getMainLooper())
        private var destinationX = 0f
        private var destinationY = 0f
        private var startingTime: Long = 0

        fun start(x: Float, y: Float) {
            this.destinationX = x
            this.destinationY = y
            startingTime = System.currentTimeMillis()
            handler.post(this)
        }

        override fun run() {
            if (rootView == null || rootView.parent == null) {
                return
            }
            val progress = min(
                1.0,
                ((System.currentTimeMillis() - startingTime) / 400f).toDouble()
            ).toFloat()
            val deltaX = (destinationX - x) * progress
            val deltaY = (destinationY - y) * progress
            move(deltaX, deltaY)
            if (progress < 1) {
                handler.post(this)
            }
        }

        fun stop() {
            handler.removeCallbacks(this)
        }
    }

    private fun move(deltaX: Float, deltaY: Float) {
        x += deltaX
        y += deltaY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (parent != null) {
            val isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
            markPortraitY(isLandscape)
            (parent as ViewGroup).post {
                updateSize()
                moveToEdge(isNearestLeft, isLandscape)
            }
        }
    }

    private fun markPortraitY(isLandscape: Boolean) {
        if (isLandscape) {
            mPortraitY = y
        }
    }

    companion object {
        const val MARGIN_EDGE: Int = 13
        private const val TOUCH_TIME_THRESHOLD = 150
    }
}
