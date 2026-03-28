# Arquitectura del cliente (PlaceNote)

## Objetivo

La app debe ser **usable sin conexión**: crear y editar reseñas y puntuaciones en almacenamiento local (Room o SQLDelight en KMP), registrar operaciones en una cola de sincronización y enviarlas al **PlaceNote-Server** cuando haya red.

## OCR

La extracción de datos de tickets se hace **en el dispositivo** (ML Kit en Android, Vision en iOS) mediante `expect`/`actual`. Solo el texto u datos ya interpretados viajan al servidor.

## Módulo `shared`

Contiene lógica, modelos y una capa HTTP inicial (`com.placenote.client.api`, Ktor) que parsea envoltorios; ampliar DTOs y llamadas según pantallas. La red consumirá los mismos DTOs que describe el OpenAPI del servidor; ver [CONTRACT.md](CONTRACT.md).

## Próximos pasos previstos

- Persistencia local y esquema alineado a UUID generados en cliente.
- Worker o corrutinas para `sync/push` y `sync/pull`.
- UI con Compose Multiplatform cuando se añada el módulo de aplicación.
