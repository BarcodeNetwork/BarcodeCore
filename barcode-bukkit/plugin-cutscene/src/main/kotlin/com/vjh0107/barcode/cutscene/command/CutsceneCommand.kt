package com.vjh0107.barcode.cutscene.command

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeCommand
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.executeWithPlayer
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.withSubcommandOf
import com.vjh0107.barcode.framework.utils.messagesender.sendBNMessage
import com.vjh0107.barcode.framework.utils.messagesender.sendBNWarnMessage
import com.vjh0107.barcode.framework.utils.transformer.isPlayer
import com.vjh0107.barcode.cutscene.CutsceneMode
import com.vjh0107.barcode.cutscene.api.CutsceneAPI
import com.vjh0107.barcode.cutscene.data.getCutscenePlayerData
import com.vjh0107.barcode.cutscene.datahandler.DataHandler
import com.vjh0107.barcode.cutscene.uibuilder.CutscenePickerMenu
import com.vjh0107.barcode.cutscene.utils.Standards
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

@BarcodeComponent
class CutsceneCommand : BarcodeCommand {
    override val command: CommandAPICommand = CommandAPIExtensions.commandAPICommandOf("bCutscene") {
        withPermission(CommandPermission.OP)
        withAliases("barcodeCutscene")
        withSubcommandOf("editor") {
            executes(CommandExecutor { sender, _ ->
                if (sender.isPlayer()) {
                    CutscenePickerMenu.getMenu(sender, 0)
                }
            })
        }
        withSubcommandOf("play") {
            withSubcommandOf("movable") {
                withArguments(StringArgument("cutsceneID").replaceSuggestions(ArgumentSuggestions.stringsAsync {
                    CompletableFuture.supplyAsync {
                        getAllCutsceneNames()
                    }
                }))
                executeWithPlayer { player, args ->
                    val cutsceneID = args[0] as String
                    CutsceneAPI.startCutscene(cutsceneID, player, true)
                }
            }
            withSubcommandOf("fixed") {
                withArguments(StringArgument("cutsceneID").replaceSuggestions(ArgumentSuggestions.stringsAsync {
                    CompletableFuture.supplyAsync {
                        getAllCutsceneNames()
                    }
                }))
                executeWithPlayer { player, args ->
                    val cutsceneID = args[0] as String
                    CutsceneAPI.startCutscene(cutsceneID, player, false)
                }
            }
        }
        withSubcommandOf("stop") {
            executeWithPlayer { player, _ ->
                stopCutscene(player)
            }
        }
        withSubcommandOf("resume") {
            executeWithPlayer { player, _ ->
                resumeCutscene(player)
            }
        }
        withSubcommandOf("pause") {
            executeWithPlayer { player, _ ->
                pauseCutscene(player)
            }
        }
    }

    private fun stopCutscene(player: Player) {
        val playerData = player.getCutscenePlayerData()

        val cutscene = playerData.cutscene ?: run {
            player.sendBNWarnMessage("컷씬중이 아닙니다.")
            return
        }

        cutscene.stopCutscene(false)
    }

    private fun resumeCutscene(player: Player) {
        val playerData = player.getCutscenePlayerData()

        val cutscene = playerData.cutscene ?: run {
            player.sendBNWarnMessage("컷씬중이 아닙니다.")
            return
        }
        if (!cutscene.isPaused) {
            player.sendBNWarnMessage("멈춰있지 않는 컷씬입니다.")
            return
        }

        cutscene.setIsPaused(false)
        player.sendBNMessage("성공적으로 다시 시작되었습니다.")
    }

    private fun pauseCutscene(player: Player) {
        val playerData = player.getCutscenePlayerData()

        val cutscene = playerData.cutscene ?: run {
            player.sendBNWarnMessage("컷씬중이 아닙니다.")
            return
        }
        if (cutscene.isPaused) {
            player.sendBNWarnMessage("이미 멈춰있습니다.")
            return
        }

        cutscene.setIsPaused(true)
        player.sendBNMessage("성공적으로 멈췄습니다.")
        player.sendBNMessage("현재 플레이타임: &e${cutscene.timepassed} 틱")

        CutsceneMode.setPlayerCutsceneMode(player, false)
    }

    private fun getAllCutsceneNames(): Array<String> {
        return DataHandler.getAllFilesInDirectory(*Standards.PATH_TO_CUTSCENES).map {
            it.name.replace(".yml", "");
        }.toTypedArray()
    }
}