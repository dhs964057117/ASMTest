package com.haosen.floating.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.haosen.floating.Floating

/**
 * FileName: ContextHelper
 * Author: haosen
 * Date: 2024/11/8 20:21
 * Description:
 */
object LifecycleUtils {
    @Volatile
    private var instance: Application? = null
    fun init(application: Application) {
        instance = application
        instance?.registerLifecycleCallbacks()
    }

    private fun initWithReflect() {
        var app: Application? = null
        try {
            app = Class.forName("android.app.AppGlobals").getMethod("getInitialApplication")
                .invoke(null) as Application
            checkNotNull(app) { "Static initialization of Applications must be on main thread." }
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                app = Class.forName("android.app.ActivityThread").getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (ex: Exception) {
                e.printStackTrace()
            }
        } finally {
            checkNotNull(app) { "Static initialization of Applications must be on main thread." }
            instance = app
        }
    }

    @JvmStatic
    fun get(): Application {
        if (instance == null) {
            synchronized(LifecycleUtils::class.java) {
                if (instance == null) {
                    initWithReflect()
                    instance?.registerLifecycleCallbacks()
                }
            }
        }
        checkNotNull(instance) { "Static initialization of Applications must be on main thread." }
        return instance!!
    }

    private fun Application.registerLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
                Floating.get().attach(activity)
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                Floating.get().detach(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }
}
