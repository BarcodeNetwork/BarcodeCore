package com.vjh0107.barcode.core.commands

import com.vjh0107.barcode.core.npc.NPCComponent
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeCommand
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.commandAPICommandOf
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.withSubcommandOf
import com.vjh0107.barcode.framework.utils.transformer.toPlayer
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.executors.CommandExecutor
import net.citizensnpcs.api.event.SpawnReason
import net.citizensnpcs.api.trait.trait.Equipment
import net.citizensnpcs.api.trait.trait.Owner
import org.betonquest.betonquest.compatibility.protocollib.hider.NPCHider
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@BarcodeComponent
class TestCommand(val plugin: AbstractBarcodePlugin) : BarcodeCommand {
    override val command = commandAPICommandOf("bTest") {
        withPermission(CommandPermission.OP)
        withAliases("barcodeTest")
        withSubcommandOf("NPCHiderTest") {
            withSubcommandOf("reloadNPCHider") {
                executes(CommandExecutor { _, _ ->
                    NPCHider.start()
                })
            }
            withSubcommandOf("applyVisibilityAll") {
                executes(CommandExecutor { _, _ ->
                    NPCHider.getInstance().applyVisibility()
                })
            }
            withSubcommandOf("applyVisibilityPlayer") {
                withArguments(PlayerArgument("player"))
                executes(CommandExecutor { _, args ->
                    NPCHider.getInstance().applyVisibility(args[0] as Player)
                })
            }
        }
        withSubcommandOf("NPCSpawnTest") {
            withSubcommandOf("spawnNPC") {
                executes(CommandExecutor { sender, args ->
                    createNPC(sender)
                })
            }
        }
    }

    private fun createNPC(sender: CommandSender) {
        val player = sender.toPlayer()
        val npc = NPCComponent.instance.createNPC(player.name)

        npc.spawn(player.location, SpawnReason.PLUGIN)

        val trait = npc.getOrAddTrait(Equipment::class.java)
        trait.set(Equipment.EquipmentSlot.HELMET, player.equipment.helmet)
        trait.set(Equipment.EquipmentSlot.CHESTPLATE, player.equipment.chestplate)
        trait.set(Equipment.EquipmentSlot.LEGGINGS, player.equipment.leggings)
        trait.set(Equipment.EquipmentSlot.BOOTS, player.equipment.boots)
        trait.set(Equipment.EquipmentSlot.HAND, player.equipment.itemInMainHand)
        trait.set(Equipment.EquipmentSlot.OFF_HAND, player.equipment.itemInOffHand)
        val ownerTrait = npc.getOrAddTrait(Owner::class.java)
    }
}