package com.vjh0107.barcode.core.dependencies.betonquests.variables

import io.lumine.mythic.lib.api.item.NBTItem
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.Variable
import org.betonquest.betonquest.exceptions.InstructionParseException
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.Player

class MMOItemAmount(instruction: Instruction) : Variable(instruction) {
    private val mmoItemType: String
    private val mmoItemID: String
    private var type: Type? = null
    private var amount = 0

    init {
        mmoItemType = instruction.next()
        mmoItemID = instruction.next()
        if (instruction.next().lowercase().startsWith("left:")) {
            type = Type.LEFT
            amount = try {
                instruction.current().substring(5).toInt()
            } catch (e: NumberFormatException) {
                throw InstructionParseException("Could not parse item amount", e)
            }
        } else if ("amount".equals(instruction.current(), ignoreCase = true)) {
            type = Type.AMOUNT
        } else {
            throw InstructionParseException(
                java.lang.String.format(
                    "Unknown variable type: '%s'",
                    instruction.current()
                )
            )
        }
    }

    override fun getValue(playerID: String?): String {
        val player: Player = PlayerConverter.getPlayer(playerID) ?: return ""
        var playersAmount = 0
        for (item in player.inventory.contents ?: return "") {
            if (item == null) {
                continue
            }
            val nbtItem = NBTItem.get(item)
            if (nbtItem.type != mmoItemType || nbtItem.getString("MMOITEMS_ITEM_ID") != mmoItemID) {
                continue
            }
            playersAmount += item.amount
        }
        return when (type) {
            Type.AMOUNT -> playersAmount.toString()
            Type.LEFT -> (amount - playersAmount).toString()
            else -> ""
        }
    }

    private enum class Type {
        AMOUNT, LEFT
    }
}