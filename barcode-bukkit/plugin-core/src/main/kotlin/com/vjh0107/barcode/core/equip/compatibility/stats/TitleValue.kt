package com.vjh0107.barcode.core.equip.compatibility.stats

import io.lumine.mythic.lib.api.item.ItemTag
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder
import net.Indyuce.mmoitems.stat.data.StringData
import net.Indyuce.mmoitems.stat.data.type.StatData
import net.Indyuce.mmoitems.stat.type.StringStat
import org.bukkit.Material


class TitleValue : StringStat("TITLE_VALUE",
        Material.GOLD_INGOT,
        "칭호 내용",
        arrayOf("바코드 네트워크 칭호 내용을 설정합니다.","칭호 내용은 로어에 표시되지 않습니다.", "§8Author: vjh0107"),
        arrayOf("accessory"),
        *arrayOfNulls(0)) {
    override fun whenApplied(item: ItemStackBuilder, data: StatData) {
        val titleValue = (data as StringData).string!!
        item.addItemTag(*arrayOf(ItemTag("BARCODE_TITLE_VALUE", titleValue)))
    }
}
