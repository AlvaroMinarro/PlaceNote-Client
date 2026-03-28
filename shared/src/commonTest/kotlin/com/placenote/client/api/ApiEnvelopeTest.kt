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

    @Test
    fun decodeSuccessData_parsesAuthResponse() {
        val body =
            """{"status":"success","data":{"accessToken":"tok","tokenType":"Bearer","expiresIn":3600}}"""
        val auth = decodeSuccessData(body, AuthResponse.serializer())
        assertEquals("tok", auth.accessToken)
        assertEquals("Bearer", auth.tokenType)
    }

    @Test
    fun decodeSuccessData_parsesReviewPage() {
        val body =
            """{"status":"success","data":{"items":[],"nextCursor":null}}"""
        val page = decodeSuccessData(body, ReviewPage.serializer())
        assertEquals(0, page.items.size)
    }
}
