# Contrato API entre PlaceNote-Client y PlaceNote-Server

## Fuente de verdad

El archivo **OpenAPI 3** del servidor es la referencia:

- Ruta en el repo del servidor: `docs/api/openapi.yaml`
- URL estable (sustituye el tag por un release o commit):  
  `https://raw.githubusercontent.com/AlvaroMinarro/PlaceNote-Server/<tag>/docs/api/openapi.yaml`

## Compatibilidad

El [README](../README.md) del cliente indica la **versión mínima** de API soportada. Los endpoints están bajo el prefijo `/api/v1/`.

## Generar modelos Kotlin (opcional)

Puedes usar [OpenAPI Generator](https://openapi-generator.tech/) con el generador `kotlin` o `kotlin-multiplatform` para producir DTOs a partir del YAML del servidor. Ejemplo (ajusta paths y versión del generador):

```bash
openapi-generator-cli generate \
  -i /ruta/a/PlaceNote-Server/docs/api/openapi.yaml \
  -g kotlin \
  -o ./generated \
  --additional-properties=library=multiplatform,serializationLibrary=kotlinx_serialization
```

Revisa el output y adáptalo al paquete `com.placenote.client` (o el que defináis) antes de integrarlo en `:shared`.

## Proceso manual

Mientras no haya generación automática en CI, los modelos `kotlinx.serialization` en `:shared` deben **coincidir** con los esquemas del OpenAPI (nombres de propiedades y tipos).
