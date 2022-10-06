package com.vjh0107.barcode.core.commands

import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter
import com.vjh0107.barcode.core.npc.traits.BarcodeAttackTrait
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeCommand
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.commandAPICommandOf
import com.vjh0107.barcode.framework.utils.CommandAPIExtensions.withSubcommandOf
import com.vjh0107.barcode.framework.utils.messagesender.sendBNWarnMessage
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.command.CommandSender

@BarcodeComponent
class SentinelCommand : BarcodeCommand {
    override val command = commandAPICommandOf("bSentinel") {
        withPermission(CommandPermission.OP)
        withAliases("barcodeSentinel")
        withSubcommandOf("traits") {
            withSubcommandOf("setBarcodeAttack") {
                withArguments(StringArgument("MythicMobsSkillInternalID"))
                executes(CommandExecutor { sender, args ->
                    val skillID = args[0] as? String
                    val mythicMobsSkill = MythicMobsAdapter.inst().skillManager.getSkill(skillID)
                    if (skillID != null && mythicMobsSkill.isPresent) {
                        setSentinelBarcodeAttackTrait(sender, skillID)
                    } else {
                        sender.sendBNWarnMessage("정확한 MythicMobsSkillInternalID 를 요구합니다.")
                    }
                })
            }
        }
    }

    private fun setSentinelBarcodeAttackTrait(sender: CommandSender, skillID: String) {
        val selectedNPC = CitizensAPI.getDefaultNPCSelector().getSelected(sender) ?: run {
            sender.sendBNWarnMessage("먼저 엔피시를 선택해주세요. /npc sel")
            return
        }
        val traitSkillID = selectedNPC.getTraitNullable(BarcodeAttackTrait::class.java) ?: run {
            sender.sendBNWarnMessage("엔피시에게 trait 을 추가해야합니다, /trait add barcodeattack")
            return
        }
        traitSkillID.attackSkillID = skillID
    }
}