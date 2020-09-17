package com.ciffelia.mutecamera.appusage

import android.app.usage.UsageEvents
import android.content.Context


class ForegroundAppObserver(
    context: Context,
    private val appChangeHandler: (String?) -> Unit
) {

    private val eventObserver = UsageEventObserver(context, ::handleUsageEvent)

    private var currentForegroundApp: String? = null

    fun startObserve() {
        eventObserver.startObserve(500)
    }

    fun stopObserve() {
        eventObserver.stopObserve()
    }

    private fun handleUsageEvent (event: UsageEvents.Event) {
        val newForegroundApp = when (event.eventType) {
            // Another activity moved to the foreground
            UsageEvents.Event.ACTIVITY_RESUMED -> event.packageName

            // The screen has gone in to a non-interactive state
            UsageEvents.Event.SCREEN_NON_INTERACTIVE -> null

            // Not changed
            else -> currentForegroundApp
        }

        if (newForegroundApp != currentForegroundApp) {
            appChangeHandler(newForegroundApp)
            currentForegroundApp = newForegroundApp
        }
    }
}