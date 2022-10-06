package com.vjh0107.barcode.framework.coroutine.service.impl

import com.vjh0107.barcode.framework.coroutine.service.WakeUpBlockService
import com.vjh0107.barcode.framework.utils.findClazz
import org.bukkit.plugin.Plugin
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.LockSupport

internal class WakeUpBlockServiceImpl(private val plugin: Plugin) : WakeUpBlockService {
    private var threadSupport: ExecutorService? = null

    private val craftSchedulerClazz by lazy {
        plugin.findClazz("org.bukkit.craftbukkit.{VERSION}.scheduler.CraftScheduler")
    }

    private val craftSchedulerTickField by lazy {
        val field = craftSchedulerClazz.getDeclaredField("currentTick")
        field.isAccessible = true
        field
    }

    private val craftSchedulerHeartBeatMethod by lazy {
        craftSchedulerClazz.getDeclaredMethod("mainThreadHeartbeat", Int::class.java)
    }

    override var primaryThread: Thread? = null

    override fun ensureWakeup() {
        if (primaryThread == null && plugin.server.isPrimaryThread) {
            primaryThread = Thread.currentThread()
        }

        if (primaryThread == null) {
            return
        }

        if (threadSupport == null) {
            threadSupport = Executors.newFixedThreadPool(1)
        }

        threadSupport!!.submit {
            val blockingCoroutine = LockSupport.getBlocker(primaryThread)

            if (blockingCoroutine != null) {
                val currentTick = craftSchedulerTickField.get(plugin.server.scheduler)
                craftSchedulerHeartBeatMethod.invoke(plugin.server.scheduler, currentTick)
            }
        }
    }

    override fun dispose() {
        threadSupport?.shutdown()
    }
}