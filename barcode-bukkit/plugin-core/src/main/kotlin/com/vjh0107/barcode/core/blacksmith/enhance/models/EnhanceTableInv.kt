package com.vjh0107.barcode.core.blacksmith.enhance.models

import com.vjh0107.barcode.core.blacksmith.enhance.EnhanceBehavior
import com.vjh0107.barcode.core.blacksmith.enhance.EnhanceInfo
import com.vjh0107.barcode.core.blacksmith.enhance.EnhanceMeta
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.utils.item.createGuiItem
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.utils.formatters.toBarcodeFormat
import io.lumine.mythic.lib.api.item.NBTItem
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object EnhanceSchedulers {
    var playerSet = mutableMapOf<Player, EnhanceInfo>()
}

@BarcodeComponent
class EnhanceTableInv(private val plugin: AbstractBarcodePlugin) : BarcodeRegistrable {
    override val id: String = "EnhanceTableInv"

    override fun register() {
        plugin.schedulers.runTaskTimer(0, 2) {
            EnhanceSchedulers.playerSet.forEach outer@{ player, enhanceInfo ->
                val enhanceTable = player.openInventory.topInventory
                if(player.openInventory.title != title) {
                    EnhanceSchedulers.playerSet.remove(player)
                    return@outer
                }
                val item: ItemStack = enhanceTable.getItem(EnhanceInventorySlots.ITEM_SLOT.slotNums[0]) ?: run {
                    cantEnhanceSet(enhanceTable)
                    return@outer
                }
                enhanceInfo.apply {
                    chance = 0.0
                    destroyChance = 0.0
                    volume = 0.0
                    gold = 0.0
                    calculatedEnhanceChance = 0.0
                    this.result = null
                }
                val nbtItem: NBTItem = NBTItem.get(item)
                val mmoItem: LiveMMOItem = LiveMMOItem(item)

                if (!nbtItem.hasType()) {
                    enhanceTable.setItem(EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums[0], EnhanceItems.CANT_ENHANCE_ITEM_BUTTON.item)
                    return@outer
                }
                if (!EnhanceBehavior.isEnhanceable(nbtItem)) {
                    enhanceTable.setItem(EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums[0], EnhanceItems.CANT_ENHANCE_ITEM_BUTTON.item)
                    return@outer
                }

                val enhanceNewLevel = mmoItem.upgradeLevel + 1
                if (enhanceNewLevel > 20) {
                    enhanceTable.setItem(EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums[0], EnhanceItems.MAX_UPGRADE_REACHED.item)
                    return@outer
                }

                val itemRequiredLevel: Int = if (nbtItem.getInteger("MMOITEMS_REQUIRED_LEVEL") == 0) 1 else nbtItem.getInteger("MMOITEMS_REQUIRED_LEVEL")
                var nulledSlotSize = 0
                EnhanceInventorySlots.ENHANCE_MATERIAL_SLOT.slotNums.forEach { slotNum ->
                    if (enhanceTable.getItem(slotNum) == null) {
                        nulledSlotSize += 1
                        return@forEach
                    }

                    val nbtSlotItem = NBTItem.get(enhanceTable.getItem(slotNum))

                    val enhanceChance = (nbtSlotItem.getDouble("BARCODE_ENHANCE_CHANCE") * nbtSlotItem.item.amount.toDouble())
                    val enhanceDestroyChance = nbtSlotItem.getDouble("BARCODE_ENHANCE_DESTROY_CHANCE") * nbtSlotItem.item.amount.toDouble()
                    val enhanceVolume = nbtSlotItem.getDouble("BARCODE_ENHANCE_VOLUME") * nbtSlotItem.item.amount.toDouble()

                    if (enhanceChance == 0.0 && enhanceDestroyChance == 0.0 && enhanceVolume == 0.0) {
                        enhanceTable.setItem(EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums[0], EnhanceItems.ENHANCE_MATERIAL_IS_CORRUPTED.item)
                        return@outer
                    }

                    enhanceInfo.addChance(enhanceChance)
                    enhanceInfo.addDestroyChance(enhanceDestroyChance)
                    enhanceInfo.addVolume(enhanceVolume)
                }

                if(nulledSlotSize == 9) {
                    enhanceTable.setItem(EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums[0], EnhanceItems.ENHANCE_MATERIAL_NOT_AVAILABLE.item)
                    enhanceTable.setItem(EnhanceInventorySlots.PREVIEW_SLOT.slotNums[0], EnhanceItems.EMPTY_PREVIEW.item)
                    return@outer
                }
                val maxVolume = EnhanceMeta.getEnhanceMaxVolume(itemRequiredLevel)
                if(enhanceInfo.volume > maxVolume) {
                    enhanceTable.setItem(EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums[0], EnhanceItems.MAX_VOLUME_REACHED.item)
                    return@outer
                }

                val tier: String = nbtItem.getString("MMOITEMS_TIER")

                enhanceInfo.gold = EnhanceMeta.getEnhanceCost(itemRequiredLevel, enhanceNewLevel, tier)

                val enhanceChanceCalculate = EnhanceMeta.calculateEnhanceChance(enhanceInfo.chance, itemRequiredLevel, enhanceNewLevel)
                enhanceInfo.calculatedEnhanceChance = enhanceChanceCalculate

                val enhanceTryItem = createGuiItem(
                    Material.ANVIL, 1, "§a강화하기",
                    listOf(
                        "",
                        "  §7§o[이전 단계] §e+${mmoItem.upgradeLevel} §7-> §e+${mmoItem.upgradeLevel + 1} §7§o[다음 단계]  ",
                        "",
                        "§7강화 비용: §e${enhanceInfo.gold.toBarcodeFormat()}§f\uE163",
                        "",
                        "§7강화 성공 확률: §e${enhanceChanceCalculate.toBarcodeFormat()}% §c(${(EnhanceMeta.getUpgradeChancePenalty(itemRequiredLevel, enhanceNewLevel) * 100).toBarcodeFormat()}% 적용 중)",
                        "§7강화 실패 시 파괴 확률: §c${enhanceInfo.destroyChance.toBarcodeFormat()}%",
                        "§7강화석 부피: §f${enhanceInfo.volume.toBarcodeFormat()}/${maxVolume.toBarcodeFormat()}",
                        "",
                        "§a클릭하여 강화에 시도할 수 있습니다."
                    ), null)
                enhanceTable.setItem(EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums[0], enhanceTryItem)

                val resultItem = EnhanceBehavior.upgrade(mmoItem, mmoItem.upgradeLevel + 1)
                enhanceInfo.result = resultItem

                //set preview item
                val previewItem = resultItem.clone()
                val previewItemMeta = previewItem.itemMeta
                previewItemMeta.setDisplayName("${previewItem.itemMeta.displayName} §7(미리보기)")
                previewItemMeta.lore = previewItem.itemMeta.lore!! + listOf("", "§a강화에 성공하게 될 경우 해당 아이템을 얻습니다.")
                previewItem.itemMeta = previewItemMeta
                enhanceTable.setItem(EnhanceInventorySlots.PREVIEW_SLOT.slotNums[0], previewItem)
            }
        }
    }

    private fun cantEnhanceSet(inventory: Inventory) {
        inventory.setItem(EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums[0], EnhanceItems.DEFAULT_ENHANCE_BUTTON.item)
        inventory.setItem(EnhanceInventorySlots.PREVIEW_SLOT.slotNums[0], EnhanceItems.EMPTY_PREVIEW.item)
    }

    companion object {
        const val title = "§6강화"

        fun openEnhanceTable(player: Player) {
            EnhanceSchedulers.playerSet[player] = EnhanceInfo()
            val inventory: Inventory = Bukkit.createInventory(null, 27, title)
            EnhanceInventorySlots.values().forEach { constant ->
                constant.slotNums.forEach { slotNum ->
                    inventory.setItem(slotNum, constant.item)
                }
            }
            player.openInventory(inventory)
        }
    }
}