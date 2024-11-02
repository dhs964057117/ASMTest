package com.haosen.asmtest

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner

class App : Application() {
    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycObserver())
        registerActivityLifecycleCallbacks(ActivityLifecycObserver())
        application = this
    }
}
