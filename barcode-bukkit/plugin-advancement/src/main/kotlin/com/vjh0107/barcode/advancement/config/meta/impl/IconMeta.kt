package com.vjh0107.barcode.advancement.config.meta.impl

import com.vjh0107.barcode.advancement.services.ConditionManager
import com.vjh0107.barcode.advancement.config.meta.ItemMeta
import com.vjh0107.barcode.advancement.config.condition.ConditionData
import com.vjh0107.barcode.framework.utils.item.createGuiItem
import com.vjh0107.barcode.framework.utils.item.toBukkitMaterial
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IconMeta(
    private val itemExpression: String,
    private val conditionIcons: ConfigurationSection?
) : ItemMeta {
    private val conditionIconDataSet: MutableSet<ConditionData> = mutableSetOf()

    init {
        conditionIcons?.getKeys(false)?.mapNotNull { key ->
            val conditionList = conditionIcons.getConfigurationSection("$key.conditions")
            val description = conditionIcons
                .getString("$key.icon") ?: throw NullPointerException("value 없음")
            conditionIconDataSet.add(ConditionData.of(conditionList, description))
        }
    }

    /**
     * expression
     * <material>:<custom model data>
     *
     * example) STONE:1
     */
    private fun String.parseItemExpression(): ItemStack {
        if (!this.contains(":")) {
            return createGuiItem(this.toBukkitMaterial())
        }
        val split = this.split(":")
        return createGuiItem(split.first().toBukkitMaterial(), customModelData = split.last().toInt())
    }

    override fun getResult(player: Player) : ItemStack {
        return let {
            conditionIconDataSet.forEach {
                if (ConditionManager.evaluateCondition(player, it.conditions)) {
                    return@let it.value
                }
            }
            return@let itemExpression
        }.parseItemExpression()
    }
}