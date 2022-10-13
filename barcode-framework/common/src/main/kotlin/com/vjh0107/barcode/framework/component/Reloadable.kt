package com.vjh0107.barcode.framework.component

/**
 * 컴포넌트가 만약에 reloadable 이면, AbstractBarcodePlugin 이 reloadPlugin 할때
 * close() 후 load() 가 된다.
 */
interface Reloadable {
    fun load() {}
    fun close() {}
}