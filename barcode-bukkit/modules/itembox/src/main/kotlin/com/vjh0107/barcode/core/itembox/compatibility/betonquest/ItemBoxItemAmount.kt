package com.vjh0107.barcode.core.itembox.compatibility.betonquest

import com.vjh0107.barcode.core.itembox.service.ItemBoxService
import com.vjh0107.barcode.framework.exceptions.playerdata.PlayerDataNotFoundException
import com.vjh0107.barcode.framework.koin.injector.inject
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.Variable
import org.betonquest.betonquest.utils.PlayerConverter

class ItemBoxItemAmount(instruction: Instruction) : Variable(instruction) {
    private val service: ItemBoxService by inject()

    override fun getValue(playerID: String?): String {
        val player = PlayerConverter.getPlayer(playerID) ?: return "0"
        return try {
            service.getItems(player).size.toString()
        } catch (exception: PlayerDataNotFoundException) {
            "0"
        }
    }
}