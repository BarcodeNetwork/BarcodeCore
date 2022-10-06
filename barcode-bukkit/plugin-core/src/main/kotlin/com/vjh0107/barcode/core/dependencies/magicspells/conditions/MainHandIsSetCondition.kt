package com.vjh0107.barcode.core.dependencies.magicspells.conditions

import com.nisovin.magicspells.castmodifiers.Condition
import io.lumine.mythic.lib.api.item.NBTItem
import net.Indyuce.mmoitems.api.Type
import net.Indyuce.mmoitems.api.player.PlayerData
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player


class MainHandIsSetCondition : Condition() {
    private var mmoItemType: String = "CATALYST"

    override fun initialize(value: String?): Boolean {
        this.mmoItemType = value ?: return false
        return true
    }

    override fun check(livingEntity: LivingEntity?): Boolean {
        if(livingEntity == null) return false
        if(livingEntity.equipment == null) return false
        val player: Player = livingEntity as? Player ?: return false
        val mmoItemPlayerData: PlayerData = PlayerData.get(player)

        val item = mmoItemPlayerData.inventory.hand
        val typeID: String = Type.get(NBTItem.get(item).type)?.id ?: return false

        if (typeID != mmoItemType.toUpperCase()) return false
        return true
    }

    override fun check(p0: LivingEntity?, livingEntity: LivingEntity?): Boolean {
        return false
    }

    override fun check(p0: LivingEntity?, p1: Location?): Boolean {
        return false
    }

}