package com.vjh0107.barcode.framework.coroutine.plugin.impl

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.coroutine.dispatchers.AsyncDispatcher
import com.vjh0107.barcode.framework.coroutine.dispatchers.MainDispatcher
import com.vjh0107.barcode.framework.coroutine.events.CoroutineExceptionEvent
import com.vjh0107.barcode.framework.coroutine.plugin.CoroutinePlugin
import com.vjh0107.barcode.framework.coroutine.service.WakeUpBlockService
import com.vjh0107.barcode.framework.coroutine.service.impl.WakeUpBlockServiceImpl
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.logging.Level
import kotlin.coroutines.CoroutineContext

class CoroutinePluginImpl(private val plugin: AbstractBarcodePlugin) : CoroutinePlugin {
    override val wakeUpBlockService: WakeUpBlockService by lazy {
        WakeUpBlockServiceImpl(plugin)
    }

    override val mainDispatcher: CoroutineContext by lazy {
        MainDispatcher(plugin, wakeUpBlockService)
    }

    override val asyncDispatcher: CoroutineContext by lazy {
        AsyncDispatcher(plugin, wakeUpBlockService)
    }

    override val scope: CoroutineScope

    init {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            val coroutineExceptionEvent = CoroutineExceptionEvent(plugin, exception)

            if (plugin.isEnabled) {
                plugin.server.scheduler.runTask(plugin, Runnable {
                    plugin.server.pluginManager.callEvent(coroutineExceptionEvent)

                    if (!coroutineExceptionEvent.isCancelled) {
                        if (exception !is CancellationException) {
                            plugin.logger.log(Level.SEVERE, "코루틴 내에서 오류가 발생하였으나, 바코드 코루틴 디스패처의 문제가 아닙니다.", exception)
                        }
                    }
                })
            }
        }

        val rootCoroutineScope = CoroutineScope(exceptionHandler)

        // 마인크래프트 scope 는 plugin scope 와 SupervisorJob 의 자식이다.
        // SupervisorJob 의 자식은 독립적이다.
        scope = rootCoroutineScope + SupervisorJob() + mainDispatcher
    }
}