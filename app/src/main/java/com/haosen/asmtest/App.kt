package com.haosen.asmtest

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.haosen.floating.Floating
import com.haosen.tools.base.http.OkHelper
import com.haosen.tools.base.http.setBaseUrl
import com.haosen.tools.base.http.setHttpClient

class App : Application() {
    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycObserver())
        registerActivityLifecycleCallbacks(ActivityLifecycObserver())
        application = this
        setBaseUrl("https://www.wanandroid.com/")
        setHttpClient(OkHelper.httpClient(applicationContext))
        Floating.get().with(this).show()
    }
}
