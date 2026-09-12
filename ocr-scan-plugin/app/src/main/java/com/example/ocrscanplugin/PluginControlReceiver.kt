package com.example.ocrscanplugin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log

private const val TAG = "OcrScanPlugin"
private const val PREFS_NAME = "ocr_plugin_prefs"
private const val PREF_ENABLED = "enabled"

/**
 * Canal de control con la app principal:
 * - ACTION_PING: la app pregunta si el plugin está vivo -> respondemos ACTION_STATUS.
 * - ACTION_SET_ENABLED: la app informa si el usuario activó/desactivó el plugin.
 */
class PluginControlReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Protocol.ACTION_PING -> respondStatus(context)
            Protocol.ACTION_SET_ENABLED -> {
                val enabled = intent.getBooleanExtra("enabled", true)
                prefs(context).edit().putBoolean(PREF_ENABLED, enabled).apply()
                Log.d(TAG, "Plugin ${if (enabled) "habilitado" else "deshabilitado"}")
            }
        }
    }

    private fun respondStatus(context: Context) {
        val enabled = prefs(context).getBoolean(PREF_ENABLED, true)
        val state = if (enabled) Protocol.State.IDLE else Protocol.State.BLOCKED

        val statusIntent = Intent(Protocol.ACTION_STATUS).apply {
            setPackage(Protocol.TARGET_PACKAGE)
            putExtra(Protocol.EXTRA_PROTOCOL_VERSION, 1)
            putExtra(Protocol.EXTRA_PACKAGE, context.packageName)
            putExtra(Protocol.EXTRA_RUNNING, true)
            putExtra(Protocol.EXTRA_STATE, state)
            putExtra(Protocol.EXTRA_STATUS_DETAIL, "OCR plugin listo")
        }
        context.sendBroadcast(statusIntent)
    }

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
