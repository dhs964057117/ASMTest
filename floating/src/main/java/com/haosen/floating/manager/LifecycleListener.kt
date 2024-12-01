package com.haosen.floating.manager

import androidx.lifecycle.LifecycleOwner

/**
 * An interface for listener to [android.app.Fragment] and [android.app.Activity]
 * lifecycle events.
 */
interface LifecycleListener {
    /**
     * Callback for when [android.app.Fragment.onStart]} or [ ][android.app.Activity.onStart] is called.
     */
    fun onStart(owner: LifecycleOwner)

    /**
     * Callback for when [android.app.Fragment.onStop]} or [ ][android.app.Activity.onStop]} is called.
     */
    fun onStop(owner: LifecycleOwner)

    /**
     * Callback for when [android.app.Fragment.onDestroy]} or [ ][android.app.Activity.onDestroy] is called.
     */
    fun onDestroy(owner: LifecycleOwner)
}
