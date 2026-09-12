package com.example.ocrscanplugin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Pantalla de ajustes muy básica. Se abre desde el selector de plugins de
 * la app principal al mantener pulsado el icono de este plugin.
 * Aquí podrías añadir, por ejemplo, un switch para elegir si el OCR corre
 * siempre o solo cuando el valor del código de barras cumple cierto patrón,
 * o un campo para configurar el idioma esperado del texto.
 */
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this).apply {
            text = "Ajustes del plugin OCR (placeholder)"
            setPadding(48, 48, 48, 48)
        }
        setContentView(textView)
    }
}
