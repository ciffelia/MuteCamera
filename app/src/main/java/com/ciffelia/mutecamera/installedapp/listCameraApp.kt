package com.ciffelia.mutecamera.installedapp

import android.content.Context
import android.content.pm.PackageManager


fun listCameraApp(context: Context): List<InstalledApp> {
    val pm: PackageManager = context.packageManager

    val packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)

    val cameraAppPackages = packages.filter {
        it.requestedPermissions?.contains("android.permission.CAMERA") ?: false
    }

    return cameraAppPackages.map {
        InstalledApp(it.applicationInfo, pm)
    }
}
