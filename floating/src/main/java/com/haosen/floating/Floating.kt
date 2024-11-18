package com.haosen.floating

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.haosen.floating.utils.LifecycleUtils
import java.lang.ref.WeakReference
import kotlin.concurrent.Volatile

/**
 * FileName: FloatingView
 * Author: haosen
 * Date: 2024/11/17 20:50
 * Description:
 */
class Floating private constructor() : IFloatingView {
    private var mEnFloatingView: FloatingView? = null
    private var mContainer: WeakReference<FrameLayout>? = null

    @LayoutRes
    private var mLayoutId = R.layout.en_floating_view

    @DrawableRes
    private var mIconRes = R.drawable.imuxuan
    private var mLayoutParams: ViewGroup.LayoutParams = params


    override fun remove(): Floating {
        Handler(Looper.getMainLooper()).post {
            if (mEnFloatingView == null) {
                return@post
            }
            if (mEnFloatingView?.isAttachedToWindow == true && container != null) {
                container.removeView(mEnFloatingView)
            }
            mEnFloatingView = null
        }
        return this
    }

    private fun ensureFloatingView() {
        synchronized(this) {
            if (mEnFloatingView != null) {
                mEnFloatingView?.visibility = View.VISIBLE
                return
            }
            val sampleFloatingView = SampleFloatingView(LifecycleUtils.get(), mLayoutId)
            mEnFloatingView = sampleFloatingView
            sampleFloatingView.layoutParams = mLayoutParams
            sampleFloatingView.setIconImage(mIconRes)
            addViewToWindow(sampleFloatingView)
        }
    }

    override fun show(): Floating {
        ensureFloatingView()
        return this
    }

    override fun hide(): Floating {
        mEnFloatingView?.visibility = View.GONE
        return this
    }

    internal fun attach(activity: Activity?): Floating {
        attach(getActivityRoot(activity))
        return this
    }

    private fun attach(container: FrameLayout?): Floating {
        if (container == null || mEnFloatingView == null) {
            mContainer = WeakReference(container)
            return this
        }
        if (mEnFloatingView?.parent === container) {
            return this
        }
        if (mEnFloatingView?.parent != null) {
            (mEnFloatingView?.parent as ViewGroup).removeView(mEnFloatingView)
        }
        mContainer = WeakReference(container)
        container.addView(mEnFloatingView)
        return this
    }

    internal fun detach(activity: Activity?): Floating {
        detach(getActivityRoot(activity))
        return this
    }

    private fun detach(container: FrameLayout?): Floating {
        if (mEnFloatingView != null && container != null && mEnFloatingView!!.isAttachedToWindow) {
            container.removeView(mEnFloatingView)
        }
        if (this.container === container) {
            mContainer = null
        }
        return this
    }

    override val view: FloatingView? = mEnFloatingView


    override fun icon(@DrawableRes resId: Int): Floating {
        mIconRes = resId
        return this
    }

    override fun customView(viewGroup: FloatingView?): Floating {
        mEnFloatingView = viewGroup
        return this
    }

    override fun customView(@LayoutRes resource: Int): Floating {
        mLayoutId = resource
        return this
    }

    override fun layoutParams(params: ViewGroup.LayoutParams): Floating {
        mLayoutParams = params
        mEnFloatingView?.layoutParams = params
        return this
    }

    override fun listener(magnetViewListener: MagnetViewListener): Floating {
        mEnFloatingView?.setMagnetViewListener(magnetViewListener)
        return this
    }

    private fun addViewToWindow(view: View) = container?.addView(view)

    private val container: FrameLayout? = mContainer?.get()


    private val params: FrameLayout.LayoutParams
        get() {
            val params = FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.BOTTOM or Gravity.START
            params.setMargins(13, params.topMargin, params.rightMargin, 500)
            return params
        }

    private fun getActivityRoot(activity: Activity?): FrameLayout? {
        if (activity == null) {
            return null
        }
        try {
            return activity.window.decorView.findViewById<View>(android.R.id.content) as FrameLayout
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun with(target: Any): Floating {
        if (target is Activity) {
            LifecycleUtils.init(target.application)
        }else if (target is Fragment){

            LifecycleUtils.init(target?.)
        }
        return this
    }

    companion object {
        @Volatile
        private var mInstance: Floating? = null
        fun get(): Floating {
            if (mInstance == null) {
                synchronized(Floating::class.java) {
                    if (mInstance == null) {
                        mInstance = Floating()
                    }
                }
            }
            return mInstance!!
        }
    }
}