package com.haosen.asmtest

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log

class ActivityLifecycObserver : ActivityLifecycleCallbacks {
    companion object {
        const val TAG = "TAG_ActivityObserver"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onCreate")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onActivityDestroyed")
    }
}