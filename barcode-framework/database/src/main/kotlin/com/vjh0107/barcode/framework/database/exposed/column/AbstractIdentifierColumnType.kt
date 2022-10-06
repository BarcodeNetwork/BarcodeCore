package com.vjh0107.barcode.framework.database.exposed.column

import com.vjh0107.barcode.framework.database.player.Identifier
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.vendors.currentDialect
import java.nio.ByteBuffer
import java.util.*

abstract class AbstractIdentifierColumnType<T : Identifier> : ColumnType() {
    override fun sqlType(): String = currentDialect.dataTypeProvider.uuidType()

    companion object {
        private val uuidRegexp = Regex("[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}", RegexOption.IGNORE_CASE)
    }

    override fun notNullValueToDB(value: Any): Any = currentDialect.dataTypeProvider.uuidToDB(valueToUUID(value))

    abstract override fun valueFromDB(value: Any): T

    override fun nonNullValueToString(value: Any): String = "'${valueToUUID(value)}'"

    fun uuidValueFromDB(value: Any): UUID = when {
        value is Identifier -> value.id
        value is UUID -> value
        value is ByteArray -> ByteBuffer.wrap(value).let { b -> UUID(b.long, b.long) }
        value is String && value.matches(uuidRegexp) -> UUID.fromString(value)
        value is String -> ByteBuffer.wrap(value.toByteArray()).let { b -> UUID(b.long, b.long) }
        else -> error("uuidValueFromDB: Identifier/UUID 가 아닌 다른 값이 있습니다 -> $value of ${value::class.qualifiedName}")
    }

    private fun valueToUUID(value: Any): UUID = when (value) {
        is Identifier -> value.id
        is UUID -> value
        is String -> UUID.fromString(value)
        is ByteArray -> ByteBuffer.wrap(value).let { UUID(it.long, it.long) }
        else -> error("valueToUUID: Identifier/UUID 가 아닌 다른 값이 있습니다 -> ${value.javaClass.canonicalName}")
    }
}