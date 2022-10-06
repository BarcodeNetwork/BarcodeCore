package com.vjh0107.barcode.framework.coroutine.extensions

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.coroutine.CoroutineComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.getFromPlugin(plugin: AbstractBarcodePlugin): CoroutineScope {
    return CoroutineComponent.instance.getCoroutinePlugin(plugin).scope
}

fun CoroutineContext.launchScope(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) : Job {
    return CoroutineScope(this).launch(context, start, block)
}