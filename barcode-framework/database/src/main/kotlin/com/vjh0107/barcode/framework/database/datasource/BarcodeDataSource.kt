package com.vjh0107.barcode.framework.database.datasource

import com.vjh0107.barcode.framework.Closeable
import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import java.sql.Connection
import javax.sql.DataSource

interface BarcodeDataSource : Closeable {
    /**
     * javax.sql 표준 DataSource
     */
    val dataSource: DataSource

    /**
     * Exposed Database 를 DataSource 와 연결합니다.
     * transaction 에 인자로 넣어서 사용해야함.
     */
    val database: Database

    fun isClosed() : Boolean

    /**
     * 커넥션 가져오기
     *
     * @return 커넥션
     */
    fun getConnection(): Connection

    // ***********************************************************************
    //                          코루틴 suspend
    // ***********************************************************************

    /**
     * CRUD 를 꼭 이 메소드를 통해서만 진행해주세요.
     *
     * @param block 쿼리
     * @return query 로 가져온 ResultSet 등
     */
    suspend fun <T> query(block: suspend (Transaction) -> T) : T
    suspend fun <T> query(dispatcher: CoroutineDispatcher, block: suspend (Transaction) -> T) : T

    // suspend fun finish
}