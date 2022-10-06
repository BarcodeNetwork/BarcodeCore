package com.vjh0107.barcode.core.dependencies.mythicmobs

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.damagers.BarcodeDamageMechanicImpl
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.damagers.DamageByOwnerMechanic
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.magicspells.MagicSpellsCasterMechanic
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.magicspells.MagicSpellsTargetCasterMechanic
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.mmoitems.MMOItemsStatBuffMechanic
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.mmoitems.ManaMechanic
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.mmoitems.SetAttackSpeedCooldownMechanic
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.mmoitems.SetMMOItemsCooldownMechanic
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.skills.*
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent

class BarcodeMechanicFactory(val event: MythicMechanicLoadEvent) {
    private val line = event.mechanicName
    private val mlc = event.config

    fun createBarcodeDamageMechanic() : BarcodeDamageMechanicImpl {
        return BarcodeDamageMechanicImpl(line, mlc)
    }

    fun createDamageByOwnerMechanic() : DamageByOwnerMechanic {
        return DamageByOwnerMechanic(line, mlc)
    }

    fun createMagicSpellsCasterMechanic() : MagicSpellsCasterMechanic {
        return MagicSpellsCasterMechanic(line, mlc)
    }

    fun createMagicSpellsTargetCasterMechanic() : MagicSpellsTargetCasterMechanic {
        return MagicSpellsTargetCasterMechanic(line, mlc)
    }

    fun createManaMechanic() : ManaMechanic {
        return ManaMechanic(line, mlc)
    }

    fun createMMOItemsStatBuffMechanic() : MMOItemsStatBuffMechanic {
        return MMOItemsStatBuffMechanic(line, mlc)
    }

    fun createSetAttackSpeedCooldownMechanic() : SetAttackSpeedCooldownMechanic {
        return SetAttackSpeedCooldownMechanic(line, mlc)
    }

    fun createSetMMOItemsCooldownMechanic() : SetMMOItemsCooldownMechanic {
        return SetMMOItemsCooldownMechanic(line, mlc)
    }

    fun createBuffIndicatorMechanic() : BuffIndicatorMechanic {
        return BuffIndicatorMechanic(line, mlc)
    }

    fun createCooldownMechanic() : CooldownMechanic {
        return CooldownMechanic(line, mlc)
    }

    fun createEquipSkillMechanic() : EquipSkillMechanic {
        return EquipSkillMechanic(line, mlc)
    }

    fun createFontSkillMechanic() : FontSkillMechanic {
        return FontSkillMechanic(line, mlc)
    }

    fun createLowerTheShieldMechanic() : LowerTheShieldMechanic {
        return LowerTheShieldMechanic(line, mlc)
    }

    fun <T : BarcodeSkillMechanic> T.register() {
        event.register(this)
    }
}