package com.placenote.client.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ApiException(
    val apiCode: Int,
    message: String,
) : Exception(message)

val defaultApiJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun parseSuccessEnvelope(body: String, json: Json = defaultApiJson) =
    json.parseToJsonElement(body).jsonObject.let { root ->
        when (root["status"]?.jsonPrimitive?.content) {
            "success" -> root
            "error" -> {
                val msg = root["message"]?.jsonPrimitive?.content ?: "error"
                val code = root["code"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0
                throw ApiException(code, msg)
            }
            else -> throw IllegalArgumentException("Respuesta API inválida: falta status success")
        }
    }

fun <T> decodeSuccessData(
    body: String,
    deserializer: KSerializer<T>,
    json: Json = defaultApiJson,
): T {
    val root = parseSuccessEnvelope(body, json)
    val data = root["data"] ?: throw IllegalArgumentException("Respuesta success sin data")
    return json.decodeFromJsonElement(deserializer, data)
}
