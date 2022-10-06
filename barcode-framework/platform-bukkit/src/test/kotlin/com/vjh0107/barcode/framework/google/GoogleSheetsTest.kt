package com.vjh0107.barcode.framework.google

import io.kotest.core.spec.style.AnnotationSpec
import java.io.File

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GoogleSheetsTest : AnnotationSpec() {
    private val resourcesPath = "src/test/resources/credentials"
    private val spreadsheetId = ""

    @Test
    fun sheetsTest() {
        val key = File(resourcesPath)
            .listFiles()
            .firstOrNull() ?: throw NullPointerException("$resourcesPath 에 credentials json key 가 없습니다.")

//        val credentials = GoogleServiceAccountCredentials(key)
//        val transport = GoogleNetHttpTransport.newTrustedTransport()
//
//        val sheetsService = GoogleServiceFactory.createSheetsService("test", credentials, transport)
//        val controller = sheetsService.controller.apply {
//            register("barcode_db", spreadsheetId)
//        }
//        runBlocking {
//            val result = controller.find("barcode_db", "레벨", "B2:B301", Dispatchers.IO)
//
//            result?.forEach {
//                it.getOrNull(0)?.print()
//            }
//        }
    }
}