package com.vjh0107.barcode.framework.ktor

import com.vjh0107.barcode.framework.utils.print
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
class RestAPITest : AnnotationSpec() {

    private val client: HttpClient = HttpClient(CIO)

    @BeforeAll
    fun setMainDispatcher() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(this.testScheduler))
    }

    @Test
    fun restApiTest() = runTest {
        val a = 10
        val response = withContext(Dispatchers.IO) {
            val url = "https://httpbin.org/get"
            client.get(url)
        }
        response.status shouldBe HttpStatusCode.OK
        a.print()
        response.bodyAsText().print()
    }

    @AfterAll
    fun tearDown() {
        Dispatchers.resetMain()
    }
}