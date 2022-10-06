package com.vjh0107.barcode.framework.database.config

import com.vjh0107.barcode.framework.LoggerProvider

interface DatabaseHostProvider : LoggerProvider {
    /**
     * 데이터베이스 호스트를 반환합니다.
     */
    fun getDatabaseHost(): DatabaseHost
}