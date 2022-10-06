package com.vjh0107.barcode.framework.coroutine.extensions

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.coroutine.CoroutineComponent
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * 마인크래프트 메인쓰레드 디스패처를 구합니다.
 */
fun Dispatchers.MinecraftMain(plugin: AbstractBarcodePlugin): CoroutineContext {
    return CoroutineComponent.instance.getCoroutinePlugin(plugin).mainDispatcher
}

/**
 * 비동기 디스패처를 구합니다. 비동기 버킷 스케줄러 쓰레드가 사용됩니다.
 */
fun Dispatchers.MinecraftAsync(plugin: AbstractBarcodePlugin): CoroutineContext {
    return CoroutineComponent.instance.getCoroutinePlugin(plugin).asyncDispatcher
}

/**
 * 데이터베이스를 사용할때 쓸 디스패처입니다.
 */
fun Dispatchers.Database(plugin: AbstractBarcodePlugin): CoroutineContext {
    return Dispatchers.IO
}
