package com.placenote.client.api

import com.placenote.client.createHttpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Cliente HTTP mínimo alineado con [PlaceNote-Client/docs/CONTRACT.md](docs/CONTRACT.md):
 * envoltorios bajo `/api/v1`, salvo [getHealth] en `/health`.
 */
class PlaceNoteApiClient(
    private val baseUrl: String,
    engine: HttpClientEngine = createHttpClientEngine(),
    private val json: Json = defaultApiJson,
) {
    private val client = HttpClient(engine) {
        install(ContentNegotiation) {
            json(json)
        }
    }

    private fun url(path: String) = "${baseUrl.trimEnd('/')}$path"

    suspend fun getHealth(): HealthResponse {
        val text = client.get(url("/health")).bodyAsText()
        return json.decodeFromString(text)
    }

    suspend fun getUsersMe(token: String): UserDto {
        val text = client.get(url("/api/v1/users/me")) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.bodyAsText()
        return decodeSuccessData(text, UserDto.serializer(), json)
    }
}
