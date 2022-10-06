package com.vjh0107.barcode.framework.component

import com.vjh0107.barcode.framework.Closeable

/**
 * 바코드 네트워크 Bukkit Repository 입니다. BarcodeRepository 는 자동으로 플레이어의 정보를 로드/저장합니다.
 */
interface BarcodeRepository : IBarcodeComponent, Closeable {
    /**
     * DataSource 를 통해 테이블을 생성하는 등의 초기화작업을 합니다.
     */
    fun load()

    /**
     * DataSource 를 닫는 등의 teardown 행동을 합니다.
     */
    override fun close()
}