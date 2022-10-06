package com.vjh0107.barcode.framework.coroutine.dispatchers

import com.vjh0107.barcode.framework.coroutine.service.WakeUpBlockService
import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

class AsyncDispatcher(
    private val plugin: Plugin,
    private val wakeUpBlockService: WakeUpBlockService
) : CoroutineDispatcher() {
    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        wakeUpBlockService.ensureWakeup()
        return plugin.server.isPrimaryThread
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!plugin.isEnabled) {
            return
        }

        plugin.server.scheduler.runTaskAsynchronously(plugin, block)
    }
}