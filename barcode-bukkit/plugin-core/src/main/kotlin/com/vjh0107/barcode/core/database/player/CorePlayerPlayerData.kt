package com.vjh0107.barcode.core.database.player

import com.vjh0107.barcode.core.events.player.AsyncCorePlayerDataLoadEvent
import com.vjh0107.barcode.core.indicator.PlayerBuffIndicatorData
import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import net.Indyuce.mmocore.MMOCore
import net.Indyuce.mmocore.api.player.PlayerData
import org.bukkit.Bukkit

class CorePlayerPlayerData private constructor(override val playerID: PlayerIDWrapper) : SavableCorePlayerDataProvider() {

    val buffIndicator = PlayerBuffIndicatorData(player)

    var isMeisterBehaving = false
    var isSilenced: Boolean = false
        set(value) {
            val mmoCorePlayerData: PlayerData = PlayerData.get(player)
            //스킬모드 중이며 침묵상태로 만들었을 때
            if (mmoCorePlayerData.isCasting && value) {
                MMOCore.plugin.configManager.getSimpleMessage("casting.no-longer").send(this.player)
                mmoCorePlayerData.skillCasting.close()
            }
            field = value
        }

    override fun callAsyncCorePlayerDataLoadEvent() {
        Bukkit.getPluginManager().callEvent(AsyncCorePlayerDataLoadEvent(this))
    }

    companion object {
        fun of(playerID: PlayerIDWrapper): CorePlayerPlayerData {
            return CorePlayerPlayerData(playerID)
        }
    }
}

