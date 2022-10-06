package com.vjh0107.barcode.framework.coroutine.dispatchers

import com.vjh0107.barcode.framework.coroutine.service.WakeUpBlockService
import com.vjh0107.barcode.framework.coroutine.utils.CoroutineTimings
import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

/**
 * 마인크래프트 메인 쓰레드 디스패처이다.
 */
class MainDispatcher(
    private val plugin: Plugin,
    private val wakeUpBlockService: WakeUpBlockService
) : CoroutineDispatcher() {
    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        wakeUpBlockService.ensureWakeup()
        return !plugin.server.isPrimaryThread
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!plugin.isEnabled) {
            return
        }

        val timedRunnable = context[CoroutineTimings.Key]

        if (timedRunnable == null) {
            plugin.server.scheduler.runTask(plugin, block)
            return
        }

        timedRunnable.queue.add(block)
        plugin.server.scheduler.runTask(plugin, timedRunnable)
    }
}