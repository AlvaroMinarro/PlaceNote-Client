# PlaceNote-Client

Aplicación **Kotlin Multiplatform** (KMP) para **PlaceNote**: reseñas gastronómicas con enfoque offline-first y despliegue self-hosted del servidor.

## Servidor y contrato API

- Repositorio del API: **[PlaceNote-Server](https://github.com/AlvaroMinarro/PlaceNote-Server)**.
- **Versión mínima de API soportada por esta rama:** `v1` (OpenAPI `0.1.0` en el servidor). Si el servidor publica cambios incompatibles, se actualizará esta línea y el changelog.

La fuente de verdad del contrato HTTP es el archivo OpenAPI del servidor:

`PlaceNote-Server/docs/api/openapi.yaml`

El documento [docs/CONTRACT.md](docs/CONTRACT.md) resume además los **envoltorios** de respuesta (`success`/`error`), rutas canónicas frente a deprecadas y la cabecera JWT, además de la generación opcional de modelos.

## Estructura

- **`shared`**: código multiplataforma compartido (modelos, capa HTTP mínima en `com.placenote.client.api`, lógica). Incluye targets **Android** y **JVM**; el target **iOS** se puede añadir de nuevo (p. ej. `iosArm64` / `iosSimulatorArm64`) cuando el entorno Xcode/Kotlin Native esté verificado en tu máquina.
- Módulos adicionales (por ejemplo `composeApp`) se pueden añadir cuando se integre Compose Multiplatform.

## Requisitos de desarrollo

- Android Studio o IntelliJ con soporte **Kotlin Multiplatform**.
- **Android SDK** (para el target Android).
- **JDK** para el target JVM (útil para tests y CI sin SDK Android).

## Compilar el módulo `shared`

```bash
./gradlew :shared:compileKotlinJvm
./gradlew :shared:compileDebugKotlinAndroid
```

Sin Android SDK configurado, `compileDebugKotlinAndroid` fallará hasta definir `ANDROID_HOME` / Android Studio.

Tests JVM del módulo (sin Android SDK):

```bash
./gradlew :shared:jvmTest
```

En CI (GitHub Actions), cada push/PR a `main` ejecuta `:shared:jvmTest`.

## Documentación

- [Arquitectura del cliente (offline-first)](docs/architecture.md)
- [Contrato con el servidor](docs/CONTRACT.md)

## Licencia

MIT. Ver [LICENSE](LICENSE).
