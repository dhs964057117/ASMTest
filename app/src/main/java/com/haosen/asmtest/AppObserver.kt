package com.haosen.asmtest

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycObserver : DefaultLifecycleObserver {
    companion object {
        const val TAG = "TAGAppObserver"
    }

    /**
     * 在App的整个生命周期中只会被调用一次
     */
    override fun onCreate(owner: LifecycleOwner) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onCreate")
    }

    /**
     * 在App在前台出现时被调用
     */
    override fun onStart(owner: LifecycleOwner) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onStart")
    }

    /**
     * 在App在前台出现时被调用
     */
    override fun onResume(owner: LifecycleOwner) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onResume")
    }

    /**
     * 在App退出到后台时被调用
     */
    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onPause")
    }

    /**
     * 在App退出到后台时被调用
     */
    override fun onStop(owner: LifecycleOwner) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onStop")
    }

    /**
     * 理论上是不会触发这个事件的，因为系统不会分发，在项目当中可以不写这个事件
     */
    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>> onStop")
    }
}