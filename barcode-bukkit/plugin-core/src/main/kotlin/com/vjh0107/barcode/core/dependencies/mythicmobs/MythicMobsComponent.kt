package com.vjh0107.barcode.core.dependencies.mythicmobs

import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter
import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter.toBukkitPlayer
import com.vjh0107.barcode.core.dependencies.mythicmobs.conditions.BetonQuestCondition
import com.vjh0107.barcode.core.dependencies.mythicmobs.conditions.UnderIsAirCondition
import com.vjh0107.barcode.core.dependencies.mythicmobs.drops.BarcodeMMOItemsDrop
import com.vjh0107.barcode.core.dependencies.mythicmobs.drops.MMOCoreClassExpDrop
import com.vjh0107.barcode.core.dependencies.mythicmobs.drops.MMOCoreMeisterExpDrop
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import io.lumine.mythic.bukkit.events.*
import io.lumine.mythic.core.skills.placeholders.Placeholder
import net.Indyuce.mmocore.MMOCore
import net.Indyuce.mmocore.api.player.PlayerData
import org.bukkit.event.EventHandler

@BarcodeComponent
class MythicMobsComponent : BarcodeListener {
    var placeholderRegistered = false

    @EventHandler
    fun onMythicMechanicLoad(event: MythicMechanicLoadEvent) {
        if (!placeholderRegistered) {
            placeholderRegistered = true
            registerPlaceholders()
        }

        val factory = BarcodeMechanicFactory(event)

        when (event.mechanicName.lowercase()) {
            "damagebyowner" -> factory.createDamageByOwnerMechanic()
            "castmagicspells" -> factory.createMagicSpellsCasterMechanic()
            "casttargetedmagicspells" -> factory.createMagicSpellsTargetCasterMechanic()
            "setattackspeedcooldown" -> factory.createSetAttackSpeedCooldownMechanic()
            "setmmoitemscooldown" -> factory.createSetMMOItemsCooldownMechanic()
            "givemana" -> factory.createManaMechanic()
            "setcooldown" -> factory.createCooldownMechanic()
            "lowertheshield" -> factory.createLowerTheShieldMechanic()
            "barcodestatbuff" -> factory.createMMOItemsStatBuffMechanic()
            "barcodebuffindicator" -> factory.createBuffIndicatorMechanic()
            "equipskill" -> factory.createEquipSkillMechanic()
            "fontskill" -> factory.createFontSkillMechanic()
            "barcodedamage" -> factory.createBarcodeDamageMechanic()
            else -> return
        }.register(event)
    }

    @EventHandler
    fun onMythicMobsReload(event: MythicReloadedEvent) {
        registerPlaceholders()
    }

    private fun registerPlaceholders() {
        val placeholder = Placeholder.meta { metadata, arg ->
            val args = arg.split(":")
            if (args.size < 2) {
                throw RuntimeException("barcodeskill.<스킬명>:<모디파이어>")
            } else if (!metadata.caster.entity.isPlayer) {
                throw RuntimeException("caster 는 플레이어여야합니다.")
            } else {
                val skillName = args[0]
                val modifierName = args[1]
                val player = metadata.caster.entity.asPlayer().toBukkitPlayer()

                val skill = MMOCore.plugin.skillManager.get(skillName) ?: throw RuntimeException("없는 스킬입니다.")
                val skillLevel = PlayerData.get(player).getSkillLevel(skill)

                val modifier: Double = skill.getModifier(modifierName, skillLevel)
                return@meta modifier.toString()
            }
        }
        MythicMobsAdapter.inst().placeholderManager.register("barcodeskill", placeholder)
    }

    @EventHandler
    fun onMythicTargeterLoad(event: MythicTargeterLoadEvent) {
        val factory = BarcodeTargeterFactory(event)

        when (event.targeterName.lowercase()) {
            "owned" -> factory.createOwnedTargeter()
            "barcode" -> factory.createBarcodeTargeter()
            else -> return
        }.register(event)
    }

    @EventHandler
    fun onMythicConditionLoad(event: MythicConditionLoadEvent) {
        val condition = when (event.conditionName.lowercase()) {
            "underisair" -> UnderIsAirCondition(event.conditionName, event.config)
            "betonquestcondition" -> BetonQuestCondition(event.conditionName, event.config)
            else -> return
        }
        event.register(condition)
    }

    @EventHandler
    fun onMythicDropLoad(event: MythicDropLoadEvent) {
        val drop = when (event.dropName.lowercase()) {
            "barcodemeisterexp" -> MMOCoreMeisterExpDrop(event.config)
            "barcodeclassexp" -> MMOCoreClassExpDrop(event.config)
            "barcodemmoitems" -> BarcodeMMOItemsDrop(event.config)
            else -> return
        }
        event.register(drop)
    }
}