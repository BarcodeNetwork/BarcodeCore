package com.vjh0107.barcode.advancement.commands

import com.vjh0107.barcode.advancement.BarcodeAdvancementPlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeCommand
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.commandAPICommandOf
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.withSubcommandOf
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.logging.Level

@BarcodeComponent
class AdminCommand(val plugin: BarcodeAdvancementPlugin) : BarcodeCommand {
    override val command = commandAPICommandOf("bAdvancement") {
        withPermission(CommandPermission.OP)
        withAliases("bad")
        withSubcommandOf("reload") {
            executes(CommandExecutor { sender, _ ->
                reloadPlugin(sender)
            })
        }
        withSubcommandOf("updatePlayer") {
            withArguments(PlayerArgument("player"))
            executes(CommandExecutor { sender, args ->
                val player = args[0] as Player
                plugin.playerRenderersManager.get(player).loadAsynchronously()
                sender.sendMessage("updated")
            })
        }
    }

    private fun reloadPlugin(sender: CommandSender) {
        try {
            plugin.reloadPlugin()
            Bukkit.getServer().onlinePlayers.forEach {
                plugin.playerRenderersManager.get(it).loadAsynchronously()
            }
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "error while reloading plugin. printing stacktrace")
            e.printStackTrace()
        } finally {
            sender.sendMessage("§a성공적으로 ${plugin.name} ${plugin.description.version} 를 리로드하였습니다.")
        }
    }
}