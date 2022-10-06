package com.vjh0107.barcode.core.meister.stats

import net.Indyuce.mmoitems.stat.type.DoubleStat
import net.Indyuce.mmoitems.stat.type.ItemStat
import net.Indyuce.mmoitems.stat.type.StringStat
import org.bukkit.Material

enum class MeisterStats(val stat: ItemStat) {
    MEISTER_DAMAGE(
        DoubleStat(
            "MEISTER_DAMAGE",
            Material.IRON_PICKAXE,
            "전문기술 도구 데미지",
            arrayOf("바코드 네트워크 전문기술 도구 데미지를 설정합니다.", "§8Author: vjh0107"),
            arrayOf("all"),
            *arrayOfNulls(0)
        )
    ),
    MEISTER_SPEED(
        DoubleStat(
            "MEISTER_SPEED",
            Material.IRON_PICKAXE,
            "전문기술 도구 속도",
            arrayOf("바코드 네트워크 전문기술 도구 속도를 설정합니다.", "§8Author: vjh0107"),
            arrayOf("all"),
            *arrayOfNulls(0)
        )
    ),
    MEISTER_TYPE(
        StringStat(
            "MEISTER_TYPE",
            Material.GOLD_INGOT,
            "전문기술 타입",
            arrayOf("바코드 네트워크 전문기술 타입을 설정합니다.", "§8Author: vjh0107"),
            arrayOf("tool"),
            *arrayOfNulls(0)
        )
    )
}