package com.vjh0107.barcode.mmoitems.nbt

import com.vjh0107.barcode.framework.nbt.NBTItem

fun NBTItem.getStat(stat: String): Double {
    return getDouble("MMOITEMS_$stat")
}

fun NBTItem.hasType(): Boolean {
    return hasTag("MMOITEMS_ITEM_TYPE")
}

fun NBTItem.getType(): String? {
    val tag: String = getString("MMOITEMS_ITEM_TYPE")
    return if (tag != "") tag else null
}

fun NBTItem.getTier(): String? {
    val tag: String = getString("MMOITEMS_TIER")
    return if (tag != "") tag else null
}