package com.placenote.client.api

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ApiEnvelopeTest {
    @Test
    fun decodeSuccessData_parsesUser() {
        val body = """{"status":"success","data":{"id":"u1","name":"N"}}"""
        val u = decodeSuccessData(body, UserDto.serializer())
        assertEquals("N", u.name)
        assertEquals("u1", u.id)
    }

    @Test
    fun decodeSuccessData_throwsOnErrorEnvelope() {
        val body = """{"status":"error","message":"no","code":401}"""
        val ex = assertFailsWith<ApiException> {
            decodeSuccessData(body, UserDto.serializer())
        }
        assertEquals(401, ex.apiCode)
    }
}
