package com.placenote.client.api

import com.placenote.client.createHttpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Cliente HTTP alineado con CONTRACT.md: envoltorios bajo `/api/v1`, salvo [getHealth] en `/health`.
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

    suspend fun register(request: RegisterRequest): AuthResponse {
        val text = client.post(url("/api/v1/auth/register")) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyAsText()
        return decodeSuccessData(text, AuthResponse.serializer(), json)
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        val text = client.post(url("/api/v1/auth/login")) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyAsText()
        return decodeSuccessData(text, AuthResponse.serializer(), json)
    }

    suspend fun getUsersMe(token: String): UserDto {
        val text = client.get(url("/api/v1/users/me")) {
            bearer(token)
        }.bodyAsText()
        return decodeSuccessData(text, UserDto.serializer(), json)
    }

    suspend fun listReviews(token: String, limit: Int? = null): ReviewPage {
        val text = client.get(url("/api/v1/reviews")) {
            bearer(token)
            limit?.let { parameter("limit", it) }
        }.bodyAsText()
        return decodeSuccessData(text, ReviewPage.serializer(), json)
    }

    suspend fun getReview(token: String, reviewId: String): ReviewDto {
        val text = client.get(url("/api/v1/reviews/$reviewId")) {
            bearer(token)
        }.bodyAsText()
        return decodeSuccessData(text, ReviewDto.serializer(), json)
    }

    suspend fun createReview(token: String, body: ReviewCreate): ReviewDto {
        val text = client.post(url("/api/v1/reviews")) {
            bearer(token)
            contentType(ContentType.Application.Json)
            setBody(body)
        }.bodyAsText()
        return decodeSuccessData(text, ReviewDto.serializer(), json)
    }

    private fun HttpRequestBuilder.bearer(token: String) {
        header(HttpHeaders.Authorization, "Bearer $token")
    }
}
