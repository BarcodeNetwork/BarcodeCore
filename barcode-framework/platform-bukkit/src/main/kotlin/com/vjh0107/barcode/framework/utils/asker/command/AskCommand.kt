package com.vjh0107.barcode.framework.utils.asker.command

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeCommand
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.commandAPICommandOf
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.withSubcommandOf
import com.vjh0107.barcode.framework.utils.asker.AskManager
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.entity.Player
import java.util.*

@BarcodeComponent
class AskCommand : BarcodeCommand {
    override val command = commandAPICommandOf("bAsk") {
        withAliases("barcodeAsk")
        withSubcommandOf("accept") {
            withArguments(StringArgument(""))
            executes(CommandExecutor { sender, args ->
                sender as Player
                val string = args[0] as String
                try {
                    val uuid: UUID = UUID.fromString(string)
                    AskManager.accept(uuid, sender)
                } catch (exception: IllegalArgumentException) {
                }
            })
        }
        withSubcommandOf("decline") {
            withArguments(StringArgument(""))
            executes(CommandExecutor { sender, args ->
                sender as Player
                val string = args[0] as String
                try {
                    val uuid: UUID = UUID.fromString(string)
                    AskManager.decline(uuid, sender)
                } catch (exception: IllegalArgumentException) {
                }
            })
        }
        withSubcommandOf("cancel") {
            withArguments(StringArgument(""))
            executes(CommandExecutor { sender, args ->
                sender as Player
                val string = args[0] as String
                try {
                    val uuid: UUID = UUID.fromString(string)
                    AskManager.cancel(uuid, sender)
                } catch (exception: IllegalArgumentException) {
                }
            })
        }
    }
}