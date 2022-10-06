package com.vjh0107.barcode.framework.coroutine.plugin

import com.vjh0107.barcode.framework.coroutine.service.WakeUpBlockService
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface CoroutinePlugin {

    /**
     * 마인크래프트 코루틴 스코프 입니다.
     */
    val scope: CoroutineScope

    val wakeUpBlockService: WakeUpBlockService

    val mainDispatcher: CoroutineContext

    val asyncDispatcher: CoroutineContext
}