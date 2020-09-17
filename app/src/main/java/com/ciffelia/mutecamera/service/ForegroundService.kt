package com.ciffelia.mutecamera.service

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.widget.Toast
import com.ciffelia.mutecamera.DeviceAudio
import com.ciffelia.mutecamera.Preferences
import com.ciffelia.mutecamera.appusage.ForegroundAppObserver


class ForegroundService : Service() {

    private val foregroundNotification by lazy { ForegroundNotification(this) }

    private val preferences by lazy { Preferences(this) }

    private val appObserver by lazy {
        ForegroundAppObserver(this, ::handleForegroundAppChange)
    }

    private val deviceAudio by lazy { DeviceAudio(this) }

    private var isMuted = false


    override fun onBind(intent: Intent): IBinder? = TODO()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        foregroundNotification.startForeground()

        preferences.listener = SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, key: String ->
            when (key) {
                "isServiceEnabled" -> handleStartStopRequest()
            }
        }

        if (preferences.isServiceEnabled) {
            appObserver.startObserve()
        }

        return START_STICKY
    }


    private fun handleStartStopRequest () {
        if (preferences.isServiceEnabled) {
            appObserver.startObserve()
        } else {
            appObserver.stopObserve()
        }
    }

    private fun handleForegroundAppChange (packageName: String?) {
        val shouldMuted = packageName != null && preferences.isAppEnabled(packageName)

        if (!isMuted && shouldMuted) {
            deviceAudio.mute()
            isMuted = true
            showToast("Audio muted")
        } else if (isMuted && !shouldMuted) {
            deviceAudio.unmute()
            isMuted = false
            showToast("Audio unmuted")
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
