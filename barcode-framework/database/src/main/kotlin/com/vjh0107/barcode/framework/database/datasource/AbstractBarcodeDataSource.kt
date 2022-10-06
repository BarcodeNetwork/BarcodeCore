package com.vjh0107.barcode.framework.database.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

abstract class AbstractBarcodeDataSource : BarcodeDataSource {
    override val database: Database by lazy { Database.connect(this.dataSource) }

    override suspend fun <T> query(block: suspend (Transaction) -> T): T {
        return query(Dispatchers.IO, block)
    }

    override suspend fun <T> query(dispatcher: CoroutineDispatcher, block: suspend (Transaction) -> T): T {
        return newSuspendedTransaction(dispatcher, database) {
            block(this@newSuspendedTransaction)
        }
    }
}