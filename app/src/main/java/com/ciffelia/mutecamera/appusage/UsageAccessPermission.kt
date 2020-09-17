package com.ciffelia.mutecamera.appusage

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings
import android.widget.Toast


class UsageAccessPermission(private val context: Context) {

    private val aom = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

    fun isGranted(): Boolean {
        val aom = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

        val mode = aom.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )

        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun openPermissionSettings() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        context.startActivity(intent)

        Toast.makeText(context, "MuteCamera needs permission to detect foreground apps.", Toast.LENGTH_LONG).show()
    }
}
