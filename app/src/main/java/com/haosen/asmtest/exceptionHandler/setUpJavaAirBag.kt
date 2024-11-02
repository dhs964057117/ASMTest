package com.haosen.asmtest.exceptionHandler

import android.os.Build
import android.os.Looper
import android.util.Log

data class JavaAirBagConfig(
    val crashType: String,
    val crashMessage: String,
    val crashClass: String,
    val crashMethod: String,
    val crashAndroidVersion: Int = 0
)

fun setUpJavaAirBag(configList: List<JavaAirBagConfig>) {
    val preDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
    // 设置自定义处理器
    Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
        handleException(preDefaultExceptionHandler, configList, thread, exception)
        if (thread == Looper.getMainLooper().thread) {
            // 重启 Looper
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    handleException(
                        preDefaultExceptionHandler, configList, Thread.currentThread(), e
                    )
                }
            }
        }
    }
}

private fun handleException(
    preDefaultExceptionHandler: Thread.UncaughtExceptionHandler,
    configList: List<JavaAirBagConfig>,
    thread: Thread,
    exception: Throwable
) {
    // 匹配配置
    if (configList.any { isStackTraceMatching(exception, it) }) {
        Log.w("StabilityOptimize", "Java Crash 已捕获")
    } else {
        Log.w("StabilityOptimize", "Java Crash 未捕获，交给原有 ExceptionHandler 处理")
        Log.e("StabilityOptimize", "${exception.message}")
        preDefaultExceptionHandler.uncaughtException(thread, exception)
    }
}

private fun isStackTraceMatching(exception: Throwable, config: JavaAirBagConfig): Boolean {
    val stackTraceElement = exception.stackTrace[0]
    return config.crashType == exception.javaClass.name
            && config.crashMessage == exception.message
            && config.crashClass == stackTraceElement?.className
            && config.crashMethod == stackTraceElement.methodName
            && (config.crashAndroidVersion == 0 || (config.crashAndroidVersion == Build.VERSION.SDK_INT))
}