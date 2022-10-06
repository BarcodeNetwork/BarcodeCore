package com.vjh0107.barcode.core.dependencies.placeholderapi

import com.vjh0107.barcode.core.dependencies.mmo.mmocore.utils.getStationsReadySize
import com.vjh0107.barcode.core.dependencies.mmo.mmocore.utils.isDuringCrafting
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.Indyuce.mmocore.api.player.PlayerData
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class BarcodePlaceholders : PlaceholderExpansion() {
    override fun canRegister(): Boolean {
        return true
    }

    override fun getAuthor(): String {
        return "vjh0107"
    }

    override fun getIdentifier(): String {
        return "barcode"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun onRequest(offlinePlayer: OfflinePlayer?, params: String): String {
        val target: Player = offlinePlayer?.player ?: return "PLAYER_IS_NOT_ONLINE"
        if (params.contains("infoboard")) {
            val playerData: PlayerData = PlayerData.get(offlinePlayer)
            val level = playerData.level
            val split = params.split("_")
            val reqLevel = split[1].toInt()
            if (level < reqLevel) {
                return "§c"
            }
            return ""
        }
        when (params) {
            "readys" -> {
                val mmoItemsPlayerData = net.Indyuce.mmoitems.api.player.PlayerData.get(target)
                //negative space font = delete hologram

                if(mmoItemsPlayerData.getStationsReadySize() == 0) return "\uF822\uF80A\uF808"
                return "&e${mmoItemsPlayerData.getStationsReadySize()}개 제작 완료!"
            }
            "crafting" -> {
                val mmoItemsPlayerData = net.Indyuce.mmoitems.api.player.PlayerData.get(target)
                if(mmoItemsPlayerData.isDuringCrafting("용광로")) return "&7제작 중 입니다..."
                return "&7제작 전문 대장장이"
            }
            "actaeon" -> {
                val mmoItemsPlayerData = net.Indyuce.mmoitems.api.player.PlayerData.get(target)
                if(mmoItemsPlayerData.isDuringCrafting("용광로")) return "&f악티온"
                return "\uF822\uF80A\uF808"
            }
        }
        return "Error occurred at BarcodePlaceholders:27"
    }

}