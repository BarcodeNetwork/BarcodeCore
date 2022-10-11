package com.vjh0107.barcode.framework.component

import com.vjh0107.barcode.framework.Closeable

/**
 * @see BarcodeRegistrableHandler
 */
interface BarcodeRegistrable : IBarcodeComponent, Closeable {
    /**
     * BarcodeRegistrar 의 id, debug 용
     */
    val id: String get() = this.javaClass.simpleName

    /**
     * Registrar 들이 register 되기 전에 실행됨
     */
    fun onLoad() {}

    /**
     * UnRegistrar 들이 unregister 되기 전에 실행됨
     */
    override fun close() {}
}