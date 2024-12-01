package com.haosen.floating.utils

import android.os.Handler
import android.os.Looper


/**
 * FileName: Utils
 * Author: haosen
 * Date: 2024/11/30 12:09
 * Description:
 **/

object Utils {
    private val mainThreadHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    private fun getUiThreadHandler() = mainThreadHandler

    /** Returns `true` if called on the main thread, `false` otherwise.  */
    fun isOnMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

    /** Returns `true` if called on a background thread, `false` otherwise.  */
    fun isOnBackgroundThread(): Boolean {
        return !isOnMainThread()
    }

    fun assertMainThread() {
        require(isOnMainThread()) { "You must call this method on the main thread" }
    }

    fun <T> getSnapshot(other: Collection<T>): List<T> {
        // toArray creates a new ArrayList internally and does not guarantee that the values it contains
        // are non-null. Collections.addAll in ArrayList uses toArray internally and therefore also
        // doesn't guarantee that entries are non-null. WeakHashMap's iterator does avoid returning null
        // and is therefore safe to use. See #322, #2262.
        val result: MutableList<T> = ArrayList(other.size)
        for (item in other) {
            if (item != null) {
                result.add(item)
            }
        }
        return result
    }

    fun postOnUiThread(runnable: Runnable?) {
        getUiThreadHandler().post(runnable!!)
    }

    /** Removes the given `runnable` from the UI threads queue if it is still queued.  */
    fun removeCallbacksOnUiThread(runnable: Runnable?) {
        getUiThreadHandler().removeCallbacks(runnable!!)
    }
}