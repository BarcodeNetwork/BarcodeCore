package com.vjh0107.barcode.framework.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class GoogleSheetsModule {
    @Single
    fun provideTransport(): HttpTransport {
        return GoogleNetHttpTransport.newTrustedTransport()
    }
}