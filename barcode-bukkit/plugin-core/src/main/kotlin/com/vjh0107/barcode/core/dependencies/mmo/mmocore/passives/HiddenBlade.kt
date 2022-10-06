package com.vjh0107.barcode.core.dependencies.mmo.mmocore.passives

import com.vjh0107.barcode.core.database.getCorePlayerData
import com.vjh0107.barcode.core.events.player.BarcodeDamageEvent
import io.lumine.mythic.lib.api.player.EquipmentSlot
import io.lumine.mythic.lib.api.stat.modifier.ModifierSource
import io.lumine.mythic.lib.api.stat.modifier.ModifierType
import io.lumine.mythic.lib.api.stat.modifier.StatModifier
import net.Indyuce.mmocore.api.player.PlayerData
import net.Indyuce.mmocore.api.util.math.formula.LinearValue
import net.Indyuce.mmocore.skill.PassiveSkill
import org.bukkit.Material
import org.bukkit.event.EventHandler

class HiddenBlade : PassiveSkill() {
    companion object {
        private const val key = "BARCODE_HIDDENBLADE"
    }
    init {
        setMaterial(Material.FLINT)
        setLore("")
        setPassive()
        addModifier("cooldown", LinearValue(0.0, 0.0))
        addModifier("mana", LinearValue(0.0, 0.0))
        addModifier("duration", LinearValue(0.0, 0.0))
        addModifier("critical_chance",
            LinearValue(10.0, 0.0)
        )
        addModifier("critical_damage",
            LinearValue(10.0, 0.0)
        )
    }
    @EventHandler
    fun a(event: BarcodeDamageEvent) {
        val player = event.player
        val mmoCorePlayerData = PlayerData.get(player)
        if (!mmoCorePlayerData.profess.hasSkill(this)) return
        val cast = mmoCorePlayerData.cast(this)
        if (!cast.isSuccessful) return
        mmoCorePlayerData.cast(cast.info)

        val removeModifierIfItIsNotNull: (String) -> Unit = {
            if (mmoCorePlayerData.stats.map.getInstance(it).contains(key)) {
                mmoCorePlayerData.stats.map.getInstance(it).remove(key)
            }
        }

        //크리티컬 찬스
        if (!event.isCriticalAttack) {

            // 크리티컬이 발동되지 않았을 경우, 크리티컬 데미지 상승 효과를 삭제한다.
            removeModifierIfItIsNotNull("CRITICAL_STRIKE_POWER")

            val statModifier = StatModifier(cast.getModifier("critical_chance"), ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER)

            mmoCorePlayerData.stats.map.getInstance("CRITICAL_STRIKE_CHANCE").addModifier(key, statModifier)

            player.getCorePlayerData().buffIndicator.setBuffIndicatorCooldown("\uE1F1",
                cast.getModifier("duration"),
                false,
                key
            ) { removeModifierIfItIsNotNull("CRITICAL_STRIKE_CHANCE") }
        } else if (event.isCriticalAttack) {

            // 크리티컬이 발동되었을 경우, 크리티컬 확률 인스턴스에서 상승 모디파이어를 삭제한다.
            removeModifierIfItIsNotNull("CRITICAL_STRIKE_CHANCE")
            // 크리티컬 데미지 증가 값을 불러와서 스텟 상승 모디파이어 생성
            val statModifier = StatModifier(cast.getModifier("critical_damage"), ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER)
            // 크리티컬 데미지 인스턴스에 스텟 상승 모디파이어 넣기
            mmoCorePlayerData.stats.map.getInstance("CRITICAL_STRIKE_POWER").addModifier(key, statModifier)

            player.getCorePlayerData().buffIndicator.setBuffIndicatorCooldown("\uE1F2",
                cast.getModifier("duration"),
                false,
                key
            ) { removeModifierIfItIsNotNull("CRITICAL_STRIKE_POWER") }
        }

    }
}