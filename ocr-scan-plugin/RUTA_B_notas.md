# Ruta B — OCR de texto libre en hid-barcode-scanner (para retomar después)

**Objetivo:** que la app reconozca texto normal con la cámara, no solo códigos
de barras. Esto no se puede hacer con el sistema de plugins (los plugins solo
reciben el valor ya decodificado, no frames de cámara), así que requiere
hacer **fork** del repo y tocar el código fuente.

## Pasos generales

1. Fork de https://github.com/Fabi019/hid-barcode-scanner y abrir en Android Studio.
2. Ubicar el pipeline de análisis de cámara: usa **CameraX** + **zxing-cpp**
   para decodificar cada frame (`ImageAnalysis.Analyzer`).
3. Añadir una librería de OCR en paralelo al decodificador de barcodes:
   - **ML Kit Text Recognition** (Google, on-device, gratis, se integra
     directo con `ImageAnalysis.Analyzer` de CameraX) — la opción más simple.
     Dependencia: `com.google.mlkit:text-recognition:16.0.1`
   - o **Tesseract** (via `Tesseract4Android`) si se necesita 100% offline
     sin depender de Google Play Services.
4. En el analyzer, correr ambos detectores sobre el mismo frame (o alternar
   entre modos "código de barras" / "texto") y unificar el resultado en el
   mismo flujo que ya usa la app para "código escaneado" — así se beneficia
   automáticamente de historial, filtrado por regex, motor JS, envío HID/
   RFCOMM/plugins, etc.
5. Añadir un toggle en Ajustes para elegir modo Barcode / OCR / Ambos.
6. Si el resultado es bueno, se puede abrir un Pull Request al proyecto
   original (el README invita explícitamente a contribuir así).

## Puntos a investigar cuando se retome

- Nombre exacto del archivo/clase del analyzer de cámara en el repo actual
  (buscar en `app/src/main/java/dev/fabik/bluetoothhid/` algo relacionado
  con `Camera`/`Scanner`/`Analyzer`).
- Cómo está estructurado el pipeline de resultados para que el texto OCR
  entre por el mismo camino que un código de barras normal (para heredar
  regex, JS engine, plugins, etc. gratis).
- Impacto en batería/rendimiento de correr dos detectores por frame —
  considerar limitar el OCR a un modo explícito, no simultáneo por defecto.

## Recordatorio

Esta nota vive en este chat, no en la memoria de Claude (no está activada
en esta cuenta). Si activas "Generate memory from chat history" en
Configuración, en conversaciones futuras Claude podría recordarlo
automáticamente; si no, guarda este archivo y compártelo de nuevo cuando
quieras retomar el trabajo.
