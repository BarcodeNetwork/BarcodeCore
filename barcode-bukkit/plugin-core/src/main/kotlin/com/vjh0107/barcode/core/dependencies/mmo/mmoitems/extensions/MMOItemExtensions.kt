package com.vjh0107.barcode.core.dependencies.mmo.mmoitems.extensions

import com.vjh0107.barcode.framework.utils.isNull
import io.lumine.mythic.lib.api.item.NBTItem
import net.Indyuce.mmoitems.MMOUtils
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem
import org.bukkit.inventory.ItemStack

/**
 * 성능을 많이 요하므로 검사 후 사용할 것
 */
fun ItemStack.toLiveMMOItem() : LiveMMOItem {
    return LiveMMOItem(this)
}

fun ItemStack.isMMOItem() : Boolean {
    return MMOUtils.getType(NBTItem.get(this)).isNull().not()
}
