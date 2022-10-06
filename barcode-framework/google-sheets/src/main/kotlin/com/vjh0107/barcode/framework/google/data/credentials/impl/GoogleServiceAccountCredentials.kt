package com.vjh0107.barcode.framework.google.data.credentials.impl

import com.google.api.client.http.HttpRequestInitializer
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.auth.Credentials
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.vjh0107.barcode.framework.google.data.credentials.BarcodeGoogleCredentials
import org.koin.core.annotation.Single
import java.io.File

/**
 * @param credentialsFile credentials json 파일
 * @param scopes 권한
 * @param accessType 온라인에서 요청하는지 오프라인에서 요청되는지 여부 installed = offline
 */
@Single
data class GoogleServiceAccountCredentials(
    val credentialsFile: File,
    val scopes: Collection<String> = listOf(SheetsScopes.SPREADSHEETS_READONLY),
    val accessType: String = "offline"
) : BarcodeGoogleCredentials {
    private val credentials: Credentials

    init {
        credentials = GoogleCredentials.fromStream(credentialsFile.inputStream()).createScoped(scopes)
    }

    override fun getHttpRequestInitializer(): HttpRequestInitializer {
        return HttpCredentialsAdapter(credentials)
    }
}