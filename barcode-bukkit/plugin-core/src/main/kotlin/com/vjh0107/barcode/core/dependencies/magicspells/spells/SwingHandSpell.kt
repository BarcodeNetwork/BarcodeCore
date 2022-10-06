package com.vjh0107.barcode.core.dependencies.magicspells.spells

import com.nisovin.magicspells.spells.InstantSpell
import com.nisovin.magicspells.util.MagicConfig
import org.bukkit.entity.LivingEntity

class SwingOffHandSpell(config: MagicConfig, spellName: String) : InstantSpell(config, spellName) {
    override fun castSpell(
        caster: LivingEntity,
        state: SpellCastState?,
        power: Float,
        args: Array<out String>?
    ): PostCastAction {
        caster.swingOffHand()
        return PostCastAction.HANDLE_NORMALLY
    }

}

