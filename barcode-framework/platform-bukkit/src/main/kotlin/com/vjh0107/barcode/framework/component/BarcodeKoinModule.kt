package com.vjh0107.barcode.framework.component

import org.koin.core.module.Module

interface BarcodeKoinModule : IBarcodeComponent {
    /**
     * 코인 모듈 입니다. 자동으로 load, unload 됩니다.
     */
    val targetModule: Module
}