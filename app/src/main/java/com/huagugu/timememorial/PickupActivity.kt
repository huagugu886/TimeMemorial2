package com.huagugu.timememorial

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

/**
 * 取件码功能：权限申请 + 最近取件码显示
 * 被 SmsReceiver 的通知点击打开
 */
class PickupActivity : ComponentActivity() {

    private val perms = buildList {
        add(Manifest.permission.RECEIVE_SMS)
        add(Manifest.permission.READ_SMS)
        if (Build.VERSION.SDK_INT >= 33) add(Manifest.permission.POST_NOTIFICATIONS)
    }.toTypedArray()

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.all { it.value }) {
            Toast.makeText(this, "权限已授予，取件码监听已就绪", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "需要短信权限才能监听取件码", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 申请权限
        val need = perms.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (need.isNotEmpty()) launcher.launch(need.toTypedArray())

        // 处理通知点击
        handleIntent(intent)
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: android.content.Intent?) {
        val code = intent?.getStringExtra("code") ?: return
        Toast.makeText(this, "取件码：$code", Toast.LENGTH_LONG).show()
        PickupNotificationHelper.cancel(this)
    }
}
