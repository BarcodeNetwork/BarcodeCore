package com.vjh0107.barcode.cutscene

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object CutsceneMode {
    @Suppress("DEPRECATION")
    @JvmOverloads
    @JvmStatic
    fun setPlayerCutsceneMode(p: Player, mode: Boolean, isMovable: Boolean = false) {
        if (mode) {
            if (!isMovable) {
                p.allowFlight = true
                p.isFlying = true
                p.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1))
                for (q in Bukkit.getOnlinePlayers()) {
                    q.hidePlayer(p)
                    p.hidePlayer(q)
                }
            }
            for (sound in Sound.values()) {
                p.stopSound(sound)
            }
        } else {
            p.resetPlayerTime()
            p.resetPlayerWeather()
            p.isFlying = false
            p.allowFlight = false
            p.removePotionEffect(PotionEffectType.INVISIBILITY)
            for (sound in Sound.values()) {
                p.stopSound(sound)
            }
            for (q in Bukkit.getOnlinePlayers()) {
                q.showPlayer(p)
                p.showPlayer(q)
            }
        }
    }
}