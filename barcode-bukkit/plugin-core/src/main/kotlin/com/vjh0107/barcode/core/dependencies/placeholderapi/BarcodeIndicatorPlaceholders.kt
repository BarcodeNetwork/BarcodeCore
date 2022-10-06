package com.vjh0107.barcode.core.dependencies.placeholderapi

import com.vjh0107.barcode.core.database.getNullableCorePlayerData
import com.vjh0107.barcode.core.indicator.BuffIndicator
import com.vjh0107.barcode.core.equip.models.EquipSlots
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.Indyuce.mmocore.api.player.PlayerData
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class BarcodeIndicatorPlaceholders : PlaceholderExpansion() {
    override fun canRegister(): Boolean {
        return true
    }

    override fun getAuthor(): String {
        return "vjh0107"
    }

    override fun getIdentifier(): String {
        return "barcodeIndicator"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun onRequest(offlinePlayer: OfflinePlayer?, params: String): String {
        val player: Player = offlinePlayer?.player ?: return "PLAYER_IS_NOT_ONLINE"
        val param = params.split(":")
        when (param.first()) {
            "skill" -> {
                val mmocorePlayerData: PlayerData = PlayerData.get(player)
                if(mmocorePlayerData.isCasting) {
                    val skillCasting = mmocorePlayerData.skillCasting
                    return skillCasting.getFormat(mmocorePlayerData)
                } else {
                    if(mmocorePlayerData.profess.skills.isEmpty()) {
                        return param[3].replace("_", " ")
                    }
                    if(mmocorePlayerData.boundSkills.size == 0) {
                        return param[1].replace("_", " ")
                    } else {
                        return param[2].replace("_", " ")
                    }
                }
            }
            "buff" -> {
                val corePlayerData = player.getNullableCorePlayerData() ?: return ""
                var stringBuilder = ""
                corePlayerData.buffIndicator.indicators.forEach { (_, buffIndicator) ->
                    stringBuilder += formatting(buffIndicator)
                }
                return stringBuilder
            }
            "clock" -> {
                val artifactMaterialType = player.inventory.getItem(EquipSlots.ARTIFACT.slot)?.type ?: return " "
                if (artifactMaterialType == Material.CLOCK) {
                    return PlaceholderAPI.setPlaceholders(player, "%world_timein12_world%")
                        .replace("pm", " pm")
                        .replace("am", " am")
                }
                return " "
            }
        }
        return "Error occurred at BarcodePlaceholders:27"
    }
    private fun formatting(buffIndicator: BuffIndicator) : String {
        val length = buffIndicator.second.toString().length
        var builder: String = "${buffIndicator.icon}\uf806"
        val second = replaceNumberToSmallNumber(buffIndicator.second.toString())
        builder += if (length == 1) {
            "\uf807$second"
        } else {
            second
        }
        return "$builder\uf808"
    }
    private fun replaceNumberToSmallNumber(string: String) : String {
        return string
            .replace("0", "\ue1e5")
            .replace("1", "\ue1e6")
            .replace("2", "\ue1e7")
            .replace("3", "\ue1e8")
            .replace("4", "\ue1e9")
            .replace("5", "\ue1ea")
            .replace("6", "\ue1eb")
            .replace("7", "\ue1ec")
            .replace("8", "\ue1ed")
            .replace("9", "\ue1ee")
    }
}