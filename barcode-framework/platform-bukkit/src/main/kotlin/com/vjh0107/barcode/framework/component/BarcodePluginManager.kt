package com.vjh0107.barcode.framework.component

import com.vjh0107.barcode.framework.Closeable

interface BarcodePluginManager : IBarcodeComponent, Closeable {
    fun load() {}
}