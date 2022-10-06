package com.vjh0107.barcode.core.dependencies.mmo

import com.vjh0107.barcode.core.dependencies.mmo.mmocore.passives.BarcodeBackStab
import com.vjh0107.barcode.core.dependencies.mmo.mmocore.passives.HiddenBlade
import com.vjh0107.barcode.core.dependencies.mmo.mmoitems.abilities.BarcodeBurningHell
import com.vjh0107.barcode.core.dependencies.mmo.mmoitems.abilities.BarcodeLightning
import com.vjh0107.barcode.core.dependencies.mmo.mmoitems.abilities.BarcodePluperfect
import com.vjh0107.barcode.core.blacksmith.stats.EnhanceChance
import com.vjh0107.barcode.core.blacksmith.stats.EnhanceDestroyChance
import com.vjh0107.barcode.core.blacksmith.stats.EnhanceVolume
import com.vjh0107.barcode.core.equip.compatibility.stats.TitleValue
import com.vjh0107.barcode.core.blacksmith.stats.Enhanceable
import com.vjh0107.barcode.core.dependencies.mmo.mmoitems.stats.IsCrossBowCharged
import com.vjh0107.barcode.core.equip.compatibility.MMOItemsCompatibility
import com.vjh0107.barcode.core.item.stats.NotExchangeableStat
import com.vjh0107.barcode.core.meister.stats.MeisterStats
import com.vjh0107.barcode.core.skill.MythicLibsListenerUnregisterer
import com.vjh0107.barcode.core.skill.defaultattack.stats.BarcodeDefaultAttackAbility
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar
import net.Indyuce.mmocore.MMOCore
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.ability.Ability
import org.bukkit.event.Listener

@BarcodeComponent
class MMOComponent(val plugin: AbstractBarcodePlugin) : BarcodeRegistrable {
    override val id: String = "MMOComponentRegistrar"

    @Registrar
    fun registerListeners() {
        registerAbilityListener(barcodeLightning)
        registerAbilityListener(barcodePluperfect)
        registerAbilityListener(barcodeBurningHell)
    }

    private fun registerAbilityListener(ability: Ability<*>) {
        if (ability is Listener) plugin.registerListener(ability as Listener)
    }

    @Registrar
    fun registerMMOCoreAbility() {
        plugin.run {
            registerListener(barcodeBackStab)
            registerListener(hiddenBlade)
            MythicLibsListenerUnregisterer(this)
        }

        MMOCore.plugin.skillManager.run {
            register(barcodeBackStab)
            register(hiddenBlade)
            reload()
        }
    }

    override fun unregister() {
        MMOCore.plugin.skillManager.run {
            unregister(barcodeBackStab.id)
            unregister(hiddenBlade.id)
            reload()
        }
    }

    companion object {
        // MMOCore
        private val barcodeBackStab = BarcodeBackStab()
        private val hiddenBlade = HiddenBlade()

        // MMOItems
        val barcodeLightning = BarcodeLightning()
        val barcodeBurningHell = BarcodeBurningHell()
        val barcodePluperfect = BarcodePluperfect()

        fun onLoad(plugin: AbstractBarcodePlugin) {
            registerCustomStats()
            registerInventory()
            registerMMOItemsAbility(plugin)
        }

        private fun registerInventory() {
            MMOItems.plugin.registerPlayerInventory(MMOItemsCompatibility())
        }

        private fun registerCustomStats() {
            MMOItems.plugin.stats.run {
                MeisterStats.values().forEach {
                    register(it.stat)
                }

                register(EnhanceChance())
                register(EnhanceVolume())
                register(EnhanceDestroyChance())
                register(IsCrossBowCharged())
                register(TitleValue())
                register(BarcodeDefaultAttackAbility())
                register(Enhanceable())
                register(NotExchangeableStat())
            }
        }

        private fun registerMMOItemsAbility(plugin: AbstractBarcodePlugin) {
            plugin.run {
                registerMMOItemsAbility(barcodeLightning)
                registerMMOItemsAbility(barcodeBurningHell)
                registerMMOItemsAbility(barcodePluperfect)
            }
        }

        private fun registerMMOItemsAbility(ability: Ability<*>) {
            MMOItems.plugin.abilities.registerAbilityByExternal(ability)
        }
    }
}

