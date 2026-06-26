package com.huagugu.memorial.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.huagugu.memorial.pickup.PickupNotificationHelper
import com.huagugu.memorial.pickup.PickupParser

class SmsPickupReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val prefs = context.getSharedPreferences("pickup_island", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("enabled", true)) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        val grouped = messages
            .filter { it.displayOriginatingAddress != null }
            .groupBy { it.displayOriginatingAddress }
            .mapValues { (_, parts) ->
                parts.sortedBy { it.timestampMillis }
                    .joinToString("") { it.messageBody ?: "" }
            }

        for ((_, body) in grouped) {
            val pickup = PickupParser.parse(body) ?: continue
            Log.d("PickupIsland", "命中取件码: ${pickup.code}")

            prefs.edit().apply {
                putString("last_code", pickup.code)
                putString("last_station", pickup.station)
                putString("last_courier", pickup.courier)
                putLong("last_time", System.currentTimeMillis())
                apply()
            }

            PickupNotificationHelper.show(context, pickup)
        }
    }
}
