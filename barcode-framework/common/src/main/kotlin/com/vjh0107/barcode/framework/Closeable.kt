package com.vjh0107.barcode.framework

/**
 * 가비지 컬렉팅이 될때 unregister 가 필요할 때 사용합시다.
 */
interface Closeable {
    /**
     * 가비지 컬렉팅이 되기 전 꼭 실행해주세요.
     */
    fun close() {}
}