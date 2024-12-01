package com.haosen.floating.core

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.haosen.floating.manager.Config
import com.haosen.floating.manager.LifecycleListener
import com.haosen.floating.manager.LifecycleLifecycle
import com.haosen.floating.utils.Utils.assertMainThread

object LifecycleManagerRetriever {
    val lifecycleToRequestManager: MutableMap<Lifecycle, FloatingManager> = HashMap()

    private fun getOnly(lifecycle: Lifecycle): FloatingManager? {
        assertMainThread()
        return lifecycleToRequestManager[lifecycle]
    }

    fun getOrCreate(
        context: Context?,
        lifecycle: Lifecycle,
        config: Config
    ): FloatingManager {
        assertMainThread()
        var result = getOnly(lifecycle)
        if (result == null) {
            val glideLifecycle = LifecycleLifecycle(lifecycle)
            result =
                FloatingManager.Builder().build(context, glideLifecycle, config)
            lifecycleToRequestManager[lifecycle] = result
            glideLifecycle.addListener(
                object : LifecycleListener {
                    override fun onStart(owner: LifecycleOwner) {
                    }

                    override fun onStop(owner: LifecycleOwner) {
                    }

                    override fun onDestroy(owner: LifecycleOwner) {
                        lifecycleToRequestManager.remove(lifecycle)
                    }
                })
            // This is a bit of hack, we're going to start the FloatingManager, but not the
            // corresponding Lifecycle. It's safe to start the FloatingManager, but starting the
            // Lifecycle might trigger memory leaks.
        }
        return result
    }
}
