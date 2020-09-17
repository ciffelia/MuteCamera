package com.ciffelia.mutecamera

import android.content.Context
import android.media.AudioManager


class DeviceAudio(context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val audioManagerClass = Class.forName("android.media.AudioManager")
    private val audioSystemClass = Class.forName("android.media.AudioSystem")

    // AudioManager.setWiredDeviceConnectionState(type: Integer, state: Integer, address: String, name: String)
    // https://git.io/JUZwt
    private val setWiredDeviceConnectionState by lazy {
        audioManagerClass.getMethod(
            "setWiredDeviceConnectionState",
            Integer.TYPE, Integer.TYPE, String::class.java, String::class.java
        )
    }

    private val DEVICE_OUT_SPEAKER by lazy { getAudioSystemField("DEVICE_OUT_SPEAKER", Integer.TYPE) }
    private val DEVICE_OUT_REMOTE_SUBMIX by lazy { getAudioSystemField("DEVICE_OUT_REMOTE_SUBMIX", Integer.TYPE) }
    // private val DEVICE_OUT_USB_DEVICE by lazy { getAudioSystemField("DEVICE_OUT_USB_DEVICE", Integer.TYPE) }

    private fun <T> getAudioSystemField(name: String, type: Class<T>) = run {
        audioSystemClass.getDeclaredField(name).get(type) as T
    }

    fun mute() {
        setWiredDeviceConnectionState.invoke(audioManager, DEVICE_OUT_SPEAKER, 0, "", "device1")
        setWiredDeviceConnectionState.invoke(audioManager, DEVICE_OUT_REMOTE_SUBMIX, 1, "", "device2")
        // setWiredDeviceConnectionState.invoke(audioManager, DEVICE_OUT_USB_DEVICE, 1, "", "device3")
    }

    fun unmute() {
        setWiredDeviceConnectionState.invoke(audioManager, DEVICE_OUT_SPEAKER, 1, "", "device1")
        setWiredDeviceConnectionState.invoke(audioManager, DEVICE_OUT_REMOTE_SUBMIX, 0, "", "device2")
        // setWiredDeviceConnectionState.invoke(audioManager, DEVICE_OUT_USB_DEVICE, 0, "", "device3")
    }
}
