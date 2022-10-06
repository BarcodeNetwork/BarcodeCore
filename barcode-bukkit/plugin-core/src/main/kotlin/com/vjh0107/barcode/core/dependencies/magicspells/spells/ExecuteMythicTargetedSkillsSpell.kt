package com.vjh0107.barcode.core.dependencies.magicspells.spells

import com.nisovin.magicspells.spells.TargetedEntitySpell
import com.nisovin.magicspells.spells.TargetedSpell
import com.nisovin.magicspells.util.MagicConfig
import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class ExecuteMythicTargetedSkillsSpell(config: MagicConfig?, spellName: String?) : TargetedSpell(config, spellName), TargetedEntitySpell {

    private var skillName: String = getConfigString("skillName", "a")

    override fun castSpell(sender: LivingEntity, state: SpellCastState, power: Float, args: Array<String>?): PostCastAction {
        return PostCastAction.HANDLE_NORMALLY
    }

    override fun castFromConsole(sender: CommandSender, args: Array<String>): Boolean {
        return true
    }

    override fun castAtEntity(p0: LivingEntity?, p1: LivingEntity?, p2: Float): Boolean {
        val player = p0 as Player? ?: return true
        val target = p1 ?: return true
        val spell = skillName
        MythicMobsAdapter.inst().apiHelper.castSkill(player, spell, player, player.origin, listOf(target), listOf(target.location), 1F)

        return true
    }

    override fun castAtEntity(p0: LivingEntity?, p1: Float): Boolean {
        return true
    }

}