package com.ciffelia.mutecamera.installedapp

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class InstalledApp(applicationInfo: ApplicationInfo, pm: PackageManager) {
    val packageName: String = applicationInfo.packageName

    val name: String by lazy { pm.getApplicationLabel(applicationInfo).toString() }
    val icon: Drawable by lazy { pm.getApplicationIcon(applicationInfo) }
}
