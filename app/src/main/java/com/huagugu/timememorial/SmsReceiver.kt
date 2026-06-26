package com.huagugu.timememorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val prefs = context.getSharedPreferences("pickup", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("enabled", true)) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        val grouped = messages
            .filter { it.displayOriginatingAddress != null }
            .groupBy { it.displayOriginatingAddress }
            .mapValues { (_, parts) ->
                parts.sortedBy { it.timestampMillis }
                    .joinToString("") { it.messageBody ?: "" }
            }

        for ((sender, body) in grouped) {
            if (body.isBlank()) continue
            val pickup = PickupParser.parse(body) ?: continue
            Log.d("PickupIsland", "命中: ${pickup.code}")
            saveRecent(context, sender, pickup)
            PickupNotificationHelper.show(context, sender, pickup)
        }
    }

    private fun saveRecent(context: Context, sender: String, p: PickupParser.PickupInfo) {
        context.getSharedPreferences("pickup", Context.MODE_PRIVATE).edit().apply {
            putString("last_code", p.code)
            putString("last_station", p.station)
            putString("last_courier", p.courier)
            putString("last_sender", sender)
            putLong("last_time", System.currentTimeMillis())
            apply()
        }
    }
}
