package com.vjh0107.barcode.core.itembox.command

import com.vjh0107.barcode.core.itembox.data.ItemBoxItem
import com.vjh0107.barcode.core.itembox.service.ItemBoxService
import com.vjh0107.barcode.core.itembox.ui.ItemBoxInventory
import com.vjh0107.barcode.framework.component.BarcodeCommand
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.koin.injector.inject
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.commandAPICommandOf
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.withSubcommandOf
import com.vjh0107.barcode.framework.utils.messagesender.sendBNMessage
import com.vjh0107.barcode.framework.utils.messagesender.sendBNWarnMessage
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.koin.core.parameter.parametersOf
import java.time.LocalDateTime

@BarcodeComponent
class ItemBoxCommand(private val service: ItemBoxService) : BarcodeCommand {
    override val command: CommandAPICommand = commandAPICommandOf("bItemBox") {
        withPermission(CommandPermission.OP)
        withAliases("barcodeItemBox")
        withSubcommandOf("give") {
            withArguments(PlayerArgument("player"))
            executes(CommandExecutor { sender, args ->
                val player = args[0] as Player
                val senderPlayer = sender as? Player

                if (senderPlayer != null) {
                    if (senderPlayer.inventory.itemInMainHand.type != Material.AIR) {
                        val itemBoxItem = ItemBoxItem.of(
                            senderPlayer.inventory.itemInMainHand,
                            LocalDateTime.now(),
                            LocalDateTime.now().plusMinutes(1),
                            "&f관리자에 의해 발송된 우편입니다.",
                            senderPlayer.name
                        )
                        service.addItem(player, itemBoxItem)
                        senderPlayer.sendBNMessage("우편을 발송했습니다. &7(우편은 관리자만 보낼 수 있습니다.)")
                    } else {
                        senderPlayer.sendBNWarnMessage("아이템이 없습니다.")
                    }
                }
            })
        }
        withSubcommandOf("open") {
            executes(CommandExecutor { sender, _ ->
                val senderPlayer = sender as? Player
                val inventory: ItemBoxInventory by inject { parametersOf(senderPlayer) }

                inventory.open()
            })
        }
    }
}