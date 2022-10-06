package com.vjh0107.barcode.core.skill.defaultattack.stats


import net.Indyuce.mmoitems.stat.type.StringStat
import org.bukkit.Material

class BarcodeDefaultAttackAbility : StringStat(
    "BARCODE_DEFAULT_ATTACK_ABILITY",
    Material.WOODEN_SWORD,
    "바코드 기본공격",
    arrayOf("바코드 기본공격을 설정합니다. (좌클릭일 시에만 사용하면 됨)"),
    arrayOf("all"),
    *arrayOfNulls(0)
)
