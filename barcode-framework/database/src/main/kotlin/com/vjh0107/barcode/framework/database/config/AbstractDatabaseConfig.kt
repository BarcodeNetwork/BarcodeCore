package com.vjh0107.barcode.framework.database.config

/**
 * @param T getter 가 반환할 값 T를 정해줍니다. ex) HikariConfig
 */
abstract class AbstractDatabaseConfig<CONFIG> : DatabaseConfig<CONFIG> {
    val properties: MutableMap<String, String> = mutableMapOf()

    init {
        this.initDefaultProperties()
    }

    override fun setProperty(key: String, value: String) {
        properties[key] = value
    }

    override fun getProperty(key: String): String? {
        return properties[key]
    }
}