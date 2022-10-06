package com.vjh0107.barcode.framework.google.service

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * 구글 시트 서비스입니다.
 */
interface GoogleSheetService {

    fun register(id: String, sheetsId: String)

    fun getSheetsId(id: String): String

    /**
     * @param range 셀의 범위 ex) A1:B4 이렇게 적는다.
     */
    suspend fun find(
        id: String,
        sheetsName: String,
        range: String,
        dispatcher: CoroutineContext = Dispatchers.IO
    ): Collection<List<Any>>?
}