package com.vjh0107.barcode.core.skill

import com.vjh0107.barcode.core.skill.damager.listeners.BarcodeDamageListener
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import io.lumine.mythic.lib.MythicLib
import io.lumine.mythic.lib.listener.AttackEffects
import org.bukkit.event.HandlerList
import java.util.logging.Level

class MythicLibsListenerUnregisterer(plugin: AbstractBarcodePlugin) {
    init {
        try {
            HandlerList.getRegisteredListeners(MythicLib.inst()).forEach { registeredListener ->
                if (registeredListener.listener is AttackEffects) {
                    val attackEffects = registeredListener.listener as AttackEffects
                    plugin.logger.log(
                        Level.INFO,
                        "attempting to unregister AttackEffects event handlers"
                    )
                    HandlerList.unregisterAll(attackEffects)
                    plugin.logger.log(Level.INFO, "successful unregistered AttackEffects handler")
                }
            }
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "can't unregister AttackEffects event handlers")
            e.printStackTrace()
        } finally {
            plugin.registerListener(BarcodeDamageListener())
            plugin.logger.log(Level.INFO, "successful registered BarcodeNetwork custom damage listener")
        }
    }
}