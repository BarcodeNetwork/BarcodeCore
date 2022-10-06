package com.vjh0107.barcode.framework.database.exposed.extensions

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * json 칼럼입니다.
 *
 * json 칼럼은 mysql 데이터베이스 버전(추정)에 따라 기본값 적용이 되지 않을 수 있으므로,
 * 클라이언트에서 기본값을 받습니다.
 */
fun Table.json(name: String, collate: String? = null, eagerLoading: Boolean = false): Column<String> {
    return text(name, collate, eagerLoading).clientDefault { "{}" }
}

