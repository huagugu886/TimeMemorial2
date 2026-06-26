package com.huagugu.memorial.pickup

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat

object PickupNotificationHelper {

    private const val TAG = "PickupIsland"
    private const val CHANNEL_ID = "pickup_island"
    private const val NOTIF_ID = 2001

    fun show(context: Context, pickup: PickupParser.PickupInfo) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannel(context, nm)

        val title = buildString {
            append("📦 取件码：${pickup.code}")
            if (pickup.courier.isNotEmpty()) append(" · ${pickup.courier}")
        }
        val text = pickup.station.ifEmpty { "点击查看详情" }

        // 点击跳转（复用现有 MainActivity）
        val launchIntent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?.apply {
                putExtra("pickup_code", pickup.code)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        val pi = PendingIntent.getActivity(
            context, System.currentTimeMillis().toInt(), launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pi)
            .setFullScreenIntent(pi, true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setExtras(Bundle().apply {
                putBoolean("miui.enableFloat", true)
                putInt("miui.focus", 1)
            })
            .build()

        nm.notify(NOTIF_ID, notification)
        wakeScreen(context)
        Log.d(TAG, "通知已发送: $title")
    }

    private fun createChannel(context: Context, nm: NotificationManager) {
        if (nm.getNotificationChannel(CHANNEL_ID) != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "取件码灵动岛",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "取件码短信到达时灵动岛展示"
            enableLights(true)
            lightColor = Color.parseColor("#4CAF50")
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 100, 100, 100)
        }
        nm.createNotificationChannel(channel)
    }

    private fun wakeScreen(context: Context) {
        try {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wl = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "PickupIsland:wake"
            )
            wl.acquire(2000L)
            wl.release()
        } catch (_: Exception) {}
    }
}
