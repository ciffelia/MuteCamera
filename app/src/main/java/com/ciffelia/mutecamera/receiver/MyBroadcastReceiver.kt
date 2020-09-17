package com.ciffelia.mutecamera.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ciffelia.mutecamera.service.ForegroundService


class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_MY_PACKAGE_REPLACED, Intent.ACTION_BOOT_COMPLETED -> startService(context)
        }
    }

    private fun startService(context: Context) {
        val serviceIntent = Intent(context, ForegroundService::class.java)
        context.startForegroundService(serviceIntent)
    }
}
