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
