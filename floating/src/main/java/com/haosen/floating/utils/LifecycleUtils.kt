package com.haosen.floating.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

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
                }
            }
        }
        checkNotNull(instance) { "Static initialization of Applications must be on main thread." }
        return instance!!
    }
}
