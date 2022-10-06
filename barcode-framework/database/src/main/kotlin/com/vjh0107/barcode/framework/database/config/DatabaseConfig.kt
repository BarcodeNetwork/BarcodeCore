package com.vjh0107.barcode.framework.database.config

interface DatabaseConfig<CONFIG> {
    /**
     * 데이터베이스 설정 setter
     */
    fun setProperty(key: String, value: String)

    /**
     * 데이터베이스 설정 가져오기 getter
     */
    fun getProperty(key: String) : String?

    /**
     * 데이터베이스 설정 initializer
     */
    fun initDefaultProperties()

    /**
     * Database Config Getter
     */
    fun get() : CONFIG
}