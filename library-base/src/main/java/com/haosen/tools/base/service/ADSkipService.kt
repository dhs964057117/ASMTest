package com.haosen.tools.base.service

import android.accessibilityservice.AccessibilityService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.app.NotificationCompat
import com.haosen.tools.base.R

class ADSkipService : AccessibilityService() {

    companion object {
        const val NOTICE_ID = 20230908
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val source = event.source
        if (source?.text?.contains("跳过") == true) {
            source.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }

    override fun onInterrupt() {
    }

    override fun onCreate() {
        super.onCreate()
        val id = packageName
        val name = javaClass.name
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        startForeground(
            NOTICE_ID, NotificationCompat.Builder(applicationContext, id)
                .setContentTitle("跳过广告服务~")
                .setContentText("该服务仅支持部分APP跳过广告。")
                .setSmallIcon(R.drawable.logo)
                .build()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

}