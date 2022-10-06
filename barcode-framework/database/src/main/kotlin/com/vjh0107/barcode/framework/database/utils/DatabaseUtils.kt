package com.vjh0107.barcode.framework.database.utils

import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import com.vjh0107.barcode.framework.utils.formatters.toBarcodeFormat
import com.vjh0107.barcode.framework.utils.logTimeSpent
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.logging.Logger

fun BarcodeDataSource.syncTablesWithDatabase(vararg tables: Table, logger: Logger) {
    transaction(this.database) {
        db.dialect.resetCaches()
        val createStatements = logger.logTimeSpent("테이블 ${tables.map { it.tableName }.toBarcodeFormat()} (들)을 생성을 준비합니다...") {
            SchemaUtils.createStatements(*tables)
        }
        logger.logTimeSpent("테이블 생성을 실행 후 커밋합니다.") {
            this.execStatements(createStatements)
            commit()
        }

        val alterStatements = logger.logTimeSpent("Alter 를 준비합니다.") {
            SchemaUtils.addMissingColumnsStatements(tables = tables, true)
        }
        logger.logTimeSpent("Alter 를 실행 후 커밋합니다.") {
            execStatements(alterStatements)
            commit()
        }
        val executedStatements = createStatements + alterStatements
        logger.logTimeSpent("테이블 매핑의 일관성을 검사 후 일관되지 않은 칼럼을 동기화합니다.") {
            val modifyTablesStatements = SchemaUtils
                .checkMappingConsistence(tables = tables, true)
                .filter { it !in executedStatements }
            execStatements(modifyTablesStatements)
            commit()
        }
        db.dialect.resetCaches()
    }
}

/**
 * Statements 를 한번에 실행합니다.
 */
fun Transaction.execStatements(statements: List<String>, inBatch: Boolean = false) {
    if (inBatch) {
        execInBatch(statements)
    } else {
        for (statement in statements) {
            exec(statement)
        }
    }
}