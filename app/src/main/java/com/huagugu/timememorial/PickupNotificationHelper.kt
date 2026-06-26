package com.huagugu.timememorial

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.PowerManager
import android.util.Log

/**
 * 取件码通知 → 灵动岛 + 焦点通知
 *
 * MIUI/HyperOS 灵动岛触发条件：
 *   - 渠道 importance = HIGH
 *   - category = CATEGORY_MESSAGE
 *   - priority = PRIORITY_MAX
 *   - fullScreenIntent（锁屏时触发）
 *
 * 焦点通知：同一渠道弹出 heads-up 横幅，亮屏/锁屏均可见
 */
object PickupNotificationHelper {

    private const val TAG = "PickupIsland"
    private const val CHANNEL_ID = "pickup_code"
    private const val NOTIF_ID = 9527

    fun show(context: Context, sender: String, pickup: PickupParser.PickupInfo) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannel(context, nm)

        val title = buildString {
            append("📦 取件码：${pickup.code}")
            if (pickup.courier.isNotEmpty()) append(" · ${pickup.courier}")
        }
        val text = buildString {
            if (pickup.station.isNotEmpty()) append(pickup.station)
            append(" 短信来源：$sender")
        }

        // 点击打开 App
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("code", pickup.code)
            putExtra("station", pickup.station)
            putExtra("courier", pickup.courier)
            putExtra("sender", sender)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val tapPending = PendingIntent.getActivity(
            context, System.currentTimeMillis().toInt(), tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notif = Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(tapPending)
            .setFullScreenIntent(tapPending, true)       // 灵动岛/锁屏关键
            .setPriority(Notification.PRIORITY_MAX)       // 最高优先级
            .setCategory(Notification.CATEGORY_MESSAGE)   // 消息类 → MIUI 灵动岛
            .setVisibility(Notification.VISIBILITY_PUBLIC) // 锁屏可见
            .setAutoCancel(true)
            .setStyle(Notification.BigTextStyle().bigText(text))
            .build()

        nm.notify(NOTIF_ID, notif)
        Log.d(TAG, "通知已发: $title")

        // 唤醒屏幕 2 秒，确保灵动岛弹出
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

    private fun createChannel(context: Context, nm: NotificationManager) {
        if (nm.getNotificationChannel(CHANNEL_ID) != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "取件码通知",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "快递取件码到达时弹出灵动岛和焦点通知"
            enableLights(true)
            lightColor = Color.parseColor("#4CAF50")
            enableVibration(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        nm.createNotificationChannel(channel)
    }

    fun cancel(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIF_ID)
    }
}
