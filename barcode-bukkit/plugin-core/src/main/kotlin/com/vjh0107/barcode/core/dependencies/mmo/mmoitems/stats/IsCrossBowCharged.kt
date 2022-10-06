package com.vjh0107.barcode.core.dependencies.mmo.mmoitems.stats

import io.lumine.mythic.lib.api.item.ItemTag
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder
import net.Indyuce.mmoitems.stat.data.BooleanData
import net.Indyuce.mmoitems.stat.data.type.StatData
import net.Indyuce.mmoitems.stat.type.BooleanStat
import org.bukkit.Material
import java.util.*

class IsCrossBowCharged : BooleanStat("IS_CROSSBOW_CHARGED",
    Material.CROSSBOW,
    "석궁을 상시장전할지",
    arrayOf("바코드 네트워크의 석궁을 상시장전하도록 합니다.", "§8Author: vjh0107"),
    arrayOf("crossbow"),
    *arrayOfNulls(0)) {
    override fun whenApplied(item: ItemStackBuilder, data: StatData) {
        if (!(data as BooleanData).isEnabled) return
        item.addItemTag(getAppliedNBT(data))
    }


    override fun getAppliedNBT(data: StatData): ArrayList<ItemTag?> {
        val ret: ArrayList<ItemTag?> = ArrayList<ItemTag?>()
        if ((data as BooleanData).isEnabled) {
            ret.add(ItemTag("Charged", true))
            ret.add(ItemTag("ChargedProjectiles", "[{id:\"minecraft:arrow\",Count:1b}]"))
        }
        return ret
    }
}
