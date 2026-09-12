package com.example.ocrscanplugin

/**
 * Constantes del protocolo de plugins externos de hid-barcode-scanner.
 *
 * IMPORTANTE: Los nombres exactos de los "extras" (las claves usadas con
 * intent.getStringExtra(...) etc.) están definidos en el archivo fuente
 * ExternalProtocol.kt del proyecto principal:
 * https://github.com/Fabi019/hid-barcode-scanner/blob/main/app/src/main/java/dev/fabik/bluetoothhid/bt/ExternalProtocol.kt
 *
 * Los valores de abajo son mi mejor estimación a partir del README y de
 * plugins de ejemplo conocidos, pero DEBES verificarlos contra ese archivo
 * (o contra el plugin de ejemplo oficial) antes de confiar en ellos, ya que
 * el protocolo puede cambiar entre versiones.
 * Ejemplo oficial: https://github.com/Fabi019/bt-scanner-example-plugin
 */
object Protocol {
    const val TARGET_PACKAGE = "dev.fabik.bluetoothhid"

    // Acciones que envía la app principal
    const val ACTION_BARCODE_SCANNED = "dev.fabik.bluetoothhid.action.BARCODE_SCANNED"
    const val ACTION_SET_ENABLED = "dev.fabik.bluetoothhid.plugin.action.SET_ENABLED"
    const val ACTION_PING = "dev.fabik.bluetoothhid.plugin.action.PING"
    const val ACTION_SETTINGS = "dev.fabik.bluetoothhid.plugin.action.SETTINGS"

    // Acción que el plugin envía de vuelta
    const val ACTION_STATUS = "dev.fabik.bluetoothhid.plugin.action.STATUS"

    // --- Extras esperados en ACTION_BARCODE_SCANNED (VERIFICAR nombres reales) ---
    const val EXTRA_PROTOCOL_VERSION = "protocol_version"
    const val EXTRA_SCAN_ID = "scan_id"
    const val EXTRA_RAW_VALUE = "raw_value"
    const val EXTRA_PROCESSED_VALUE = "processed_value"
    const val EXTRA_FORMAT = "format"
    const val EXTRA_TIMESTAMP = "timestamp"
    const val EXTRA_SOURCE = "source"
    const val EXTRA_SCANNER_ID = "scanner_id"
    const val EXTRA_REGEX_GROUPS = "regex_groups"
    const val EXTRA_SCAN_IMAGE_NAME = "scan_image_name"

    // --- Extras usados al responder con ACTION_STATUS ---
    const val EXTRA_PACKAGE = "package"
    const val EXTRA_RUNNING = "running"
    const val EXTRA_STATE = "state"
    const val EXTRA_STATUS_DETAIL = "status_detail"

    // Estados soportados según el README del proyecto principal
    object State {
        const val IDLE = "IDLE"
        const val STARTING = "STARTING"
        const val CONNECTING = "CONNECTING"
        const val CONNECTED = "CONNECTED"
        const val LISTENING = "LISTENING"
        const val ERROR = "ERROR"
        const val BLOCKED = "BLOCKED"
        const val NO_PERMISSION = "NO_PERMISSION"
        const val UNKNOWN = "UNKNOWN"
    }
}
