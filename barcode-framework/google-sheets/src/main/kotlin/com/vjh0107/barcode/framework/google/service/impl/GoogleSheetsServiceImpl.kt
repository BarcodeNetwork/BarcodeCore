package com.vjh0107.barcode.framework.google.service.impl

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.vjh0107.barcode.framework.exceptions.DataNotFoundException
import com.vjh0107.barcode.framework.google.data.credentials.BarcodeGoogleCredentials
import com.vjh0107.barcode.framework.google.service.GoogleSheetService
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import kotlin.coroutines.CoroutineContext

@Single
class GoogleSheetsServiceImpl(
    @Property("applicationName") applicationName: String,
    credentials: BarcodeGoogleCredentials,
    transport: NetHttpTransport
) : GoogleSheetService {
    private val sheets = Sheets.Builder(transport, GsonFactory.getDefaultInstance(), credentials.getHttpRequestInitializer())
        .setApplicationName(applicationName)
        .build()

    private val registeredSheets: MutableMap<String, String> = mutableMapOf()

    override fun register(id: String, sheetsId: String) {
        registeredSheets[id] = sheetsId
    }

    override fun getSheetsId(id: String): String {
        return registeredSheets[id] ?: throw DataNotFoundException(id)
    }

    override suspend fun find(
        id: String,
        sheetsName: String,
        range: String,
        dispatcher: CoroutineContext
    ): Collection<List<Any>>? {
        val response = withContext(dispatcher) {
            runCatching {
                sheets.spreadsheets().values().get(getSheetsId(id), "$sheetsName!$range").execute()
            }
        }
        return response.getOrNull()?.getValues()
    }
}