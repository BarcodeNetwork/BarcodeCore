package com.vjh0107.barcode.core.dependencies.mythicmobs.drops

import com.vjh0107.barcode.framework.utils.item.createGuiItem
import io.lumine.mythic.api.adapters.AbstractItemStack
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.drops.DropMetadata
import io.lumine.mythic.api.drops.IItemDrop
import io.lumine.mythic.bukkit.adapters.BukkitItemStack
import io.lumine.mythic.core.logging.MythicLogger
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.Type
import org.apache.commons.lang.Validate
import org.bukkit.Material
import org.bukkit.entity.Player

class BarcodeMMOItemsDrop(config: MythicLineConfig) : IItemDrop {
    val type: Type
    val id: String

    init {
        val typeFormat = config.getString("type").uppercase().replace("-", "_")
        Validate.isTrue(
            MMOItems.plugin.types.has(typeFormat),
            "Could not find type with ID $typeFormat"
        )
        type = MMOItems.plugin.types.get(typeFormat) ?: throw RuntimeException("type $typeFormat not found")
        id = config.getString("id")
        Validate.notNull(id, "MMOItems ID cannot be null")
    }

    override fun getDrop(metadata: DropMetadata, amount: Double): AbstractItemStack? {
        val player = metadata.trigger.bukkitEntity as? Player ?: return null
        if (player.isOnline.not()) return null

        val itemStack = MMOItems.plugin.getItem(type, id)
        return if (itemStack == null) {
            val id = type.id
            MythicLogger.errorCompatibility("MMOITEMS", "Item type " + id + "," + this.id + " not found")
            BukkitItemStack(
                createGuiItem(
                    Material.STONE,
                    displayName = "오류 아이템입니다. 관리자에게 주면 보상과 함께 교환해줍니다. code: ${this.id}"
                )
            )
        } else {
            BukkitItemStack(itemStack).amount(amount.toInt())
        }
    }
}