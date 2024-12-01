package com.haosen.floating.manager

/**
 * A [com.haosen.floating.manager.Lifecycle] implementation for tracking and notifying
 * listeners of [android.app.Application] lifecycle events.
 *
 *
 * Since there are essentially no [android.app.Application] lifecycle events, this class
 * simply defaults to notifying new listeners that they are started.
 */
internal class ApplicationLifecycle : Lifecycle {
    override fun addListener(listener: LifecycleListener) {
    }

    override fun removeListener(listener: LifecycleListener) {
    }
}