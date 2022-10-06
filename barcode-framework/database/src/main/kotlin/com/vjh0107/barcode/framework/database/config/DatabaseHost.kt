package com.vjh0107.barcode.framework.database.config

data class DatabaseHost(
    val address: String,
    val port: String,
    val user: String,
    val password: String,
    val databaseName: String
)
