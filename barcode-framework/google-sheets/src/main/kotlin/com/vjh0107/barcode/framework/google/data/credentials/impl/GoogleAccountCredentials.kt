package com.vjh0107.barcode.framework.google.data.credentials.impl

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.SheetsScopes
import com.vjh0107.barcode.framework.google.data.credentials.BarcodeGoogleCredentials
import java.io.File

/**
 * @param credentials credentials json 파일
 * @param port 리시버 포트
 * @param userId 유저아이디
 * @param scopes 권한
 * @param accessType 온라인에서 요청하는지 오프라인에서 요청되는지 여부 installed = offline
 */
@Deprecated("이거 개인 구글아이디 사용해야 하기 때문에 유지보수가 힘들 가능성이 있음 대신 ServiceAccountCredentials 사용합시다.")
data class GoogleAccountCredentials(
    val credentials: File,
    val port: Int,
    val userId: String,
    val transport: NetHttpTransport,
    val dataStorePath: File,
    val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance(),
    val scopes: Collection<String> = listOf(SheetsScopes.SPREADSHEETS_READONLY),
    val accessType: String = "offline"
) : BarcodeGoogleCredentials {
    private val credential: Credential

    init {
        val clientSecrets = GoogleClientSecrets.load(jsonFactory, credentials.inputStream().reader())
        val flow = GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory, clientSecrets, scopes)
            .setDataStoreFactory(FileDataStoreFactory(dataStorePath))
            .setAccessType(accessType)
            .build()
        val receiver = LocalServerReceiver.Builder()
            .setPort(port)
            .build()
        credential = AuthorizationCodeInstalledApp(flow, receiver).authorize(userId)
    }

    override fun getHttpRequestInitializer(): Credential {
        return credential
    }
}