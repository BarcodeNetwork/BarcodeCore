package com.vjh0107.barcode.framework.utils.asker

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider
import com.vjh0107.barcode.framework.utils.asker.ask.Ask
import com.vjh0107.barcode.framework.utils.messagesender.sendBNMessage
import org.bukkit.entity.Player
import java.util.*

@BarcodeComponent
class AskManager(val plugin: AbstractBarcodePlugin) : BarcodePluginManager {
    private val asks = mutableMapOf<UUID, Ask>()

    fun getAsk(key: UUID) : Ask? {
        return asks[key]
    }

    fun removeAsk(key: UUID) {
        asks.remove(key)
    }

    fun registerAsk(key: UUID, ask: Ask) {
        asks[key] = ask
    }

    @InjectInstance
    companion object : InstanceProvider<AskManager> {
        override lateinit var instance: AskManager

        fun accept(key: UUID, player: Player) {
            val ask = instance.getAsk(key) ?: run {
                player.sendMessage("§8[BN] §f이미 만료되었거나 존재하지 않는 요청입니다.")
                return
            }
            val askPlayer = ask.targetAskPlayers.first { it.player == player }
            askPlayer.isAccepted = true
            //받지않은사람이 한명도 없을 때
            if(ask.targetAskPlayers.none { !it.isAccepted }) {
                ask.whenAccept()
                ask.cancel()
            }
        }

        fun decline(key: UUID, player: Player) {
            val ask = instance.getAsk(key) ?: run {
                player.sendBNMessage("§f이미 만료되었거나 존재하지 않는 요청입니다.")
                return
            }
            val exceptSelf = (ask.getTargetPlayers() + ask.sender).filterNot { it.player == player }
            exceptSelf.forEach { playerEach ->
                playerEach.sendBNMessage("§e${player.name} §f님께서 요청을 거절하셨습니다.")
            }
            ask.whenDecline()
            ask.cancel()
        }

        fun cancel(key: UUID, player: Player) {
            val ask = instance.getAsk(key) ?: run {
                player.sendBNMessage("§f이미 만료되었거나 존재하지 않는 요청입니다.")
                return
            }
            player.sendBNMessage("&f성공적으로 취소하였습니다.")
            ask.getTargetPlayers().forEach { playerEach ->
                playerEach.sendBNMessage("§e${player.name} §f님께서 요청을 취소하셨습니다.")
            }
            ask.cancel()
        }
    }
}