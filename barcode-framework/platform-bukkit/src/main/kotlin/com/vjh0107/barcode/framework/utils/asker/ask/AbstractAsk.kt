package com.vjh0107.barcode.framework.utils.asker.ask

import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftAsync
import com.vjh0107.barcode.framework.utils.asker.AskManager
import com.vjh0107.barcode.framework.utils.asker.data.AskPlayer
import com.vjh0107.barcode.framework.utils.messagesender.sendBNMessage
import com.vjh0107.barcode.framework.utils.formatters.toBarcodeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.md_5.bungee.api.chat.*
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.entity.Player
import java.util.*

@Suppress("DEPRECATION")
abstract class AbstractAsk(
    override val sender: Player,
    targets: Set<Player>,
    val expirationSec: Long = 30L
) : Ask {

    abstract override val title: String

    abstract override fun whenAsk()
    abstract override fun whenAccept()
    abstract override fun whenDecline()

    final override val key: UUID = UUID.randomUUID()
    final override val targetAskPlayers: Set<AskPlayer> = targets.map { AskPlayer.of(it, false) }.toSet()

    final override val expirationJob = CoroutineScope(Dispatchers.MinecraftAsync(AskManager.instance.plugin)).launch {
        delay(expirationSec * 1000)
        AskManager.instance.removeAsk(key)
        val notAcceptedPlayers = targetAskPlayers
            .filter { !it.isAccepted }
            .map { it.player }
        val notAcceptedPlayerNames = notAcceptedPlayers
            .map { it.name }
            .toBarcodeFormat()
        sender.sendBNMessage("§e$notAcceptedPlayerNames §f님께서 수락하지 않아 요청이 취소되었습니다.")

        notAcceptedPlayers.forEach { player ->
            player.sendBNMessage("§e${sender.name} §f님께서 요청하신 §e$title §f이(가) 취소되었습니다.")
        }
    }

    final override fun cancel() {
        AskManager.instance.removeAsk(key) // remove ask instance
        expirationJob.cancel()
    }

    final override fun send() {
        AskManager.instance.registerAsk(key, this)
        whenAsk() // when ask

        val cancelComponent = TextComponent("§7§n취소하시겠습니까?").apply {
            clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ask cancel $key")
            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("§f클릭하여 §e$title §f를(을) §c취소합니다."))
        }
        sender.sendMessage(*ComponentBuilder("")
            .append("§8[BN] §f성공적으로 요청을 발송하였습니다.")
            .append(" ")
            .append(cancelComponent)
            .create())

        val acceptComponent = TextComponent("§a§l[✔]").apply {
            clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ask accept $key")
            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("§f클릭하여 §e$title §f를(을) §a수락합니다."))
        }
        val declineComponent = TextComponent("§c§l[✖]").apply {
            clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ask decline $key")
            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("§f클릭하여 §e$title §f를(을) §c거절합니다."))
        }
        targetAskPlayers.forEach { target ->
            target.player.sendMessage(*ComponentBuilder("\n")
            .append("§8[BN] §f당신에게 §e${this.sender.name} §f님으로부터 §e${this.title} §f이(가) 왔습니다.\n")
            .append("§8[BN] §f수락하시겠습니까? &7초대는 ${expirationSec}초 후 만료됩니다.")
            .append(acceptComponent)
            .append(" ")
            .append(declineComponent)
            .append("\n")
            .create())
        }
    }
}
