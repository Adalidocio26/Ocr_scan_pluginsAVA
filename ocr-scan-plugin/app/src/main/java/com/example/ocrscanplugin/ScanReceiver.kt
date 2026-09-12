package com.example.ocrscanplugin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

private const val TAG = "OcrScanPlugin"

/**
 * Recibe el broadcast ACTION_BARCODE_SCANNED de hid-barcode-scanner.
 *
 * Este plugin NO decodifica códigos de barras (eso ya lo hizo la app
 * principal). Lo que hace es tomar el resultado y, opcionalmente,
 * ejecutar OCR sobre la imagen del escaneo (si la app la adjunta) usando
 * ML Kit Text Recognition, para extraer texto adicional que aparezca en
 * la etiqueta/empaque junto al código de barras (ej. lote, fecha de
 * caducidad, número de serie impreso en texto plano).
 */
class ScanReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Protocol.ACTION_BARCODE_SCANNED) return

        val rawValue = intent.getStringExtra(Protocol.EXTRA_RAW_VALUE)
        val processedValue = intent.getStringExtra(Protocol.EXTRA_PROCESSED_VALUE)
        val format = intent.getStringExtra(Protocol.EXTRA_FORMAT)
        val scanImageName = intent.getStringExtra(Protocol.EXTRA_SCAN_IMAGE_NAME)

        Log.d(TAG, "Escaneo recibido: raw=$rawValue processed=$processedValue format=$format image=$scanImageName")

        // Si la app adjunta el nombre/ruta de una imagen del escaneo,
        // le corremos OCR encima. Si no hay imagen, no hay nada que
        // reconocer y solo procesamos el valor ya decodificado.
        if (!scanImageName.isNullOrBlank()) {
            runOcrOnScanImage(context, scanImageName) { recognizedText ->
                handleResult(processedValue ?: rawValue, format, recognizedText)
            }
        } else {
            handleResult(processedValue ?: rawValue, format, null)
        }
    }

    /**
     * Punto único donde decides qué hacer con el resultado final:
     * guardarlo, mandarlo por HTTP/TCP a un servidor, escribirlo a un
     * archivo, mostrarlo en una notificación, etc.
     */
    private fun handleResult(barcodeValue: String?, format: String?, ocrText: String?) {
        Log.i(TAG, "Resultado final -> barcode: $barcodeValue ($format) | OCR: $ocrText")
        // TODO: aquí conectas tu lógica real (HTTP, guardar en BD local, etc.)
    }

    /**
     * Intenta localizar el archivo de imagen del escaneo y correr OCR con
     * ML Kit. La ubicación real donde la app guarda esa imagen (directorio
     * de caché, FileProvider, etc.) depende de cómo la exponga
     * hid-barcode-scanner: revisa ExternalProtocol.kt en el repo principal
     * para saber si "scan_image_name" es una ruta absoluta, un nombre de
     * archivo relativo a un directorio compartido, o requiere un content://
     * URI vía FileProvider.
     */
    private fun runOcrOnScanImage(
        context: Context,
        scanImageName: String,
        onResult: (String?) -> Unit
    ) {
        try {
            // Ajusta esta ruta según cómo la app principal exponga la imagen.
            val imageFile = File(scanImageName)
            if (!imageFile.exists()) {
                Log.w(TAG, "No se encontró el archivo de imagen: $scanImageName")
                onResult(null)
                return
            }

            val image = InputImage.fromFilePath(context, android.net.Uri.fromFile(imageFile))
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    onResult(visionText.text.ifBlank { null })
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error ejecutando OCR", e)
                    onResult(null)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción preparando OCR", e)
            onResult(null)
        }
    }
}
