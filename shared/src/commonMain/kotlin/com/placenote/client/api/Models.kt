package com.placenote.client.api

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
    val service: String,
)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val tokenType: String = "Bearer",
    val expiresIn: Long? = null,
)

@Serializable
data class ReviewDto(
    val id: String,
    val nombreSitio: String,
    val latitud: Double,
    val longitud: Double,
    val fotoUrl: String? = null,
    val ocrTextoCrudo: String? = null,
    val precioTotal: Double? = null,
    val fechaVisita: String,
    val creadorId: String,
    val modifiedAt: String? = null,
)

@Serializable
data class ReviewCreate(
    val id: String,
    val nombreSitio: String,
    val latitud: Double,
    val longitud: Double,
    val fotoUrl: String? = null,
    val ocrTextoCrudo: String? = null,
    val precioTotal: Double? = null,
    val fechaVisita: String,
)

@Serializable
data class ReviewPage(
    val items: List<ReviewDto>,
    val nextCursor: String? = null,
)
