package com.vjh0107.barcode.framework.google.data.credentials

import com.google.api.client.http.HttpRequestInitializer

/**
 * 구글 API credential 을 credentials json 파일로부터 가져오는 역할을 합니다.
 */
interface BarcodeGoogleCredentials {
    /**
     * 구글 HttpRequestInitializer 을 가져옵니다.
     */
    fun getHttpRequestInitializer(): HttpRequestInitializer
}