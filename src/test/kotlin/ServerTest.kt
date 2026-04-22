package com.example

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.*

class ServerTest {

    @Test
    fun `test root endpoint`() = testApplication {
        configure()

        assertEquals(HttpStatusCode.OK, client.get("/").status)
        assertEquals("Hello, Ktor!", client.get("/").bodyAsText())
    }

    @Test
    fun `test foo endpoint`() = testApplication {
        configure()

        assertEquals(HttpStatusCode.OK, client.get("/foo").status)
        assertEquals("html", client.get("/foo").contentType()?.contentSubtype)
        assertContains(client.get("/foo").bodyAsText(), "Hello, Ktor!")
    }

    @Test
    fun `test content endpoint`() = testApplication {
        configure()

        assertEquals(HttpStatusCode.OK, client.get("/content").status)
        assertEquals("html", client.get("/content").contentType()?.contentSubtype)
        assertContains(client.get("/content").bodyAsText(), "This page is built with:")
    }

    @Test
    fun `tasks can be found by priority` () = testApplication {
        configure()

        val response = client.get("/tasks/byPriority/Medium")
        val body = response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(body, "草刈り、除草剤を撒く")
        assertContains(body, "Kotlinの学習")
    }

    @Test
    fun `invalid priority produces 400` () = testApplication {
        configure()

        val response = client.get("/tasks/byPriority/Invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `unused priority produces 404` () = testApplication {
        configure()

        val response = client.get("/tasks/byPriority/Vital")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
