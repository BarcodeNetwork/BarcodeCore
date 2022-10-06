package com.vjh0107.barcode.core.meister.gui.impl

import com.vjh0107.barcode.core.meister.models.MeisterSkills
import com.vjh0107.barcode.core.meister.models.MeisterMessages
import com.vjh0107.barcode.framework.utils.item.createGuiItem
import com.vjh0107.barcode.framework.inventory.AbstractBarcodeInventory
import com.vjh0107.barcode.framework.inventory.BarcodeInventory
import com.vjh0107.barcode.framework.utils.formatters.colorize
import com.vjh0107.barcode.framework.utils.item.models.BarcodeGuiItems
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.koin.core.annotation.InjectedParam

class MeisterSkillInventoryImpl(
    @InjectedParam override val player: Player
    ) : AbstractBarcodeInventory() {
    private val reallocateItem = object : BarcodeInventory.SlotItem {
        override val slots = listOf(8)
        override fun get(): ItemStack {
            return createGuiItem(
                Material.IRON_SHOVEL, 1, "§c전문기술 스킬 초기화",
                listOf(
                    "",
                    "&7현재 &6${data.savableDataObject.meisterSkill.getAllSkillLevel()}개&7의 포인트가 투자되어 있습니다.",
                    "&7우클릭시 투자된 모든 포인트를 회수합니다.",
                    "",
                    "&e◆ 보유중인 전문기술 스킬 초기화 포인트 : &6${data.savableDataObject.meisterSkill.resetPoints}"
                ).map { it.colorize() },
                8
            )
        }
    }

    val size: Int = 18
    val name: String = "&6전문기술 스킬".colorize()

    override fun getInventory(): Inventory {
        val inventory = createInventory(size, name)
        MeisterSkills.map.forEach { (index, it) ->
            inventory.setItem(index, it.getItem(data))
        }
        inventory.setItems(reallocateItem)
        repeat(size) {
            inventory.getItem(it) ?: inventory.setItem(it, BarcodeGuiItems.EMPTY.item)
        }
        return inventory
    }


    override fun whenClickedTop(event: InventoryClickEvent) {
        event.isCancelled = true
        val clickedInventory = event.clickedInventory ?: return
        if (clickedInventory != event.inventory) return

        MeisterSkills.map[event.slot]?.let {
            clickOnSkills(it)
            return
        }

        if (event.slot == reallocateItem.slots.first()) {
            clickOnReallocateItem()
            return
        }
    }

    private fun clickOnSkills(skill: MeisterSkills) {
        if (!skill.validatePlayer(player)) {
            MeisterMessages.Fail.NOT_YET_LEARNED.send(player)
            return
        }
        if (data.savableDataObject.meisterSkill.skillPoints < 1) {
            MeisterMessages.Fail.SKILL_POINT_REQUIRED.send(player)
            return
        }
        if (skill.maxLevel <= data.savableDataObject.meisterSkill.getSkillLevel(skill)) {
            MeisterMessages.Fail.MAX_LEVEL_REACHED.send(player)
            return
        }

        data.savableDataObject.meisterSkill.upgradeSkillLevel(skill)
        data.savableDataObject.meisterSkill.skillPoints -= 1
        MeisterMessages.Success.SKILL_LEVEL_UP.send(player, skill, data.savableDataObject.meisterSkill.getSkillLevel(skill))
        open()
    }

    private fun clickOnReallocateItem() {
        if (data.savableDataObject.meisterSkill.getAllSkillLevel() < 1) {
            MeisterMessages.Fail.NO_SKILL_POINTS_SPENT.send(player)
            return
        }
        if (data.savableDataObject.meisterSkill.resetPoints >= 1) {
            data.savableDataObject.meisterSkill.run {
                resetPoints -= 1
                resetSkills()
            }
            MeisterMessages.Success.SKILLS_REALLOCATED.send(player)
            open()
            return
        } else {
            MeisterMessages.Fail.SKILL_RESET_POINT_REQUIRE.send(player)
            return
        }
    }
}