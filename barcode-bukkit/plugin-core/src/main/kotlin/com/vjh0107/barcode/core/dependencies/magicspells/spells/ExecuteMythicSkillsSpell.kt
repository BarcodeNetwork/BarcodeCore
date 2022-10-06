package com.vjh0107.barcode.core.dependencies.magicspells.spells

import com.nisovin.magicspells.spells.InstantSpell
import com.nisovin.magicspells.util.MagicConfig
import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class ExecuteMythicSkillsSpell(config: MagicConfig?, spellName: String?) : InstantSpell(config, spellName) {

    private var skillName: String = getConfigString("skillName", "Fireball")

    override fun castSpell(sender: LivingEntity, state: SpellCastState, power: Float, args: Array<String>?): PostCastAction {
        val player = sender as Player
        val spell = skillName
        MythicMobsAdapter.inst().apiHelper.castSkill(player, spell)
        return PostCastAction.HANDLE_NORMALLY
    }

    override fun castFromConsole(sender: CommandSender, args: Array<String>): Boolean {
        return true
    }

}