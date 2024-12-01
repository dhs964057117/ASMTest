package com.haosen.floating.core

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
import androidx.fragment.app.FragmentActivity
import com.haosen.floating.R
import com.haosen.floating.manager.Config
import com.haosen.floating.manager.MagnetViewListener
import com.haosen.floating.manager.SampleFloatingView
import com.haosen.floating.utils.LifecycleUtils
import java.lang.ref.WeakReference


/**
 * FileName: FloatingImpl
 * Author: haosen
 * Date: 2024/11/30 14:37
 * Description:
 **/
class FloatingImpl(config: Config) : IFloatingView {
    private var mEnFloatingView: FloatingView? = null
    private var mContainer: WeakReference<FrameLayout>? = null

    @LayoutRes
    private var mLayoutId = R.layout.en_floating_view

    @DrawableRes
    private var mIconRes = R.drawable.imuxuan
    private var mLayoutParams: ViewGroup.LayoutParams = params


    override fun remove() {
        Handler(Looper.getMainLooper()).post {
            if (mEnFloatingView == null) {
                return@post
            }
            if (mEnFloatingView?.isAttachedToWindow == true && container != null) {
                container.removeView(mEnFloatingView)
            }
            mEnFloatingView = null
        }
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

    override fun show() {
        ensureFloatingView()
    }

    override fun hide() {
        mEnFloatingView?.visibility = View.GONE
    }

    internal fun attach(fragment: Fragment?): FloatingImpl {
        attach(getActivityRoot(fragment?.activity))
        return this
    }

    internal fun attach(activity: Activity?): FloatingImpl {
        attach(getActivityRoot(activity))
        return this
    }

    private fun attach(container: FrameLayout?): FloatingImpl {
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

    internal fun detach(fragment: Fragment?): FloatingImpl {
        detach(getActivityRoot(fragment?.activity))
        return this
    }

    internal fun detach(activity: Activity?): FloatingImpl {
        detach(getActivityRoot(activity))
        return this
    }

    private fun detach(container: FrameLayout?): FloatingImpl {
        if (mEnFloatingView != null && container != null && mEnFloatingView!!.isAttachedToWindow) {
            container.removeView(mEnFloatingView)
        }
        if (this.container === container) {
            mContainer = null
        }
        return this
    }

    override val view: FloatingView? = mEnFloatingView


    override fun icon(@DrawableRes resId: Int): FloatingImpl {
        mIconRes = resId
        return this
    }

    override fun layoutParams(params: ViewGroup.LayoutParams): FloatingImpl {
        mLayoutParams = params
        mEnFloatingView?.layoutParams = params
        return this
    }

    override fun listener(magnetViewListener: MagnetViewListener): FloatingImpl {
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
}