package com.vjh0107.barcode.advancement.services

import com.vjh0107.barcode.advancement.BarcodeAdvancementPlugin
import com.vjh0107.barcode.advancement.api.condition.AdvancementCondition
import com.vjh0107.barcode.advancement.api.events.AdvancementConditionLoadEvent
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import com.vjh0107.barcode.framework.component.Reloadable
import com.vk2gpz.jsengine.JSEngine
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import javax.script.ScriptEngine

@BarcodeComponent
class ConditionManager(val plugin: BarcodeAdvancementPlugin) : BarcodePluginManager, Reloadable {
    init {
        plugin.conditionManager = this
    }
    companion object {
        val SCRIPT_ENGINE: ScriptEngine = JSEngine.getEngine()

        fun evaluateCondition(player: Player, conditions: ConfigurationSection?): Boolean {
            BarcodeAdvancementPlugin.instance.conditionManager.getAllConditions().forEach {
                if (!it.evaluateAll(player, conditions)) {
                    return false
                }
            }
            return true
        }
    }

    private val conditions: MutableSet<AdvancementCondition> = mutableSetOf()

    fun getAllConditions() : Set<AdvancementCondition> {
        return conditions
    }

    private fun registerCondition(condition: AdvancementCondition) : String {
        conditions.add(condition)
        return "${condition.javaClass.simpleName} 이(가) 성공적으로 로드되었습니다."
    }

    override fun close() {
        conditions.clear()
    }

    override fun load() {
        BarcodeAdvancementPlugin.runTaskLater(1) {
            val called = AdvancementConditionLoadEvent()
            Bukkit.getPluginManager().callEvent(called)
            called.conditions.forEach {
                plugin.logger.info(registerCondition(it))
            }
        }
    }
}