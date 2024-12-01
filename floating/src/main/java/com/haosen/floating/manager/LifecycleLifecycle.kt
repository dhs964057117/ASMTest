package com.haosen.floating.manager

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.haosen.floating.utils.Utils.getSnapshot

internal class LifecycleLifecycle(lifecycle: androidx.lifecycle.Lifecycle) : Lifecycle,
    LifecycleObserver {
    private val lifecycleListeners: MutableSet<LifecycleListener> = HashSet()

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_START)
    fun onStart(owner: LifecycleOwner) {
        for (lifecycleListener in getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStart(owner)
        }
    }

    @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_STOP)
    fun onStop(owner: LifecycleOwner) {
        for (lifecycleListener in getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStop(owner)
        }
    }

    @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        for (lifecycleListener in getSnapshot(lifecycleListeners)) {
            lifecycleListener.onDestroy(owner)
        }
        owner.lifecycle.removeObserver(this)
    }

    override fun addListener(listener: LifecycleListener) {
        lifecycleListeners.add(listener)
    }

    override fun removeListener(listener: LifecycleListener) {
        lifecycleListeners.remove(listener)
    }
}
