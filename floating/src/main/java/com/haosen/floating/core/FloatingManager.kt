package com.haosen.floating.core

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.haosen.floating.manager.ApplicationLifecycle
import com.haosen.floating.manager.Config
import com.haosen.floating.manager.LifecycleListener
import com.haosen.floating.utils.Utils.isOnBackgroundThread
import com.haosen.floating.utils.Utils.postOnUiThread


/**
 * FileName: FloatingManager
 * Author: haosen
 * Date: 2024/11/30 11:42
 * Description: 生命周期管理
 **/
class FloatingManager private constructor(
    context: Context?,
    private val lifecycle: com.haosen.floating.manager.Lifecycle,
    private val config: Config
) : LifecycleListener, ActivityLifecycleCallbacks {
    // Objects used to find Fragments and Activities containing views.
    companion object {
        @Volatile
        private var floatingManager: FloatingManager? = null

        fun build(
            context: Context?,
            lifecycle: com.haosen.floating.manager.Lifecycle,
            config: Config
        ): FloatingManager {
            if (floatingManager == null) {
                synchronized(this) {
                    if (floatingManager == null) {
                        floatingManager = FloatingManager(context, lifecycle, config)
                    }
                }
            }
            return floatingManager!!
        }
    }

    private val floatingImpl: FloatingImpl by lazy { FloatingImpl(config) }

    private val addSelfToLifecycle: Runnable = Runnable {
        bindLifecycle(context)
    }

    init {
        // If we're the application level request manager, we may be created on a background thread.
        // In that case we cannot risk synchronously pausing or resuming requests, so we hack around the
        // issue by delaying adding ourselves as a lifecycle listener by posting to the main thread.
        // This should be entirely safe.
        if (isOnBackgroundThread()) {
            postOnUiThread(addSelfToLifecycle)
        } else {
            bindLifecycle(context)
        }
    }

    private fun bindLifecycle(context: Context?) {
        if (lifecycle is ApplicationLifecycle) {
            (context?.applicationContext as? Application)?.registerActivityLifecycleCallbacks(
                this
            )
        } else {
            lifecycle.addListener(this)
        }
    }

    fun show() {
        floatingImpl.show()
    }

    class Builder {
        fun build(
            context: Context?,
            lifecycle: com.haosen.floating.manager.Lifecycle,
            config: Config
        ): FloatingManager {
            return FloatingManager.build(context, lifecycle, config)
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        if (owner is FragmentActivity)
            floatingImpl.attach(owner)
        if (owner is Fragment)
            floatingImpl.attach(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        if (owner is FragmentActivity)
            floatingImpl.detach(owner)
        if (owner is Fragment)
            floatingImpl.detach(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        floatingImpl.attach(activity)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        floatingImpl.detach(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
