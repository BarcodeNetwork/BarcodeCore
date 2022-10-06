package com.vjh0107.barcode.framework.utils

import com.vjh0107.barcode.framework.utils.transformer.isPlayer
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ExecutorType
import org.bukkit.entity.Player

object CommandAPIExtensions {
    /**
     * CommandAPI 의 CommandAPICommand 객체 생성을 람다로 처리합니다.
     */
    fun commandAPICommandOf(commandName: String, block: CommandAPICommand.() -> CommandAPICommand): CommandAPICommand {
        return block(CommandAPICommand(commandName))
    }

    /**
     * CommandAPI 의 withSubcommand 를 람다로 처리합니다.
     */
    fun CommandAPICommand.withSubcommandOf(
        commandName: String,
        subcommandBlock: CommandAPICommand.() -> CommandAPICommand
    ): CommandAPICommand {
        val subcommand = commandAPICommandOf(commandName) {
            subcommandBlock()
        }
        this.subcommands.add(subcommand)
        return this
    }

    fun CommandAPICommand.executeWithPlayer(block: (player: Player, args: Array<out Any>) -> Unit) : CommandAPICommand {
        this.executes(CommandExecutor { sender, args ->
            if (sender.isPlayer()) {
                block(sender, args)
            }
        }, ExecutorType.PLAYER)
        return this
    }
}