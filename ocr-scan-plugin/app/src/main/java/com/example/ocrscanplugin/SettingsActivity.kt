package com.example.ocrscanplugin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Pantalla de ajustes muy básica. También sirve como punto de entrada
 * normal (launcher) para sacar la app del estado "stopped" tras instalarla.
 * Se abre desde el selector de plugins de la app principal al mantener
 * pulsado el icono de este plugin, o desde el ícono normal del launcher.
 */
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this).apply {
            text = "OCR Scan Plugin\n\n" +
                "Si ves esta pantalla, el plugin ya salió del estado " +
                "'detenido' de Android y puede recibir escaneos. " +
                "No necesitas hacer nada más aquí: vuelve a la app " +
                "HID Barcode Scanner y selecciona este plugin en " +
                "Connection Mode > External Plugin."
            setPadding(48, 48, 48, 48)
        }
        setContentView(textView)
    }
}
