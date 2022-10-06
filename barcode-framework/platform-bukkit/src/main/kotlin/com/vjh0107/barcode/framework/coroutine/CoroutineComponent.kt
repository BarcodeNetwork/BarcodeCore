package com.vjh0107.barcode.framework.coroutine

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import com.vjh0107.barcode.framework.coroutine.plugin.CoroutinePlugin
import com.vjh0107.barcode.framework.coroutine.plugin.impl.CoroutinePluginImpl
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider

@BarcodeComponent
class CoroutineComponent : BarcodePluginManager {
    private val pluginSessions: MutableMap<AbstractBarcodePlugin, CoroutinePlugin> = mutableMapOf()

    @InjectInstance
    companion object : InstanceProvider<CoroutineComponent> {
        override lateinit var instance: CoroutineComponent
    }

    /**
     * 플러그인 코루틴 세션을 구합니다.
     */
    fun getCoroutinePlugin(plugin: AbstractBarcodePlugin): CoroutinePlugin {
        if (!pluginSessions.containsKey(plugin)) {
            pluginSessions[plugin] = CoroutinePluginImpl(plugin)
        }

        return pluginSessions[plugin]!!
    }

    /**
     * 플러그인 코루틴 세션을 마칩니다.
     */
    fun removeCoroutinePlugin(plugin: AbstractBarcodePlugin) {
        pluginSessions[plugin]?.let {
            it.wakeUpBlockService.dispose()
            pluginSessions.remove(plugin)
        }
    }

    override fun close() {
        pluginSessions.forEach {
            // BarcodeFramework 플러그인이 꺼질때 리스너에서 미쳐 처리하지 못한 세션을 전부 꺼준다.
            removeCoroutinePlugin(it.key)
        }
    }
}