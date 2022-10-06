package com.vjh0107.barcode.core.skill.damager.listeners

import com.nisovin.magicspells.Spell
import com.nisovin.magicspells.events.SpellApplyDamageEvent
import com.vjh0107.barcode.core.skill.damager.getSkillDamageModifier
import com.vjh0107.barcode.core.skill.damager.giveBarcodeDamage
import io.lumine.mythic.lib.damage.DamageType
import net.Indyuce.mmocore.api.player.PlayerData
import net.Indyuce.mmocore.api.util.MMOCoreUtils
import net.Indyuce.mmoitems.MMOUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.security.InvalidParameterException

class PainSpellTagParser : Listener {
    @EventHandler
    fun onSpellApplyDamage(event: SpellApplyDamageEvent) {
        val player: Player = event.caster as? Player ?: return
        val target = event.target
        val mmoCorePlayerData = PlayerData.get(player)

        if (!MMOCoreUtils.canTarget(mmoCorePlayerData, target) || !MMOUtils.canTarget(player, target)) return

        val splited = parseTag(event.spell).entries
        val damageTypes = getDamageTypesByTags(splited)
        val skillName = splited.find { it.key.lowercase() == "skillname" }?.value ?: throw NullPointerException("can't get skillname at ${event.spell.name}")
        val skillDamageKey = splited.find { it.key.lowercase() == "damagekey" }?.value ?: throw NullPointerException("can't get damagekey at ${event.spell.name}")

        val skillDamageParameter = player.getSkillDamageModifier(skillName, skillDamageKey)

        event.target.giveBarcodeDamage(player, skillDamageParameter, damageTypes)
        event.applyDamageModifier(0F)
    }

    private fun getDamageTypesByTags(splited: Set<Map.Entry<String, String>>) : Array<DamageType> {
        val damageTypes: MutableList<DamageType> = mutableListOf<DamageType>()
        splited.filter {
            it.key.lowercase() == "vjh0107.damagetype"
        }.forEach {
            try {
                val damageTypesSplited = it.value.uppercase().replace(" ", "").split(",")
                damageTypesSplited.forEach { singleDamageType ->
                    damageTypes.add(DamageType.valueOf(singleDamageType))
                }
            } catch (event: IllegalArgumentException) {
                event.printStackTrace()
            }
        }
        return damageTypes.map { it }.toTypedArray()
    }

    private fun parseTag(spell: Spell): Map<String, String> {
        return spell.tags.mapNotNull {
            if (it.startsWith("spell")) return@mapNotNull null
            val splited = it.split("=")
            val key =
                splited.getOrNull(0) ?: throw InvalidParameterException("Can't split data $it from ${spell.name} - arg 0")
            val args =
                splited.getOrNull(1) ?: throw InvalidParameterException("Can't split data $it from ${spell.name} - arg 1")
            key to args
        }.toMap()
    }
}