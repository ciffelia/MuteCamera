package com.ciffelia.mutecamera

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ciffelia.mutecamera.appusage.UsageAccessPermission
import com.ciffelia.mutecamera.installedapp.InstalledApp
import com.ciffelia.mutecamera.installedapp.listCameraApp
import com.ciffelia.mutecamera.service.ForegroundService
import com.ciffelia.mutecamera.ui.AppRecyclerViewAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    private val preferences by lazy { Preferences(this) }

    private val usagePermission by lazy { UsageAccessPermission(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        setupServiceEnableSwitch()
        setupAppRecyclerView()

        val isPermissionGranted = usagePermission.isGranted()
        if (!isPermissionGranted) {
            usagePermission.openPermissionSettings()
        }

        val serviceIntent = Intent(this, ForegroundService::class.java)
        startForegroundService(serviceIntent)
    }

    private fun setupServiceEnableSwitch () {
        val switch = findViewById<SwitchCompat>(R.id.serviceEnableSwitch)

        switch.isChecked = preferences.isServiceEnabled

        switch.setOnCheckedChangeListener { _, isChecked ->
            preferences.isServiceEnabled = isChecked
        }
    }

    private fun setupAppRecyclerView () {
        val cameraApps = listCameraApp(this).sortedWith(
            compareByDescending<InstalledApp> { preferences.isAppEnabled(it.packageName) }
                .thenBy(String.CASE_INSENSITIVE_ORDER) { it.name }
        )

        recyclerView = findViewById<RecyclerView>(R.id.appList).apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            adapter = AppRecyclerViewAdapter(cameraApps, preferences)
        }
    }

    /*private fun printUsageEvent (event: UsageEvents.Event) {
        val timestamp = event.timeStamp
        val packageName = event.packageName

        val eventType = when (event.eventType) {
            UsageEvents.Event.ACTIVITY_RESUMED -> "ACTIVITY_RESUMED"
            UsageEvents.Event.ACTIVITY_PAUSED -> "ACTIVITY_PAUSED"
            UsageEvents.Event.ACTIVITY_STOPPED -> "ACTIVITY_STOPPED"

            UsageEvents.Event.SCREEN_INTERACTIVE -> "SCREEN_INTERACTIVE"
            UsageEvents.Event.SCREEN_NON_INTERACTIVE -> "SCREEN_NON_INTERACTIVE"

            UsageEvents.Event.KEYGUARD_SHOWN -> "KEYGUARD_SHOWN"
            UsageEvents.Event.KEYGUARD_HIDDEN -> "KEYGUARD_HIDDEN"

            else -> event.eventType.toString()
        }

        Log.d("events", "[$eventType]@$timestamp: $packageName, ${event.className}")
    }*/
}
