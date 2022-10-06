package com.vjh0107.barcode.core.listeners

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftAsync
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftMain
import kotlinx.coroutines.*
import org.bukkit.Bukkit

/**
 * 월드의 시간을 2틱마다 월드의 시간에 1틱을 더하여 두배 늦춘다.
 */
@BarcodeComponent
class DayCycleTuner(private val plugin: AbstractBarcodePlugin) : BarcodeRegistrable {
    override val id: String = "DayCycleTuner"

    private lateinit var job: Job

    override fun register() {
        job = CoroutineScope(Dispatchers.MinecraftAsync(plugin)).launch CoroutineScope@{
            while(this@CoroutineScope.isActive) {
                delay(100)
                withContext(Dispatchers.MinecraftMain(plugin)) {
                    val world = Bukkit.getWorld("world") ?: throw NullPointerException("world 월드가 존재하지 않습니다.")
                    world.time += 1
                }
            }
        }
    }

    override fun unregister() {
        job.cancel()
    }
}