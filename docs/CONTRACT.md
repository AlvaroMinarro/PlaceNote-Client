# Contrato API entre PlaceNote-Client y PlaceNote-Server

## Fuente de verdad

El archivo **OpenAPI 3** del servidor es la referencia:

- Ruta en el repo del servidor: `docs/api/openapi.yaml`
- URL estable (sustituye `<tag>` por un release, tag o SHA de commit):  
  `https://raw.githubusercontent.com/AlvaroMinarro/PlaceNote-Server/<tag>/docs/api/openapi.yaml`

## Compatibilidad

El [README](../README.md) del cliente indica la **versión mínima** de API soportada. Los endpoints están bajo el prefijo `/api/v1/`.

## Autenticación

Las rutas protegidas esperan JWT **stateless** en la cabecera:

```http
Authorization: Bearer <access_token>
```

Los tokens se obtienen con `POST /api/v1/auth/login` o al registrarse (`POST /api/v1/auth/register`); el cuerpo de éxito incluye el token dentro de `data` (véase envoltorios más abajo).

## Envoltorios de respuesta (obligatorio para clientes y bots)

Salvo la excepción de salud, **no** se devuelve JSON de negocio “pelado”: el servidor usa un contrato fijo para que app KMP, bots (p. ej. Telegram) y herramientas traten errores de forma uniforme.

### Éxito (`2xx`)

```json
{
  "status": "success",
  "data": { }
}
```

El payload real del endpoint va en `data`. Los esquemas del OpenAPI (`SuccessEnvelope*`) describen la forma completa incluyendo `status` y el tipo de `data`.

### Error

```json
{
  "status": "error",
  "message": "Texto legible para el usuario o logs",
  "code": 404
}
```

El campo `code` suele coincidir con el código HTTP de la respuesta; conviene usarlo junto con el status line HTTP al implementar reintentos o mensajes.

### Excepción: salud del proceso

`GET /health` **no** usa envoltorio: devuelve directamente un objeto pequeño (p. ej. `status` y `service`), pensado para balanceadores y comprobaciones ligeras.

## Rutas canónicas (recomendadas para nuevo código)

| Dominio | Uso |
|--------|-----|
| Usuario | `GET` / `PATCH` `/api/v1/users/me` |
| Amigos | `GET` `/api/v1/friends`, `POST` `/api/v1/friends/request`, `PUT` `/api/v1/friends/accept/{id}` |
| Ratings | `POST` `/api/v1/reviews/{id}/ratings` (upsert de tu nota) |
| Sync pull | query `last_sync` (ISO-8601); el servidor acepta también el alias `since` |

El servidor puede seguir ofreciendo rutas antiguas (`/me`, `/friendships/...`, `PUT` en ratings) como **compatibilidad**; en OpenAPI aparecen como `deprecated`. No dependas de ellas en código nuevo.

## Generar modelos Kotlin (opcional)

Puedes usar [OpenAPI Generator](https://openapi-generator.tech/) con el generador `kotlin` o `kotlin-multiplatform` para producir DTOs a partir del YAML del servidor. Ejemplo (ajusta paths y versión del generador):

```bash
openapi-generator-cli generate \
  -i /ruta/a/PlaceNote-Server/docs/api/openapi.yaml \
  -g kotlin \
  -o ./generated \
  --additional-properties=library=multiplatform,serializationLibrary=kotlinx_serialization
```

Revisa el output y adáptalo al paquete `com.placenote.client` (o el que defináis) antes de integrarlo en `:shared`. Ten en cuenta que necesitarás **tipos o genéricos** para `data` en los envoltorios de éxito si el generador no los modela como `JsonElement` o clases por operación.

## Proceso manual

Mientras no haya generación automática en CI, los modelos `kotlinx.serialization` en `:shared` deben **coincidir** con los esquemas del OpenAPI (nombres de propiedades y tipos), incluyendo:

- `@SerialName` donde el JSON use `snake_case` (p. ej. `user_id` en cuerpos de amistad).
- DTOs de envoltorio con `status` + `data` para respuestas bajo `/api/v1/`, y `status` + `message` + `code` para errores.

## Implementación en `:shared` (referencia)

El módulo `shared` incluye utilidades y un cliente Ktor en `com.placenote.client.api`:

- **Parsing:** `defaultApiJson`, `decodeSuccessData`, `parseSuccessEnvelope`, `ApiException`.
- **`PlaceNoteApiClient`** (`shared/.../PlaceNoteApiClient.kt`): `getHealth()` (sin envoltorio); `register` / `login`; `getUsersMe`; `listReviews` / `getReview` / `createReview` (JWT en `Authorization: Bearer`).

Amplía DTOs y métodos según el OpenAPI (ratings, friends, sync, etc.) cuando integres pantallas o el bot.
