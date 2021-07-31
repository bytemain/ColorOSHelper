package me.lengthmin.coshelper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val BOOT_ACTION = "android.intent.action.BOOT_COMPLETED"

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (BOOT_ACTION == intent.action) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            val boot = sharedPref.getBoolean(context.getString(R.string.settings_boot), false)
            if (boot) {
                setRefreshRateByContext(context, "30")
                notify(context)
            }
        }
    }
}