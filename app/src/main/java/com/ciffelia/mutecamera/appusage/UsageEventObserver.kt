package com.ciffelia.mutecamera.appusage

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Handler


class UsageEventObserver(
    context: Context,
    private val eventHandler: (UsageEvents.Event) -> Unit
) {

    private val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    private var observeHandler: Handler? = null

    private var lastCheckedAt: Long = 0

    fun startObserve(interval: Long) {
        if (observeHandler != null) {
            stopObserve()
        }

        lastCheckedAt = System.currentTimeMillis()

        observeHandler = Handler()

        val runnable = object : Runnable {
            override fun run() {
                fetchNewEvents()
                observeHandler!!.postDelayed(this, interval)
            }
        }

        observeHandler!!.post(runnable)
    }

    fun stopObserve() {
        observeHandler?.removeCallbacksAndMessages(null)
        observeHandler = null
    }

    private fun fetchNewEvents() {
        val now = System.currentTimeMillis()

        val events = usm.queryEvents(lastCheckedAt, now) ?: return

        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            if (events.getNextEvent(event)) {
                eventHandler(event)
            }
        }

        lastCheckedAt = now
    }
}
