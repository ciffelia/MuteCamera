package com.ciffelia.mutecamera

import android.content.Context
import android.content.SharedPreferences


class Preferences(context: Context) {
    private val pref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    var listener: SharedPreferences.OnSharedPreferenceChangeListener? = null
        set(value) {
            if (field != null) {
                pref.unregisterOnSharedPreferenceChangeListener(field)
            }
            if (value != null) {
                pref.registerOnSharedPreferenceChangeListener(value)
            }

            field = value
        }


    var isServiceEnabled: Boolean
        get() = pref.getBoolean("isServiceEnabled", true)
        set(value) {
            pref.edit().apply {
                putBoolean("isServiceEnabled", value)
                apply()
            }
        }

    var enabledApps: Set<String>
        get() = pref.getStringSet("enabledApps", emptySet()) as Set<String>
        set(value) {
            pref.edit().apply {
                putStringSet("enabledApps", value)
                apply()
            }
        }


    fun isAppEnabled (packageName: String) = enabledApps.contains(packageName)

    fun setAppEnabled (packageName: String, enabled: Boolean) {
        enabledApps = enabledApps.toMutableSet().apply {
            if (enabled) add(packageName)
            else remove(packageName)
        }
    }
}
